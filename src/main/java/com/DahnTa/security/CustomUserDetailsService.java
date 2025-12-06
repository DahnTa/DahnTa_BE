package com.DahnTa.security;

import com.DahnTa.entity.Enum.ErrorCode;
import com.DahnTa.entity.User;
import com.DahnTa.exception.UserStockException;
import com.DahnTa.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userAccount) {
        User user = userRepository.findByUserAccount(userAccount)
            .orElseThrow(() -> new UserStockException(ErrorCode.USER_NOT_FOUND));
        return new CustomUserDetails(user);
    }

    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserStockException(ErrorCode.USER_NOT_FOUND));
        return new CustomUserDetails(user);
    }
}
