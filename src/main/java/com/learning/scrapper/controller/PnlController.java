package com.learning.scrapper.controller;

import com.learning.scrapper.service.PnlService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("pnl")
public class PnlController {

    private final PnlService pnlService;

    public PnlController(PnlService pnlService) {
        this.pnlService = pnlService;
    }

    @RequestMapping("/calculatePnl")
    public Map<String, Object> getPnl(@RequestParam String buyDate, @RequestParam String sellDate) {
        return pnlService.getPnl(buyDate, sellDate);
    }
}
