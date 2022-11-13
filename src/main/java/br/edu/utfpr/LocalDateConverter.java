package br.edu.utfpr;

import com.opencsv.bean.AbstractBeanField;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateConverter extends AbstractBeanField<String, LocalDate> {

    @Override
    protected LocalDate convert(String value) {
    	DateTimeFormatter formatar = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    	LocalDate dataConvertida = LocalDate.parse(value, formatar);
        return dataConvertida;
    }
}
