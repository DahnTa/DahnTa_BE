package com.DahnTa.controller;

import com.DahnTa.dto.AuthDTO;
import com.DahnTa.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/auth")
public class AuthController {

    @Autowired
    private static AuthService authService;

    @PostMapping("/signup")
    public static ResponseEntity<?> registerUser(@RequestBody AuthDTO authDTO) {
         authService.signupUser(authDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public static void loginUser(@RequestBody AuthDTO authDTO) {
       AuthDTO responseDTO = authService.authenticateToken(authDTO);

    }

    @PostMapping("/password")
    public static void editPassword() {

    }



}
