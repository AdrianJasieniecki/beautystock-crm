package com.beautystock.crm.shared.api.error;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(controllers = GlobalApiValidationWebMvcTest.TestController.class)
@Import({GlobalApiExceptionHandler.class,
        GlobalApiValidationWebMvcTest.TestController.class})
public class GlobalApiValidationWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void correctRequestDoesNotThrowValidationExceptions() throws Exception {
        mockMvc.perform(post("/test/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "name": "Anna Kowalska",
                          "email": "anna@example.com",
                          "age": 28
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Anna Kowalska"))
                .andExpect(jsonPath("$.email").value("anna@example.com"))
                .andExpect(jsonPath("$.age").value(28));
    }

    @Test
    void malformedRequestThrowsValidationExceptionWhenNull() throws Exception {
        MvcResult result = mockMvc.perform(post("/test/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "name": null,
                          "email": "anna@example.com",
                          "age": 28
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Request validation failed"))
                .andExpect(jsonPath("$.path").value("/test/create"))
                .andExpect(jsonPath("$.violations").isArray())
                .andExpect(jsonPath("$.violations", hasSize(1)))
                .andExpect(jsonPath("$.violations[0].field").value("name"))
                .andExpect(jsonPath("$.violations[0].code").value("FIELD_REQUIRED"))
                .andExpect(jsonPath("$.correlationId").doesNotExist())
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        JsonNode root = objectMapper.readTree(responseBody);
        String timestamp = root.get("timestamp").asString();
        Instant parsedTimestamp = Instant.parse(timestamp);
        assertThat(timestamp).endsWith("Z");
        assertThat(parsedTimestamp).isNotNull();
    }

    @Test
    void malformedRequestThrowsValidationExceptionWhenEmptyString() throws Exception {
        MvcResult result = mockMvc.perform(post("/test/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "name": "",
                          "email": "anna@example.com",
                          "age": 28
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Request validation failed"))
                .andExpect(jsonPath("$.path").value("/test/create"))
                .andExpect(jsonPath("$.violations").isArray())
                .andExpect(jsonPath("$.violations", hasSize(1)))
                .andExpect(jsonPath("$.violations[0].field").value("name"))
                .andExpect(jsonPath("$.violations[0].code").value("FIELD_REQUIRED"))
                .andExpect(jsonPath("$.correlationId").doesNotExist())
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        JsonNode root = objectMapper.readTree(responseBody);
        String timestamp = root.get("timestamp").asString();
        Instant parsedTimestamp = Instant.parse(timestamp);
        assertThat(timestamp).endsWith("Z");
        assertThat(parsedTimestamp).isNotNull();
    }

    @Test
    void malformedRequestThrowsValidationExceptionWhenBlank() throws Exception {
        MvcResult result = mockMvc.perform(post("/test/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "name": "   ",
                          "email": "anna@example.com",
                          "age": 28
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Request validation failed"))
                .andExpect(jsonPath("$.path").value("/test/create"))
                .andExpect(jsonPath("$.violations").isArray())
                .andExpect(jsonPath("$.violations", hasSize(1)))
                .andExpect(jsonPath("$.violations[0].field").value("name"))
                .andExpect(jsonPath("$.violations[0].code").value("FIELD_REQUIRED"))
                .andExpect(jsonPath("$.correlationId").doesNotExist())
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        JsonNode root = objectMapper.readTree(responseBody);
        String timestamp = root.get("timestamp").asString();
        Instant parsedTimestamp = Instant.parse(timestamp);
        assertThat(timestamp).endsWith("Z");
        assertThat(parsedTimestamp).isNotNull();
    }

    @Test
    void malformedRequestThrowsValidationExceptionWhenEmailHasWrongFormat() throws Exception {
        MvcResult result = mockMvc.perform(post("/test/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "name": "Anna Kowalska",
                          "email": "not-an-email",
                          "age": 28
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Request validation failed"))
                .andExpect(jsonPath("$.path").value("/test/create"))
                .andExpect(jsonPath("$.violations").isArray())
                .andExpect(jsonPath("$.violations", hasSize(1)))
                .andExpect(jsonPath("$.violations[0].field").value("email"))
                .andExpect(jsonPath("$.violations[0].code").value("INVALID_EMAIL"))
                .andExpect(jsonPath("$.correlationId").doesNotExist())
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        JsonNode root = objectMapper.readTree(responseBody);
        String timestamp = root.get("timestamp").asString();
        Instant parsedTimestamp = Instant.parse(timestamp);
        assertThat(timestamp).endsWith("Z");
        assertThat(parsedTimestamp).isNotNull();
    }

    @Test
    void malformedRequestThrowsValidationExceptionWhenValueIsOutOfRange() throws Exception {
        MvcResult result = mockMvc.perform(post("/test/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "name": "Anna Kowalska",
                          "email": "email@email.com",
                          "age": 15
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Request validation failed"))
                .andExpect(jsonPath("$.path").value("/test/create"))
                .andExpect(jsonPath("$.violations").isArray())
                .andExpect(jsonPath("$.violations", hasSize(1)))
                .andExpect(jsonPath("$.violations[0].field").value("age"))
                .andExpect(jsonPath("$.violations[0].code").value("OUT_OF_RANGE"))
                .andExpect(jsonPath("$.correlationId").doesNotExist())
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        JsonNode root = objectMapper.readTree(responseBody);
        String timestamp = root.get("timestamp").asString();
        Instant parsedTimestamp = Instant.parse(timestamp);
        assertThat(timestamp).endsWith("Z");
        assertThat(parsedTimestamp).isNotNull();
    }

    @Test
    void malformedRequestThrowsValidationExceptionWithMultipleFieldViolations() throws Exception {
        MvcResult result = mockMvc.perform(post("/test/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "name": "",
                          "email": "anna-not-email",
                          "age": 15
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Request validation failed"))
                .andExpect(jsonPath("$.path").value("/test/create"))
                .andExpect(jsonPath("$.violations").isArray())
                .andExpect(jsonPath("$.violations", hasSize(3)))
                .andExpect(jsonPath("$.violations[*].field")
                        .value(containsInAnyOrder("name", "email", "age")))
                .andExpect(jsonPath("$.violations[*].code")
                        .value(containsInAnyOrder("FIELD_REQUIRED", "INVALID_EMAIL", "OUT_OF_RANGE")))
                .andExpect(jsonPath("$.correlationId").doesNotExist())
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        JsonNode root = objectMapper.readTree(responseBody);
        String timestamp = root.get("timestamp").asString();
        Instant parsedTimestamp = Instant.parse(timestamp);
        assertThat(timestamp).endsWith("Z");
        assertThat(parsedTimestamp).isNotNull();
    }

    @RestController
    @RequestMapping("/test")
    static class TestController {

        @PostMapping("/create")
        RequestDTO create(@Valid @RequestBody RequestDTO requestDTO) {
            return requestDTO;
        }

    }

    private record RequestDTO(
            @NotBlank
            String name,
            @NotNull
            @Email
            String email,
            @NotNull
            @Min(18)
            @Max(120)
            Integer age
    ) { }

}
