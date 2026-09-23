package com.greenfield.loanorigination.config;

import com.greenfield.loanorigination.domain.service.impl.JwtService;
import com.greenfield.loanorigination.domain.service.impl.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@AllArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {


    private final JwtService jwtService;

    private final ApplicationContext applicationContext;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        String userName = null;

        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            token = authorizationHeader.substring(7);
            try {
                userName = jwtService.extractUserName(token);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }

        if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null){

            UserDetails userDetails = applicationContext.getBean(MyUserDetailsService.class)
                    .loadUserByUsername(userName);

            try {
                if(jwtService.validateToken(token, userDetails)){
                    UsernamePasswordAuthenticationToken authenticationToken
                            = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }

        }
        filterChain.doFilter(request, response);
    }
}
