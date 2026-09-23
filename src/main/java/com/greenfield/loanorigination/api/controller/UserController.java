package com.greenfield.loanorigination.api.controller;

import com.greenfield.loanorigination.domain.entity.User;
import com.greenfield.loanorigination.domain.service.impl.JwtService;
import com.greenfield.loanorigination.domain.service.impl.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final MyUserDetailsService service;
    private final AuthenticationManager authenticationManager;
    private final JwtService  jwtService;

    @PostMapping("register")
    public User register(@RequestBody User user) {
        return service.saveUser(user);
    }

    @PostMapping("login")
    public String login(@RequestBody User user) throws NoSuchAlgorithmException {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        if(authentication.isAuthenticated())
            return jwtService.generateToken(user.getUsername());
        else
            return "Failed";
    }
}