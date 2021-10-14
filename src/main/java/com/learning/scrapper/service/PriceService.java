package com.learning.scrapper.service;

import com.learning.scrapper.domain.CurrentPrice;
import com.learning.scrapper.domain.Price;
import com.learning.scrapper.repository.PriceRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class PriceService {

    private final PriceRepository priceRepository;

    public PriceService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    public List<Price> getHistoricalPrices() {
        return priceRepository.getSavedHistoricalPrices();
    }

    public Price getYdayPrice() {
        String previousDate = LocalDate.now().minusDays(1).toString();
        return priceRepository.getSavedPriceByDate(previousDate);
    }

    public CurrentPrice getCurrentPrice() throws IOException {
        long currentTime = System.currentTimeMillis();
        double currentPrice = priceRepository.getCurrentPrice();

        return CurrentPrice.builder()
                .price(currentPrice)
                .timestamp(currentTime)
                .build();
    }

    public void saveHistoricalPrices() throws IOException {
        priceRepository.saveHistoricalPrices();
    }

    public void saveYdayPrice() throws IOException {
        String previousDate = LocalDate.now().minusDays(1).toString();
        priceRepository.savePriceByDate(previousDate);
    }
}
