package com.beautystock.crm.shared.api.error;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.time.Instant;
import java.util.*;

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
    public void shouldSerializeWithExpectedJsonContract() {
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
        assertThat(new HashSet<>(root.propertyNames()))
                .containsExactlyInAnyOrder(
                        "timestamp",
                        "status",
                        "code",
                        "message",
                        "path",
                        "correlationId",
                        "violations"
                );
        JsonNode status = root.get("status");
        assertThat(status.isIntegralNumber()).isTrue();
        assertThat(status.longValue()).isEqualTo(400L);
        JsonNode violations = root.get("violations");
        assertThat(violations.isArray()).isTrue();
        assertThat(violations).hasSize(1);
        JsonNode violation = violations.get(0);
        assertThat(new HashSet<>(violation.propertyNames()))
                .containsExactlyInAnyOrder(
                        "field",
                        "code",
                        "message"
                );
        assertThat(violation.get("field").stringValue()).isEqualTo("email");
        assertThat(violation.get("code").stringValue()).isEqualTo("INVALID_EMAIL");
        assertThat(violation.get("message").stringValue())
                .isEqualTo("Email address has an invalid format");
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

    @Test
    public void passingMutableListDoesNotChangeStateOfApiErrorResponseAfter() {
        ApiFieldViolation apiFieldViolation = new ApiFieldViolation(
                "email",
                "INVALID_EMAIL",
                "Email address has an invalid format"
        );
        List<ApiFieldViolation> violations = new ArrayList<>();
        violations.add(apiFieldViolation);
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                Instant.parse("2026-07-24T10:30:00Z"),
                400L,
                "VALIDATION_ERROR",
                "Request validation failed",
                "/api/customers",
                UUID.randomUUID().toString(),
                violations
        );
        assertThat(apiErrorResponse.violations().size()).isEqualTo(1);
        ApiFieldViolation apiFieldViolation2 = new ApiFieldViolation(
                "email",
                "INVALID_EMAIL",
                "Email address has an invalid format"
        );
        violations.add(apiFieldViolation2);
        assertThat(apiErrorResponse.violations().size()).isEqualTo(1);
    }
}