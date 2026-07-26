package com.beautystock.crm.shared.api.error;

import com.beautystock.crm.shared.application.exception.ResourceConflictException;
import com.beautystock.crm.shared.application.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = GlobalApiExceptionHandlerWebMvcTest.TestController.class)
@Import({GlobalApiExceptionHandler.class,
        GlobalApiExceptionHandlerWebMvcTest.TestController.class})
class GlobalApiExceptionHandlerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnMalformedJsonError() throws Exception {
        mockMvc.perform(post("/test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name":
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("MALFORMED_REQUEST"))
                .andExpect(jsonPath("$.message")
                        .value("Request body contains malformed or unreadable JSON"))
                .andExpect(jsonPath("$.path").value("/test"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void shouldReturnResourceNotFoundException() throws Exception {
        mockMvc.perform(get("/test/notFound"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.code").value("RESOURCE_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Requested resource could not be found"))
                .andExpect(jsonPath("$.path").value("/test/notFound"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void shouldReturnResourceConflictException() throws Exception {
        mockMvc.perform(get("/test/resourceConflict"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.code").value("CONFLICT"))
                .andExpect(jsonPath("$.message").value("Resource conflict occurred"))
                .andExpect(jsonPath("$.path").value("/test/resourceConflict"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void shouldReturnIllegalStateException() throws Exception {
        mockMvc.perform(get("/test/illegalState"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.code").value("INTERNAL_ERROR"))
                .andExpect(jsonPath("$.message").value("An unexpected error occurred"))
                .andExpect(jsonPath("$.path").value("/test/illegalState"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void shouldReturnMethodNotAllowed() throws Exception {
        mockMvc.perform(post("/test/illegalState"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(header().stringValues("Allow", "GET"))
                .andExpect(header().stringValues("Content-Type", "application/json"));
    }

    @Test
    void nonExistentURLReturns404() throws Exception{
        mockMvc.perform(get("/non/Existent"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.code").value("RESOURCE_NOT_FOUND"))
                .andExpect(jsonPath("$.path").value("/non/Existent"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void badBodyTypeThrowsUnsupportedMediaType() throws Exception{
        String xmlPayload = "<user><name>John</name></user>";
        mockMvc.perform(post("/test")
                .contentType(MediaType.APPLICATION_XML)
                        .content(xmlPayload))
                .andExpect(status().isUnsupportedMediaType());
    }

    @RestController
    @RequestMapping("/test")
    static class TestController {

        @PostMapping
        RequestDTO create(@RequestBody RequestDTO request) {
            return request;
        }

        @GetMapping("/notFound")
        void getThrowsResourceNotFound() {
            throw new ResourceNotFoundException("Requested resource could not be found");
        }

        @GetMapping("/resourceConflict")
        void getThrowsResourceConflict() {
            throw new ResourceConflictException("Resource conflict occurred");
        }

        @GetMapping("/illegalState")
        void getThrowsIllegalState() {
            throw new IllegalStateException("An unexpected error occurred");
        }
    }

    record RequestDTO(
            String name
    ) { }

}