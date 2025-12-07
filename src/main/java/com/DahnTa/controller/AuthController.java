package com.DahnTa.controller;

import com.DahnTa.dto.request.LoginRequestDTO;
import com.DahnTa.dto.response.LoginResponseDTO;
import com.DahnTa.dto.request.PasswordRequestDTO;
import com.DahnTa.dto.request.SignUpRequestDTO;
import com.DahnTa.security.CustomUserDetails;
import com.DahnTa.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequestDTO signUpRequestDTO) {
         authService.signupUser(signUpRequestDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> loginUser(@RequestBody LoginRequestDTO loginRequestDTO) {
       LoginResponseDTO responseDTO = authService.authenticateToken(loginRequestDTO);
        return ResponseEntity.ok().body(responseDTO);
    }

    // @todo : token 검증 로직 service layer로 내리기
    @PostMapping("/password")
    public ResponseEntity<?> updatePassword(@AuthenticationPrincipal CustomUserDetails userDetails,
       @RequestBody PasswordRequestDTO updatePassword) throws Exception {

        // 결과에 따른 반환
        authService.changePassword(userDetails.getUser(), updatePassword);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
