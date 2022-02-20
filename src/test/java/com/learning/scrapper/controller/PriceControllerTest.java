package com.learning.scrapper.controller;

import com.learning.scrapper.domain.CurrentPrice;
import com.learning.scrapper.service.PriceService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;

class PriceControllerTest {

    private static PriceController priceController;
    private static PriceService priceService;

    @BeforeAll
    static void start() {
        priceService = mock(PriceService.class);
        priceController = new PriceController(priceService);
    }

    @Test
    public void shouldCallGetHistoricalPrices() {
        when(priceService.getHistoricalPrices(anyInt())).thenReturn(Collections.emptyList());
        priceController.getPrices(30);

        verify(priceService, times(1)).getHistoricalPrices(30);
    }

    @Test
    public void shouldCallGetYdayPrice() {
        when(priceService.getYdayPrice()).thenReturn(Optional.empty());
        priceController.getYdayPrice();

        verify(priceService, times(1)).getYdayPrice();
    }

    @Test
    public void shouldCallGetCurrentPrice() throws IOException {
        when(priceService.getCurrentPrice()).thenReturn(mock(CurrentPrice.class));
        priceController.getCurrentPrice();

        verify(priceService, times(1)).getCurrentPrice();
    }

    @Test
    public void shouldCallSaveHistoricalPrices() throws IOException {
        priceController.savePrices();
        verify(priceService, times(1)).saveHistoricalPrices();
    }

    @Test
    public void shouldCallSaveYdayPrice() throws IOException {
        priceController.saveYdayPrice();
        verify(priceService, times(1)).saveYdayPrice();
    }
}