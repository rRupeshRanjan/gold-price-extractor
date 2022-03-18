package com.learning.scrapper.service;

import com.learning.scrapper.domain.CurrentPrice;
import com.learning.scrapper.domain.Price;
import com.learning.scrapper.repository.PriceRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class PriceService {

    private final PriceRepository priceRepository;

    public PriceService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    public List<Price> getHistoricalPrices(int size) {
        return priceRepository.getSavedHistoricalPrices(size);
    }

    public Optional<Price> getYdayPrice() {
        String previousDate = LocalDate.now().minusDays(1).toString();

        log.info("Fetching prices for yesterday: {}", previousDate);
        return priceRepository.getSavedPriceByDate(previousDate);
    }

    public CurrentPrice getCurrentPrice() throws IOException {
        long currentTime = System.currentTimeMillis();

        log.info("Fetching today's prices at timestamp: {}", currentTime);
        String currentPrice = priceRepository.getCurrentPrice();

        return CurrentPrice.builder()
                .price(currentPrice)
                .timestamp(currentTime)
                .build();
    }

    public void saveHistoricalPrices() throws IOException {
        log.info("Saving historical prices for last 30 days");
        priceRepository.saveHistoricalPrices();
    }

    public Optional<Price> getPriceForDate(String date) {
        return priceRepository.getSavedPriceByDate(date);
    }
}
