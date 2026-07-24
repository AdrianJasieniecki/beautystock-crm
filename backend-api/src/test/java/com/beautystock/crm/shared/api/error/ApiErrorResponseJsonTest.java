package com.beautystock.crm.shared.api.error;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ApiErrorResponseJsonTest {

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void whenDeserializeFromJsonWithAllFields_thenCorrect() {
        ApiErrorResponse response = objectMapper.readValue(
                new File("src/test/resources/api-error-response.json"),
                new TypeReference<>() {
                }
        );
        assertThat(response.timestamp()).isInstanceOf(Instant.class);
        assertThat(response.timestamp()).isEqualTo(Instant.parse("2026-07-24T10:30:00Z"));
        assertThat(response.status()).isInstanceOf(Long.class);
        assertThat(response.status()).isEqualTo(400L);
        assertThat(response.code()).isEqualTo("VALIDATION_ERROR");
        assertThat(response.message()).isEqualTo("Request validation failed");
        assertThat(response.path()).isEqualTo("/api/customers");
        assertThat(response.correlationId()).isEqualTo("d541492a-5315-4be8-bc4b-8c6b07bfc3c7");
        assertThat(response.violations()).isNotNull();
        assertThat(response.violations().getFirst()).isInstanceOf(ApiFieldViolation.class);
        assertThat(response.violations().get(1)).isInstanceOf(ApiFieldViolation.class);
        ApiFieldViolation firstViolation = response.violations().getFirst();
        ApiFieldViolation secondViolation = response.violations().getLast();
        assertThat(firstViolation.field()).isEqualTo("email");
        assertThat(firstViolation.code()).isEqualTo("INVALID_EMAIL");
        assertThat(firstViolation.message()).isEqualTo("Email address has an invalid format");
        assertThat(secondViolation.field()).isEqualTo("name");
        assertThat(secondViolation.code()).isEqualTo("FIELD_REQUIRED");
        assertThat(secondViolation.message()).isEqualTo("Name is required");
    }

    @Test
    public void whenDeserializeFromJsonWithoutOptionalFields_thenCorrect() {
        ApiErrorResponse response = objectMapper.readValue(
                new File("src/test/resources/api-error-response-without-optional-fields.json"),
                new TypeReference<>() {
                }
        );
        assertThat(response.timestamp()).isInstanceOf(Instant.class);
        assertThat(response.timestamp()).isEqualTo(Instant.parse("2026-07-24T10:30:00Z"));
        assertThat(response.status()).isInstanceOf(Long.class);
        assertThat(response.status()).isEqualTo(400L);
        assertThat(response.code()).isEqualTo("VALIDATION_ERROR");
        assertThat(response.message()).isEqualTo("Request validation failed");
        assertThat(response.path()).isEqualTo("/api/customers");
    }

    @Test
    public void shouldSerializeTimestampAsIso8601Utc() {
        ApiErrorResponse response = new ApiErrorResponse(
                Instant.parse("2026-07-24T10:30:00Z"),
                400L,
                "VALIDATION_ERROR",
                "Request validation failed",
                "/api/customers",
                UUID.randomUUID().toString(),
                List.of()
        );
        String json = objectMapper.writeValueAsString(response);
        JsonNode root = objectMapper.readTree(json);
        String timestamp = root.get("timestamp").asString();
        Instant parsedTimestamp = Instant.parse(timestamp);
        assertThat(timestamp).endsWith("Z");
        assertThat(parsedTimestamp).isEqualTo(response.timestamp());
    }

    @Test
    public void shouldSerializeWithExactPropertyNames() {
        ApiFieldViolation apiFieldViolation = new ApiFieldViolation(
                "email",
                "INVALID_EMAIL",
                "Email address has an invalid format"
        );
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                Instant.parse("2026-07-24T10:30:00Z"),
                400L,
                "VALIDATION_ERROR",
                "Request validation failed",
                "/api/customers",
                UUID.randomUUID().toString(),
                List.of(apiFieldViolation)
        );
        JsonNode root = objectMapper.readTree(
                objectMapper.writeValueAsString(apiErrorResponse)
        );
        Set<String> actualPropertyNames = new HashSet<>(root.propertyNames());
        assertThat(actualPropertyNames)
                .containsExactlyInAnyOrder(
                        "timestamp",
                        "status",
                        "code",
                        "message",
                        "path",
                        "correlationId",
                        "violations"
                );
    }

    @Test
    public void shouldNotSerializeNullFields() {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                Instant.parse("2026-07-24T10:30:00Z"),
                400L,
                "VALIDATION_ERROR",
                "Request validation failed",
                "/api/customers"
        );
        JsonNode root = objectMapper.readTree(
                objectMapper.writeValueAsString(apiErrorResponse)
        );
        Set<String> actualPropertyNames = new HashSet<>(root.propertyNames());
        assertThat(actualPropertyNames)
                .containsExactlyInAnyOrder(
                        "timestamp",
                        "status",
                        "code",
                        "message",
                        "path"
                );
        assertThat(actualPropertyNames)
                .doesNotContain(
                        "correlationId",
                        "violations"
                );
    }

    @Test
    public void shouldNotSerializeEmptyViolationsList() {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                Instant.parse("2026-07-24T10:30:00Z"),
                400L,
                "VALIDATION_ERROR",
                "Request validation failed",
                "/api/customers",
                UUID.randomUUID().toString(),
                List.of()
        );
        JsonNode root = objectMapper.readTree(
                objectMapper.writeValueAsString(apiErrorResponse)
        );
        Set<String> actualPropertyNames = new HashSet<>(root.propertyNames());
        assertThat(actualPropertyNames)
                .containsExactlyInAnyOrder(
                        "timestamp",
                        "status",
                        "code",
                        "message",
                        "path",
                        "correlationId"
                );
        assertThat(actualPropertyNames)
                .doesNotContain(
                        "violations"
                );
    }
}