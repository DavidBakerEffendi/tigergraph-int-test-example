package io.github.davidbakereffendi.example;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TigerAppIntTest {
    @Test
    void appHasAGreetingTest() {
        var client = HttpClient.newHttpClient();

        var request = HttpRequest.newBuilder(URI.create("https://httpbin.org/get"))
                .header("accept", "application/json")
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // the response:
            System.out.println(response.body());
            assertNotNull(response.body());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void helloTest() {
        System.out.println("Hello");
    }
}
