package com.DahnTa.controller;

import com.DahnTa.dto.Auth.LoginRequestDTO;
import com.DahnTa.dto.Auth.LoginResponseDTO;
import com.DahnTa.dto.Auth.SignUpRequestDTO;
import com.DahnTa.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/password")
    public static void editPassword() {

    }



}
