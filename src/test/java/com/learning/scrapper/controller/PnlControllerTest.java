package com.learning.scrapper.controller;

import com.learning.scrapper.service.PnlService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class PnlControllerTest {

    private static PnlController pnlController;
    private static PnlService pnlService;

    @BeforeAll
    public static void start() {
        pnlService = mock(PnlService.class);
        pnlController = new PnlController(pnlService);
    }

    @Test
    public void shouldCallGetPnl() {
        String sellDate = "2000-01-02";
        String buyDate = "2000-01-01";

        when(pnlService.getPnl(anyString(), anyString())).thenReturn(Map.of("PnL", 100));
        pnlController.getPnl(buyDate, sellDate);
        verify(pnlService, times(1)).getPnl(buyDate, sellDate);
    }
}