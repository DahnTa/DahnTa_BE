package com.DahnTa.controller;

import com.DahnTa.dto.response.AssetResponseDTO;
import com.DahnTa.dto.response.HoldingsListResponseDTO;
import com.DahnTa.dto.response.InterestListResponseDTO;
import com.DahnTa.dto.response.InterestResponseDTO;
import com.DahnTa.dto.response.TransactionListResponseDTO;
import com.DahnTa.security.CustomUserDetails;
import com.DahnTa.service.UserStockService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @PostMapping("/interest/{id}/dislike")
    public ResponseEntity<?> dislikePost(@AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable("id") Long stockId) throws Exception {

        userStockService.applyDislike(stockId, userDetails.getUser());

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/interest/{id}/like")
    public ResponseEntity<?> likePost(@AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable("id") Long stockId) {

        userStockService.applyLike(stockId, userDetails.getUser());

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/transaction")
    public ResponseEntity<TransactionListResponseDTO> getTransactions(
        @AuthenticationPrincipal CustomUserDetails userDetails) {

        TransactionListResponseDTO responseDTO = userStockService.getTransactionHistory(userDetails.getUser());

        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/interest")
    public ResponseEntity<InterestListResponseDTO> getInterest(
        @AuthenticationPrincipal CustomUserDetails userDetails) {

        List<InterestResponseDTO> list = userStockService.getInterestList(userDetails.getUser());

        return ResponseEntity.ok().body(new InterestListResponseDTO(list));
    }


    @GetMapping("/asset")
    public ResponseEntity<AssetResponseDTO> getAsset(@AuthenticationPrincipal CustomUserDetails userDetails) {
        AssetResponseDTO assets = userStockService.getAssets(userDetails.getUser());
        return ResponseEntity.ok(assets);
    }


    @GetMapping("/holdings")
    public ResponseEntity<HoldingsListResponseDTO> getHoldings(
        @AuthenticationPrincipal CustomUserDetails userDetails) {

        HoldingsListResponseDTO holdings = userStockService.getHoldings(userDetails.getUser());
        return ResponseEntity.ok(holdings);

    }
}
