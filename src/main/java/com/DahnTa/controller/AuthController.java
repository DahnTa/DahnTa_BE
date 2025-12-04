package com.DahnTa.controller;

import com.DahnTa.dto.Auth.LoginRequestDTO;
import com.DahnTa.dto.Auth.LoginResponseDTO;
import com.DahnTa.dto.Auth.PasswordRequestDTO;
import com.DahnTa.dto.Auth.SignUpRequestDTO;
import com.DahnTa.service.AuthService;
import com.DahnTa.service.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final JWTService jwtService;

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

    @PostMapping("/password")
    public ResponseEntity<?> updatePassword(@RequestHeader("Authorization") String token,
       @RequestBody PasswordRequestDTO updatePassword) throws Exception {
        String actualToken = token;
        if (token.startsWith("Bearer ")) {
            actualToken = token.substring(7);
        }

        // 토큰 검증
        if (jwtService.validateToken(actualToken)) {
            // @todo : token pasring으로 Id 추출
             Long userId = jwtService.extractUserId(actualToken);

            authService.changePassword(userId, updatePassword.getPassword());
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
          throw new Exception("토큰이 유효하지 않음");
        }

    }

}
