package com.learning.scrapper.service;

import com.learning.scrapper.domain.Price;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Log4j2
@Service
public class PnlService {

    private final PriceService priceService;
    private final double taxFactor;

    public PnlService(PriceService priceService) {
        this.priceService = priceService;
        this.taxFactor = 1.03;
    }

    public Map<String, Object> getPnl(String buyDate, String sellDate) {
        if (isBuyDateAfterSellDate(buyDate, sellDate))
            return Map.of("Error", "Buy Date can't be after sell Date");

        Optional<Price> buyPrice = priceService.getPriceForDate(buyDate);
        Optional<Price> sellPrice = priceService.getPriceForDate(sellDate);

        if (buyPrice.isEmpty()) {
            return handleMissingDates(buyDate);
        } else if (sellPrice.isEmpty()) {
            return handleMissingDates(sellDate);
        } else {
            double pnl = (sellPrice.get().getClosePrice() / taxFactor) - (buyPrice.get().getClosePrice() * taxFactor);
            return Map.of("PnL", pnl);
        }
    }

    private Map<String, Object> handleMissingDates(String date) {
        String message = "price not available for date " + date;
        log.info(message);
        return Map.of("Error", message);
    }

    private boolean isBuyDateAfterSellDate(String buyDate, String sellDate) {
        return LocalDate.parse(buyDate).isAfter(LocalDate.parse(sellDate));
    }
}
