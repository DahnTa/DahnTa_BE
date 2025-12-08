package com.DahnTa.controller;

import com.DahnTa.dto.request.StockBuyRequest;
import com.DahnTa.dto.request.StockSellRequest;
import com.DahnTa.dto.response.MacroIndicatorsResponse;
import com.DahnTa.dto.response.StockCompanyFinanceResponse;
import com.DahnTa.dto.response.StockGameResultResponse;
import com.DahnTa.dto.response.StockListResponse;
import com.DahnTa.dto.response.StockNewsResponse;
import com.DahnTa.dto.response.StockOrderResponse;
import com.DahnTa.dto.response.StockRedditResponse;
import com.DahnTa.dto.response.StockResponse;
import com.DahnTa.dto.response.StockTotalAnalysisResponse;
import com.DahnTa.security.CustomUserDetails;
import com.DahnTa.service.StockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping("/start")
    public ResponseEntity<Void> gameStart(@AuthenticationPrincipal CustomUserDetails userDetails) {
        stockService.gameStart(userDetails.getUser());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/next")
    public ResponseEntity<Void> gameDateNext(@AuthenticationPrincipal CustomUserDetails userDetails) {
        stockService.gameDateNext(userDetails.getUser());

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/finish")
    public ResponseEntity<Void> gameFinish(@AuthenticationPrincipal CustomUserDetails userDetails) {
        stockService.gameFinish(userDetails.getUser());

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/{id}/orders/buy")
    public ResponseEntity<Void> stockBuy(@AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable(name = "id") Long stockId, @RequestBody StockBuyRequest request) {
        stockService.stockBuy(userDetails.getUser(), stockId, request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{id}/orders/sell")
    public ResponseEntity<Void> stockSell(@AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable(name = "id") Long stockId, @RequestBody StockSellRequest request) {
        stockService.stockSell(userDetails.getUser(), stockId, request);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    public ResponseEntity<StockListResponse> getStockList(
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        StockListResponse response = stockService.getStockList(userDetails.getUser());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockResponse> getStock(@AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable(name = "id") Long stockId) {
        StockResponse response = stockService.getStock(userDetails.getUser(), stockId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}/order")
    public ResponseEntity<StockOrderResponse> getStockOrder(
        @AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable(name = "id") Long stockId) {
        StockOrderResponse response = stockService.getStockOrder(userDetails.getUser(), stockId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}/news")
    public ResponseEntity<StockNewsResponse> getStockNews(
        @AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable(name = "id") Long stockId) {
        StockNewsResponse response = stockService.getStockNews(userDetails.getUser(), stockId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}/company")
    public ResponseEntity<StockCompanyFinanceResponse> getStockCompanyFinance(
        @AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable(name = "id") Long stockId) {
        StockCompanyFinanceResponse response = stockService.getStockCompanyFinance(userDetails.getUser(),
            stockId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/macro")
    public ResponseEntity<MacroIndicatorsResponse> getMacroIndicators(
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        MacroIndicatorsResponse response = stockService.getMacroIndicators(userDetails.getUser());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}/reddit")
    public ResponseEntity<StockRedditResponse> getReddit(
        @AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable(name = "id") Long stockId) {
        StockRedditResponse response = stockService.getReddit(userDetails.getUser(), stockId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}/total")
    public ResponseEntity<StockTotalAnalysisResponse> getTotalAnalysis(
        @AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable(name = "id") Long stockId) {
        StockTotalAnalysisResponse response = stockService.getTotalAnalysis(userDetails.getUser(), stockId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/result")
    public ResponseEntity<StockGameResultResponse> gatGameResult(
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        StockGameResultResponse response = stockService.gatGameResult(userDetails.getUser());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
