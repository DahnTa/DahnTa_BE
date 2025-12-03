package com.DahnTa.controller;

import com.DahnTa.dto.response.StockCompanyFinanceResponse;
import com.DahnTa.dto.response.StockListResponse;
import com.DahnTa.dto.response.StockNewsResponse;
import com.DahnTa.dto.response.StockOrderResponse;
import com.DahnTa.dto.response.StockResponse;
import com.DahnTa.service.StockService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stock")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping("/start")
    public ResponseEntity<Void> gameStart() {
        stockService.gameStart();

        return ResponseEntity.status(HttpStatus.OK).build();
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
}
