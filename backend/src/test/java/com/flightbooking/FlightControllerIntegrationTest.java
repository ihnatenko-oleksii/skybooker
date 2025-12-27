package com.flightbooking;

import com.flightbooking.dto.FlightResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FlightControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testSearchFlights_shouldReturnFlights() {
        // Given
        String url = "/api/flights/search?from=WAW&to=FCO&date=2026-02-10&passengers=1";

        // When
        ResponseEntity<List<FlightResponse>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<FlightResponse>>() {
                });

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isNotEmpty();

        FlightResponse firstFlight = response.getBody().get(0);
        assertThat(firstFlight.getFromAirport()).isEqualTo("WAW");
        assertThat(firstFlight.getToAirport()).isEqualTo("FCO");
    }

    @Test
    public void testGetFlightById_shouldReturnFlight() {
        // Given
        Long flightId = 1L;

        // When
        ResponseEntity<FlightResponse> response = restTemplate.getForEntity(
                "/api/flights/" + flightId,
                FlightResponse.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(flightId);
    }

    @Test
    public void testGetFlightById_notFound_shouldReturn404() {
        // Given
        Long nonExistentId = 99999L;

        // When
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/flights/" + nonExistentId,
                String.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
