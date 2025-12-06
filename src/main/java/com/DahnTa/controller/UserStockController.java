package com.DahnTa.controller;

import com.DahnTa.dto.response.AssetResponseDTO;
import com.DahnTa.dto.response.InterestListResponseDTO;
import com.DahnTa.dto.response.InterestResponseDTO;
import com.DahnTa.dto.response.TransactionListResponseDTO;
import com.DahnTa.service.UserStockService;
import java.util.List;
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

    @GetMapping("/interest")
    public ResponseEntity<InterestListResponseDTO> getInterest(@RequestHeader("Authorization") String token) {

        List<InterestResponseDTO> list = userStockService.getInterestList(token);

        return ResponseEntity.ok().body(new InterestListResponseDTO(list));
    }


    @GetMapping("/asset")
    public ResponseEntity<AssetResponseDTO> getAsset(@RequestHeader("Authorization") String token) {
         AssetResponseDTO assets = userStockService.getAssets(token);
        return ResponseEntity.ok(assets);
    }

}
