package br.edu.utfpr;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.opencsv.bean.CsvToBeanBuilder;

public class SalesReader {

	private final List<Sale> sales;

	public SalesReader(String salesFile) {

		final var dataStream = ClassLoader.getSystemResourceAsStream(salesFile);

		if (dataStream == null) {
			throw new IllegalStateException("File not found or is empty");
		}

		final var builder = new CsvToBeanBuilder<Sale>(new InputStreamReader(dataStream, StandardCharsets.UTF_8));

		sales = builder.withType(Sale.class).withSeparator(';').build().parse();
	}

	public BigDecimal totalOfCompletedSales() {
		BigDecimal valorTotalVendasCompletas = sales.stream().filter(sale -> sale.isCompleted()).map(Sale::getValue)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		return valorTotalVendasCompletas;
	}

	public BigDecimal totalOfCancelledSales() {
		BigDecimal valorTotalVendasCanceladas = sales.stream().filter(sale -> sale.isCancelled()).map(Sale::getValue)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		return valorTotalVendasCanceladas;
	}

	public Optional<Sale> mostRecentCompletedSale() {
		Optional<Sale> vendaMaisRecente = sales.stream().filter(sale -> sale.isCompleted())
				.max(Comparator.comparing(Sale::getSaleDate));
		return vendaMaisRecente;
	}

	public long daysBetweenFirstAndLastCancelledSale() {

		Optional<Sale> vendaCanceladaMaisRecente = sales.stream().filter(sale -> sale.isCancelled())
				.min(Comparator.comparing(Sale::getSaleDate));

		Optional<Sale> vendaCanceladaMaisAntiga = sales.stream().filter(sale -> sale.isCancelled())
				.max(Comparator.comparing(Sale::getSaleDate));

		long diasEntre = ChronoUnit.DAYS.between(vendaCanceladaMaisRecente.get().getSaleDate(),
				vendaCanceladaMaisAntiga.get().getSaleDate());

		return diasEntre;
	}

	public BigDecimal totalCompletedSalesBySeller(String sellerName) {
		BigDecimal valorVendasVendedor = sales.stream()
				.filter(sale -> sale.isCompleted() && sale.getSeller().equals(sellerName)).map(Sale::getValue)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		return valorVendasVendedor;
	}

	public long countAllSalesByManager(String managerName) {
		long quantidadeVendasGerente = sales.stream().filter(sale -> sale.getManager().equals(managerName)).count();
		return quantidadeVendasGerente;
	}

	public BigDecimal totalSalesByStatusAndMonth(Sale.Status status, Month... months) {
		BigDecimal valorVendasStatusEntre = sales.stream()
				.filter(sale -> sale.getStatus().equals(status)
						&& Arrays.stream(months).toList().contains(sale.getSaleDate().getMonth()))
				.map(Sale::getValue).reduce(BigDecimal.ZERO, BigDecimal::add);
		return valorVendasStatusEntre;
	}

	public Map<String, Long> countCompletedSalesByDepartment() {
		Map<String, Long> quantidadeVendasPorDepartamento = sales.stream().filter(sale -> sale.isCompleted())
				.collect(Collectors.groupingBy(Sale::getDepartment, Collectors.counting()));
		return quantidadeVendasPorDepartamento;
	}

	public Map<Integer, Map<String, Long>> countCompletedSalesByPaymentMethodAndGroupingByYear() {
		Map<Integer, Map<String, Long>> quantidadeVendasPorPagamentoPorAno = sales.stream()
				.filter(sale -> sale.isCompleted()).collect(Collectors.groupingBy(sale -> sale.getSaleDate().getYear(),
						Collectors.groupingBy(Sale::getPaymentMethod, Collectors.counting())));

		return quantidadeVendasPorPagamentoPorAno;
	}

	public Map<String, BigDecimal> top3BestSellers() {
		Map<String, BigDecimal> melhoresVendedoresTop3 = sales.stream().filter(sale -> sale.isCompleted())
				.collect(Collectors.groupingBy(Sale::getSeller,
						Collectors.collectingAndThen(Collectors.toList(),
								sale -> sale.stream().map(Sale::getValue).reduce(BigDecimal.ZERO, BigDecimal::add))))
				.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (key, value) -> key,
						LinkedHashMap::new))
				.entrySet().stream().limit(3).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
						(key, value) -> key, LinkedHashMap::new));

		return melhoresVendedoresTop3;
	}
}
