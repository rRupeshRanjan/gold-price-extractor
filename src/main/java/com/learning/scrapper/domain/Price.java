package com.learning.scrapper.domain;

import com.learning.scrapper.config.AppConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Price {
    @Id
    private LocalDate date;

    private double price;
    private double openPrice;
    private double closePrice;
    private double lowestPrice;
    private double highestPrice;
    private String delta;

    public static Price buildPriceFromList(List<String> list, DateTimeFormatter dateTimeFormatter) {
        return Price.builder()
                .date(LocalDate.parse(list.get(0), dateTimeFormatter))
                .price(formatDouble(list.get(1)))
                .openPrice(formatDouble(list.get(2)))
                .closePrice(formatDouble(list.get(3)))
                .lowestPrice(formatDouble(list.get(4)))
                .highestPrice(formatDouble(list.get(5)))
                .delta(list.get(6))
                .build();
    }

    private static double formatDouble(String s) {
        return Double.parseDouble(s.replace(",", "").substring(2));
    }
}
