package com.flightbooking.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false, unique = true, length = 200)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(nullable = false, unique = true, length = 100)
    private String login;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    public User() {
    }

    public User(Long id, String firstName, String lastName, String email, String phone, String login,
            String passwordHash) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.login = login;
        this.passwordHash = passwordHash;
    }

    /**
     * Sprawdza dane logowania.
     * UML: UżytkownikSystemu.sprawdzDaneLogowania(login: string, haslo: string)
     * Uwaga: Ta metoda porównuje podane hasło z zapisanym hashem.
     * PasswordEncoder powinien być dostarczony przez warstwę serwisową.
     */
    public boolean checkLoginCredentials(String providedLogin, String providedPassword, 
                                         org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
        if (providedLogin == null || providedPassword == null || passwordEncoder == null) {
            return false;
        }
        // Sprawdzenie czy login się zgadza
        if (!this.login.equals(providedLogin)) {
            return false;
        }
        // Sprawdzenie czy hasło się zgadza (używając PasswordEncoder)
        return passwordEncoder.matches(providedPassword, this.passwordHash);
    }

    // Gettery i Settery
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
