package com.greenfield.loanorigination.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity){

        httpSecurity
                .csrf(customiser -> customiser.disable())
       // .authorizeHttpRequests(requests -> requests.anyRequest().authenticated())
                .authorizeHttpRequests(requests -> requests.requestMatchers("/register", "/login")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
        .httpBasic(Customizer.withDefaults())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);



//        httpSecurity.csrf(customiser -> customiser.disable());
//        httpSecurity.authorizeHttpRequests(requests -> requests.anyRequest().authenticated());
//        httpSecurity.formLogin(Customizer.withDefaults()); //FormLogin not needed if session is stateless
//        httpSecurity.httpBasic(Customizer.withDefaults());
//        httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return httpSecurity.build();
    }

//    @Bean
//    public UserDetailsService  userDetailsService(){
//
//        UserDetails user = User
//        .withDefaultPasswordEncoder()
//                .username("Anaya")
//                .password("password")
//                .roles("USER")
//                .build();
//
//        UserDetails admin = User
//                .withDefaultPasswordEncoder()
//                .username("Admin")
//                .password("password")
//                .roles("ADMIN")
//                .build();
//
//        return new InMemoryUserDetailsManager(user, admin);
//    }

    @Bean
    public AuthenticationProvider  authenticationProvider(){

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder(12));

        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration  authenticationConfiguration){
        return authenticationConfiguration.getAuthenticationManager();
    }
}
