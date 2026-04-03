package com.eupaychaser.e2e;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.time.LocalDate;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ValidationFlowTest {

    @RegisterExtension
    static WireMockExtension wireMock = WireMockExtension.newInstance().options(options().dynamicPort()).build();

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("app.email.provider", () -> "wiremock");
        registry.add("app.email.provider-url", () -> wireMock.baseUrl() + "/send");
    }

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    void fullFlowWorksIncludingMockedEmailProvider() {
        Map<String, Object> payload = Map.of(
                "amount", 2000,
                "dueDate", LocalDate.now().minusDays(30).toString(),
                "debtorEmail", "client@example.com",
                "country", "BG"
        );

        ResponseEntity<Map> calc = restTemplate.postForEntity(url("/api/calculate"), payload, Map.class);
        assertThat(calc.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(calc.getBody()).containsKeys("lateDays", "interest", "fixedFee", "totalClaim");

        ResponseEntity<byte[]> pdf = restTemplate.postForEntity(url("/api/pdf"), payload, byte[].class);
        assertThat(pdf.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(pdf.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_PDF);
        assertThat(pdf.getBody()).isNotNull();
        assertThat(pdf.getBody().length).isGreaterThan(100);

        ResponseEntity<Map> preview = restTemplate.postForEntity(url("/api/email/preview"), payload, Map.class);
        assertThat(preview.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(preview.getBody()).containsKeys("subject", "body");

        wireMock.stubFor(post(urlEqualTo("/send")).willReturn(aResponse().withStatus(200)));

        Map<String, Object> sendPayload = Map.of(
                "debtorEmail", "client@example.com",
                "subject", preview.getBody().get("subject"),
                "body", preview.getBody().get("body")
        );

        ResponseEntity<Map> send = restTemplate.postForEntity(url("/api/email/send"), sendPayload, Map.class);
        assertThat(send.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(send.getBody().get("sent")).isEqualTo(true);

        wireMock.verify(postRequestedFor(urlEqualTo("/send"))
                .withRequestBody(matchingJsonPath("$.to", equalTo("client@example.com"))));
    }
}
