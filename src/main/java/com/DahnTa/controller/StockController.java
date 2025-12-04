package com.DahnTa.controller;

import com.DahnTa.dto.request.StockBuyRequest;
import com.DahnTa.dto.request.StockSellRequest;
import com.DahnTa.dto.response.MacroIndicatorsResponse;
import com.DahnTa.dto.response.StockCompanyFinanceResponse;
import com.DahnTa.dto.response.StockListResponse;
import com.DahnTa.dto.response.StockNewsResponse;
import com.DahnTa.dto.response.StockOrderResponse;
import com.DahnTa.dto.response.StockRedditResponse;
import com.DahnTa.dto.response.StockResponse;
import com.DahnTa.dto.response.StockTotalAnalysisResponse;
import com.DahnTa.service.StockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stock")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping
    public ResponseEntity<StockListResponse> getStockList() {
        StockListResponse response = stockService.getStockList();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockResponse> getStock(@PathVariable(name = "id") Long stockId) {
        StockResponse response = stockService.getStock(stockId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}/order")
    public ResponseEntity<StockOrderResponse> getStockOrder(@PathVariable(name = "id") Long stockId) {
        StockOrderResponse response = stockService.getStockOrder(stockId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}/news")
    public ResponseEntity<StockNewsResponse> getStockNews(@PathVariable(name = "id") Long stockId) {
        StockNewsResponse response = stockService.getStockNews(stockId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}/company")
    public ResponseEntity<StockCompanyFinanceResponse> getStockCompanyFinance(
        @PathVariable(name = "id") Long stockId) {
        StockCompanyFinanceResponse response = stockService.getStockCompanyFinance(stockId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/macro")
    public ResponseEntity<MacroIndicatorsResponse> getMacroIndicators() {
        MacroIndicatorsResponse response = stockService.getMacroIndicators();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}/reddit")
    public ResponseEntity<StockRedditResponse> getReddit(@PathVariable(name = "id") Long stockId) {
        StockRedditResponse response = stockService.getReddit(stockId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}/total")
    public ResponseEntity<StockTotalAnalysisResponse> getTotalAnalysis(
        @PathVariable(name = "id") Long stockId) {
        StockTotalAnalysisResponse response = stockService.getTotalAnalysis(stockId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/start")
    public ResponseEntity<Void> gameStart() {
        stockService.gameStart();

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{id}/orders/buy")
    public ResponseEntity<Void> stockBuy(@PathVariable(name = "id") Long stockId, @RequestBody
        StockBuyRequest request) {
        stockService.stockBuy(stockId, request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{id}/orders/sell")
    public ResponseEntity<Void> stockSell(@PathVariable(name = "id") Long stockId, @RequestBody
        StockSellRequest request) {
        stockService.stockSell(stockId, request);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/next")
    public ResponseEntity<Void> gameDateNext() {
        stockService.gameDateNext();

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
