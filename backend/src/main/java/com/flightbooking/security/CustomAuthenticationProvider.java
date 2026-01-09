package com.flightbooking.security;

import com.flightbooking.entity.User;
import com.flightbooking.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;

/**
 * Niestandardowy AuthenticationProvider używający User.checkLoginCredentials().
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomAuthenticationProvider(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String login = authentication.getName();
        String password = authentication.getCredentials().toString();

        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new BadCredentialsException("Nieprawidłowy login lub hasło"));

        // Użycie metody checkLoginCredentials() z encji User
        if (!user.checkLoginCredentials(login, password, passwordEncoder)) {
            throw new BadCredentialsException("Nieprawidłowy login lub hasło");
        }

        // Ustawienie roli na podstawie typu użytkownika
        Set<GrantedAuthority> authorities;
        if (user instanceof com.flightbooking.entity.Administrator) {
            authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
        }

        return new UsernamePasswordAuthenticationToken(
                user.getLogin(),
                null,
                authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
