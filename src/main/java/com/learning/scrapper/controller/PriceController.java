package com.learning.scrapper.controller;

import com.learning.scrapper.domain.CurrentPrice;
import com.learning.scrapper.domain.Price;
import com.learning.scrapper.service.PriceService;
import org.springframework.web.bind.annotation.*;

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
    public List<Price> getPrices(@RequestParam(defaultValue = "30") int size) {
        return priceService.getHistoricalPrices(size);
    }

    @GetMapping("/get-yday-price")
    public Price getYdayPrice() {
        return priceService.getYdayPrice();
    }

    @GetMapping("/get-current-price")
    public CurrentPrice getCurrentPrice() throws IOException {
        return priceService.getCurrentPrice();
    }

    @PostMapping("/save-historical-prices")
    public void savePrices() throws IOException {
        priceService.saveHistoricalPrices();
    }

    @PostMapping("/save-yday-price")
    public void saveYdayPrice() throws IOException {
        priceService.saveYdayPrice();
    }
}
