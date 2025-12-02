package com.DahnTa.controller;

import com.DahnTa.dto.response.StockListResponse;
import com.DahnTa.service.StockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<StockListResponse> getStockListResponse() {
        StockListResponse response = stockService.getStockListResponse();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
