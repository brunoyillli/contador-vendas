package br.edu.utfpr;

import com.opencsv.bean.CsvToBeanBuilder;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public class SalesReader {

    private final List<Sale> sales;

    public SalesReader(String salesFile) {

        final var dataStream = ClassLoader.getSystemResourceAsStream(salesFile);

        if (dataStream == null) {
            throw new IllegalStateException("File not found or is empty");
        }

        final var builder = new CsvToBeanBuilder<Sale>(new InputStreamReader(dataStream, StandardCharsets.UTF_8));

        sales = builder
                .withType(Sale.class)
                .withSeparator(';')
                .build()
                .parse();
    }

    public BigDecimal totalOfCompletedSales() {
        // TODO implementar
        return BigDecimal.ZERO;
    }

    public BigDecimal totalOfCancelledSales() {
        // TODO implementar
        return BigDecimal.ZERO;
    }

    public Optional<Sale> mostRecentCompletedSale() {
        // TODO implementar
        return Optional.empty();
    }

    public long daysBetweenFirstAndLastCancelledSale() {
        // TODO implementar
        return 0;
    }

    public BigDecimal totalCompletedSalesBySeller(String sellerName) {
        // TODO implementar
        return BigDecimal.ZERO;
    }

    public long countAllSalesByManager(String managerName) {
        // TODO implementar
        return 0;
    }

    public BigDecimal totalSalesByStatusAndMonth(Sale.Status status, Month... months) {
        // TODO implementar
        return BigDecimal.ZERO;
    }

    public Map<String, Long> countCompletedSalesByDepartment() {
        // TODO implementar
        return Map.of();
    }

    public Map<Integer, Map<String, Long>> countCompletedSalesByPaymentMethodAndGroupingByYear() {
        // TODO implementar
        return Map.of();
    }

    public Map<String, BigDecimal> top3BestSellers() {
        // TODO implementar
        return Map.of();
    }
}
