package com.greenfield.loanorigination.domain.service.impl;

import com.greenfield.loanorigination.api.dto.User.UserPrincipal;
import com.greenfield.loanorigination.domain.entity.User;
import com.greenfield.loanorigination.repository.UserRepoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepoRepository userRepoRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepoRepository.findByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new UserPrincipal(user);
    }

    public User saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepoRepository.save(user) ;

    }
}
