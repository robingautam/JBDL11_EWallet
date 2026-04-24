package org.gfg.TransactionService.service;

import org.gfg.model.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.client.RestTemplate;

public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    RestTemplate restTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      UserResponse userResponse =  restTemplate.getForObject("http://localhost:8080/user-service/fetch/user/"+username, UserResponse.class);

      UserDetails userDetails = User.builder().username(userResponse.getUsername()).password(userResponse.getPassword()).build();

      return userDetails;

    }
}
