package com.example.backend.security_providers;

import com.example.backend.details.UserDetails;
import com.example.backend.services.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserProvider implements AuthenticationProvider {
    @Autowired
    private UserDetailService userDetailService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDetails userDetails = userDetailService.loadUserByUsername(username);
        if (username.isEmpty() || password.isEmpty()) {
            throw new BadCredentialsException("Необходимо ввести и логин, и пароль!");
        }
        if (!userDetails.exists()) {
            throw new BadCredentialsException("Пользователя "+username+" не существует!");
        }
        if (!encoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Введён неверный пароль!");
        }
        return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
