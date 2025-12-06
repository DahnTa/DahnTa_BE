package com.DahnTa.controller;

import com.DahnTa.dto.response.TransactionListResponseDTO;
import com.DahnTa.service.UserStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserStockController {
    private final UserStockService userStockService;

    @PostMapping("/{id}/dislike")
    public ResponseEntity<?> dislikePost(@RequestHeader("Authorization") String token,
        @PathVariable("id") Long stockId) throws Exception {

        userStockService.applyDislike(stockId, token);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/{id}/like")
    public ResponseEntity<?> likePost(@RequestHeader("Authorization") String token,
        @PathVariable("id") Long stockId) {

        userStockService.applyLike(stockId, token);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/transaction")
    public ResponseEntity<TransactionListResponseDTO> getTransactions(@RequestHeader("Authorization") String token) {
        TransactionListResponseDTO responseDTO = userStockService.getTransactionHistory(token);

        return ResponseEntity.ok().body(responseDTO);
    }
}
