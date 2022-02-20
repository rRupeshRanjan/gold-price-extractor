package com.learning.scrapper.service;

import com.learning.scrapper.config.AppConfig;
import com.learning.scrapper.domain.Price;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PnlServiceTest {

    private static PnlService pnlService;
    private static PriceService priceService;

    @BeforeAll
    static void start() {
        priceService = mock(PriceService.class);
        AppConfig appConfig = mock(AppConfig.class);
        when(appConfig.getTaxPercentage()).thenReturn(3.0);

        pnlService = new PnlService(priceService, appConfig);
    }

    @Test
    public void shouldReturnErrorMessageInGetPnlWhenBuyDateAfterSellDate() {
        String sellDate = "2020-02-01"; // 1 Feb 2020
        String buyDate = "2020-03-01"; // 1 Mar 2020
        Map<String, String> expectedResponse = Map.of("Error", "Buy Date can't be after sell Date");

        Map<String, Object> actualResponse = pnlService.getPnl(buyDate, sellDate);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void shouldReturnMissingDateResponseWhenBuyDatePriceMissing() {
        String sellDate = "2020-04-01"; // 1 Apr 2020
        String buyDate = "2020-03-01"; // 1 Mar 2020
        String message = "price not available for date " + buyDate;
        Price sellDateClosePrice = Price.builder().closePrice(123.00).build();

        Map<String, Object> expectedResponse = Map.of("Error", message);

        when(priceService.getPriceForDate(buyDate)).thenReturn(Optional.empty());
        when(priceService.getPriceForDate(sellDate)).thenReturn(Optional.of(sellDateClosePrice));

        Map<String, Object> actualResponse = pnlService.getPnl(buyDate, sellDate);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void shouldReturnMissingDateResponseWhenBuySellPriceMissing() {
        String sellDate = "2020-04-01"; // 1 Apr 2020
        String buyDate = "2020-03-01"; // 1 Mar 2020
        String message = "price not available for date " + sellDate;
        Price buyDateClosePrice = Price.builder().closePrice(123.00).build();

        Map<String, Object> expectedResponse = Map.of("Error", message);

        when(priceService.getPriceForDate(buyDate)).thenReturn(Optional.of(buyDateClosePrice));
        when(priceService.getPriceForDate(sellDate)).thenReturn(Optional.empty());

        Map<String, Object> actualResponse = pnlService.getPnl(buyDate, sellDate);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void shouldReturnLossInPnL() {
        String buyDate = "2020-03-01"; // 1 Mar 2020
        String sellDate = "2020-04-01"; // 1 Apr 2020
        Price buyDateClosePrice = Price.builder().closePrice(100.00).build();
        Price sellDateClosePrice = Price.builder().closePrice(103.00).build();

        when(priceService.getPriceForDate(buyDate)).thenReturn(Optional.of(buyDateClosePrice));
        when(priceService.getPriceForDate(sellDate)).thenReturn(Optional.of(sellDateClosePrice));

        Map<String, Object> expectedResponse = Map.of("PnL", -3.0);
        Map<String, Object> actualResponse = pnlService.getPnl(buyDate, sellDate);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void shouldReturnGainInPnL() {
        String buyDate = "2020-03-01"; // 1 Mar 2020
        String sellDate = "2020-04-01"; // 1 Apr 2020
        Price buyDateClosePrice = Price.builder().closePrice(100.00).build();
        Price sellDateClosePrice = Price.builder().closePrice(206.00).build();

        when(priceService.getPriceForDate(buyDate)).thenReturn(Optional.of(buyDateClosePrice));
        when(priceService.getPriceForDate(sellDate)).thenReturn(Optional.of(sellDateClosePrice));

        Map<String, Object> expectedResponse = Map.of("PnL", 97.0);
        Map<String, Object> actualResponse = pnlService.getPnl(buyDate, sellDate);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    public void shouldReturnErrorMessageMapInHandleMissingDate() {
        String date = "2020-02-01";
        String message = "price not available for date " + date;
        Map<String, Object> expectedResponse = Map.of("Error", message);

        Map<String, Object> errorMessageMap = pnlService.handleMissingDates(date);
        assertEquals(expectedResponse, errorMessageMap);
    }

    @Test
    public void shouldReturnTrueWhenBuyDateAfterSellDate() {
        String sellDate = "2020-02-01"; // 1 Feb 2020
        String buyDate = "2020-03-01"; // 1 Mar 2020
        assertTrue(pnlService.isBuyDateAfterSellDate(buyDate, sellDate));
    }

    @Test
    public void shouldReturnFalseWhenBuyDateBeforeSellDate() {
        String sellDate = "2020-03-01"; // 1 Mar 2020
        String buyDate = "2020-02-01"; // 1 Feb 2020
        assertFalse(pnlService.isBuyDateAfterSellDate(buyDate, sellDate));
    }
}