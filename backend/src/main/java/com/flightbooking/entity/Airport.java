package com.flightbooking.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "airports")
public class Airport {

    @Id
    @Column(name = "code", length = 3)
    private String code; // Kod IATA (np. WAW, FCO)

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, length = 100)
    private String city;

    @Column(nullable = false, length = 100)
    private String country;


    public Airport() {
    }

    public Airport(String code, String name, String city, String country) {
        this.code = code;
        this.name = name;
        this.city = city;
        this.country = country;
    }

    /**
     * Aktualizuje dane lotniska.
     * UML: Lotnisko.zaktualizujDane(noweDane: Lotnisko)
     */
    public void updateData(Airport newData) {
        if (newData == null) {
            return;
        }
        // Kod jest kluczem głównym, nie aktualizujemy go
        if (newData.getName() != null) {
            this.name = newData.getName();
        }
        if (newData.getCity() != null) {
            this.city = newData.getCity();
        }
        if (newData.getCountry() != null) {
            this.country = newData.getCountry();
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
