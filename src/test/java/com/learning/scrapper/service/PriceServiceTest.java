package com.learning.scrapper.service;

import com.learning.scrapper.domain.CurrentPrice;
import com.learning.scrapper.domain.Price;
import com.learning.scrapper.repository.PriceRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class PriceServiceTest {

    private static PriceService priceService;
    private static PriceRepository priceRepository;

    @BeforeAll
    static void start() {
        priceRepository = mock(PriceRepository.class);
        priceService = new PriceService(priceRepository);
    }

    @Test
    void shouldCallPriceRepositoryForHistoricalPrices() {
        when(priceRepository.getSavedHistoricalPrices(anyInt())).thenReturn(Collections.emptyList());
        priceService.getHistoricalPrices(30);
        verify(priceRepository, times(1)).getSavedHistoricalPrices(30);
    }

    @Test
    void shouldGetPreviousDayPrice() {
        String previousDate = LocalDate.now().minusDays(1).toString();
        Price expectedPrice = Price.builder().build();

        when(priceRepository.getSavedPriceByDate(previousDate)).thenReturn(Optional.of(expectedPrice));

        Optional<Price> actualPrice = priceService.getYdayPrice();
        assertTrue(actualPrice.isPresent());
        assertEquals(expectedPrice, actualPrice.get());
    }

    @Test
    void shouldGetEmptyOptionalForPreviousDayPrice() {
        String previousDate = LocalDate.now().minusDays(1).toString();

        when(priceRepository.getSavedPriceByDate(previousDate)).thenReturn(Optional.empty());

        Optional<Price> actualPrice = priceService.getYdayPrice();
        assertTrue(actualPrice.isEmpty());
    }

    @Test
    void shouldGetCurrentPrice() throws IOException {
        double expectedPrice = 100.0;
        when(priceRepository.getCurrentPrice()).thenReturn(expectedPrice);
        CurrentPrice actualPrice = priceService.getCurrentPrice();
        assertEquals(expectedPrice, actualPrice.getPrice());
    }

    @Test
    void shouldCallPriceRepositoryToSaveHistoricalPrices() throws IOException {
        priceService.saveHistoricalPrices();
        verify(priceRepository, times(1)).saveHistoricalPrices();
    }

    @Test
    void shouldSaveYdayPrice() throws IOException {
        String previousDate = LocalDate.now().minusDays(1).toString();
        priceService.saveYdayPrice();
        verify(priceRepository, times(1)).savePriceByDate(previousDate);
    }

    @Test
    void shouldGetPriceForSpecifiedDate() {
        String date = "2020-01-01";
        priceService.getPriceForDate(date);
        verify(priceRepository, times(1)).getSavedPriceByDate(date);
    }

    @Test
    void shouldGetEmptyOptionalPriceForSpecifiedDate() {
        String date = "2020-01-01";
        when(priceRepository.getSavedPriceByDate(anyString())).thenReturn(Optional.empty());
        Optional<Price> actualPrice = priceService.getPriceForDate(date);

        assertTrue(actualPrice.isEmpty());
    }
}