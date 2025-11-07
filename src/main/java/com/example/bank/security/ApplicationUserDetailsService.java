package com.example.bank.security;

import com.example.bank.repository.ApplicationUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationUserDetailsService implements UserDetailsService {

    private final ApplicationUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // в качестве username используем email
        var user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        var authority = new SimpleGrantedAuthority(user.getRole().name());

        return new User(
                user.getEmail(),
                user.getPassword(),
                !user.isDisabled(),
                !user.isAccountExpired(),
                !user.isCredentialsExpired(),
                !user.isAccountLocked(),
                java.util.List.of(authority)
        );
    }
}
