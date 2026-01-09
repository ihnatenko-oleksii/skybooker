package com.flightbooking.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import com.flightbooking.entity.User;
import com.flightbooking.repository.UserRepository;

/**
 * Klasa pomocnicza do operacji związanych z bezpieczeństwem.
 */
public class SecurityUtils {

    /**
     * Pobiera ID aktualnie zalogowanego użytkownika z SecurityContext.
     */
    public static Long getCurrentUserId(UserRepository userRepository) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            String login = ((UserDetails) principal).getUsername();
            User user = userRepository.findByLogin(login).orElse(null);
            return user != null ? user.getId() : null;
        }

        return null;
    }

    /**
     * Pobiera aktualnie zalogowanego użytkownika z SecurityContext.
     */
    public static User getCurrentUser(UserRepository userRepository) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            String login = ((UserDetails) principal).getUsername();
            return userRepository.findByLogin(login).orElse(null);
        }

        return null;
    }
}
