package com.learning.scrapper.controller;

import com.learning.scrapper.domain.CurrentPrice;
import com.learning.scrapper.domain.Price;
import com.learning.scrapper.service.PriceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("digital-gold-prices")
public class PriceController {
    private final PriceService priceService;

    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    @GetMapping("/get-historical-prices")
    public List<Price> getPrices() {
        return priceService.getHistoricalPrices();
    }

    @GetMapping("/get-yday-price")
    public Price getYdayPrice() {
        return priceService.getYdayPrice();
    }

    @GetMapping("/get-current-price")
    public CurrentPrice getCurrentPrice() throws IOException {
        return priceService.getCurrentPrice();
    }
    
    @GetMapping("/save-historical-prices")
    public void savePrices() throws IOException {
        priceService.saveHistoricalPrices();
    }

    @GetMapping("/save-yday-price")
    public void saveYdayPrice() throws IOException {
        priceService.saveYdayPrice();
    }
}
