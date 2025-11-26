package com.example.demo.service.impl;

import com.example.demo.model.Person;
import com.example.demo.repository.PersonRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final PersonRepository repo;

    public CustomUserDetailsService(PersonRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person p = repo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        var authorities = p.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        return User.withUsername(p.getUsername()).password(p.getPassword()).authorities(authorities).build();
    }
}
