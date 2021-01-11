package io.github.davidbakereffendi.example;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TigerAppIntTest {

    private static final String DB_ENDPOINT = "http://localhost:9000";

    @BeforeAll
    static void setUpAll() {
        var client = HttpClient.newHttpClient();
        var payload = TigerAppIntTest.class.getClassLoader().getResource("data/payload.json");

        HttpRequest request;
        HttpResponse<String> response;
        try {
            request = HttpRequest.newBuilder(URI.create(DB_ENDPOINT + "/graph/social"))
                    .header("accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofFile(Path.of(payload.getPath())))
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // Test the response
            assertNotNull(response.body());
            assertTrue(response.body().contains("\"results\":[{\"accepted_vertices\":2,\"accepted_edges\":1}]"));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @AfterAll
    static void tearDownAll() {
        var client = HttpClient.newHttpClient();

        HttpRequest request;
        try {
            request = HttpRequest.newBuilder(URI.create(DB_ENDPOINT + "/graph/social/vertices/person"))
                    .header("accept", "application/json")
                    .DELETE()
                    .build();
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    void helloQueryTest() {
        var client = HttpClient.newHttpClient();

        HttpRequest request;
        HttpResponse<String> response;
        try {
            request = HttpRequest.newBuilder(URI.create(DB_ENDPOINT + "/query/hello?p=Dave"))
                    .header("accept", "application/json")
                    .GET()
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // the response:
            assertNotNull(response.body());
            assertTrue(response.body().contains("\"results\":[{\"Result\":[{\"v_id\":\"Fred\",\"v_type\":\"person\",\"attributes\":{\"name\":\"Fred\",\"age\":63,\"gender\":\"Male\"}}]}]"));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
