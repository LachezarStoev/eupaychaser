package com.eupaychaser.e2e;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ValidationFlowTest {

    static MockWebServer mockWebServer;

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("app.email.provider", () -> "mockwebserver");
        registry.add("app.email.provider-url", () -> mockWebServer.url("/send").toString());
    }

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    void fullFlowWorksIncludingMockedEmailProvider() throws InterruptedException {
        Map<String, Object> payload = Map.of(
                "amount", 2000,
                "dueDate", LocalDate.now().minusDays(30).toString(),
                "debtorEmail", "client@example.com",
                "country", "BG"
        );

        ResponseEntity<Map<String, Object>> calc = restTemplate.exchange(
                url("/api/calculate"),
                HttpMethod.POST,
                new HttpEntity<>(payload),
                new ParameterizedTypeReference<>() {
                }
        );
        assertThat(calc.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(calc.getBody()).containsKeys("lateDays", "interest", "fixedFee", "totalClaim");

        ResponseEntity<byte[]> pdf = restTemplate.postForEntity(url("/api/pdf"), payload, byte[].class);
        assertThat(pdf.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(pdf.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_PDF);
        assertThat(pdf.getBody()).isNotNull();
        assertThat(pdf.getBody().length).isGreaterThan(100);

        ResponseEntity<Map<String, Object>> preview = restTemplate.exchange(
                url("/api/email/preview"),
                HttpMethod.POST,
                new HttpEntity<>(payload),
                new ParameterizedTypeReference<>() {
                }
        );
        assertThat(preview.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(preview.getBody()).containsKeys("subject", "body");

        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody("{}"));

        Map<String, Object> sendPayload = Map.of(
                "debtorEmail", "client@example.com",
                "subject", preview.getBody().get("subject"),
                "body", preview.getBody().get("body")
        );

        ResponseEntity<Map<String, Object>> send = restTemplate.exchange(
                url("/api/email/send"),
                HttpMethod.POST,
                new HttpEntity<>(sendPayload),
                new ParameterizedTypeReference<>() {
                }
        );
        assertThat(send.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(send.getBody()).containsEntry("sent", true);

        RecordedRequest recordedRequest = mockWebServer.takeRequest(2, TimeUnit.SECONDS);
        assertThat(recordedRequest).isNotNull();
        assertThat(recordedRequest.getPath()).isEqualTo("/send");
        assertThat(recordedRequest.getBody().readUtf8()).contains("client@example.com");
    }
}
