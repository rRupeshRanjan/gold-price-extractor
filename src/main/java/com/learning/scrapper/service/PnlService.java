package com.learning.scrapper.service;

import com.learning.scrapper.config.AppConfig;
import com.learning.scrapper.domain.Price;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Log4j2
@Service
public class PnlService {

    private static final double PERCENTAGE = 100.0;
    private final PriceService priceService;
    private final double taxFactor;

    public PnlService(PriceService priceService, AppConfig appConfig) {
        this.priceService = priceService;
        this.taxFactor = 1 + (appConfig.getTaxPercentage() / PERCENTAGE);
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

    protected Map<String, Object> handleMissingDates(String date) {
        String message = "price not available for date " + date;
        log.info(message);
        return Map.of("Error", message);
    }

    protected boolean isBuyDateAfterSellDate(String buyDate, String sellDate) {
        return LocalDate.parse(buyDate).isAfter(LocalDate.parse(sellDate));
    }
}
