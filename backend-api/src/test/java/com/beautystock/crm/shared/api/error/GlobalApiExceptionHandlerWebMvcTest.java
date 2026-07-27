package com.beautystock.crm.shared.api.error;

import com.beautystock.crm.shared.application.exception.ResourceConflictException;
import com.beautystock.crm.shared.application.exception.ResourceNotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(controllers = GlobalApiExceptionHandlerWebMvcTest.TestController.class)
@Import({GlobalApiExceptionHandler.class,
        GlobalApiExceptionHandlerWebMvcTest.TestController.class})
class GlobalApiExceptionHandlerWebMvcTest {

    private static final Logger log = LoggerFactory.getLogger(GlobalApiExceptionHandlerWebMvcTest.class);
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
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").isNumber())
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
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").isNumber())
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
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").isNumber())
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
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").isNumber())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.code").value("INTERNAL_ERROR"))
                .andExpect(jsonPath("$.message").value("An unexpected error occurred"))
                .andExpect(jsonPath("$.path").value("/test/illegalState"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.exception").doesNotExist())
                .andExpect(jsonPath("$.stackTrace").doesNotExist())
                .andExpect(jsonPath("$.cause").doesNotExist());
    }

    @Test
    void shouldReturnMethodNotAllowed() throws Exception {
        mockMvc.perform(post("/test/illegalState"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(header().stringValues("Allow", "GET"))
                .andExpect(header().stringValues("Content-Type", "application/json"))
                .andExpect(jsonPath("$.status").isNumber())
                .andExpect(jsonPath("$.status").value(405))
                .andExpect(jsonPath("$.code").value("METHOD_NOT_ALLOWED"))
                .andExpect(jsonPath("$.message").value("Method Not Allowed"))
                .andExpect(jsonPath("$.path").value("/test/illegalState"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.timestamp").value(Matchers.matchesRegex("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.*Z$")))
                .andExpect(jsonPath("$.correlationId").doesNotExist())
                .andExpect(jsonPath("$.violations").doesNotExist())
                .andExpect(content().string(Matchers.not(Matchers.containsString("internal-diagnostic-sentinel"))))
                .andExpect(content().string(Matchers.not(Matchers.containsString("IllegalStateException"))));
    }

    @Test
    void nonExistentURLReturns404() throws Exception{
        mockMvc.perform(get("/non/Existent"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").isNumber())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.code").value("RESOURCE_NOT_FOUND"))
                .andExpect(jsonPath("$.path").value("/non/Existent"))
                .andExpect(jsonPath("$.message").value("Requested resource could not be found"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.timestamp").value(Matchers.matchesRegex("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.*Z$")))
                .andExpect(jsonPath("$.correlationId").doesNotExist())
                .andExpect(jsonPath("$.violations").doesNotExist());
    }

    @Test
    void badBodyTypeThrowsUnsupportedMediaType() throws Exception{
        String xmlPayload = "<user><name>John</name></user>";
        mockMvc.perform(post("/test")
                .contentType(MediaType.APPLICATION_XML)
                        .content(xmlPayload))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(header().string("Accept", Matchers.containsString("application/json")))
                .andExpect(jsonPath("$.status").isNumber())
                .andExpect(jsonPath("$.status").value(415))
                .andExpect(jsonPath("$.code").value("UNSUPPORTED_MEDIA_TYPE"))
                .andExpect(jsonPath("$.message").value("Unsupported Media Type"))
                .andExpect(jsonPath("$.path").value("/test"));
    }

    @Test
    @ExtendWith(OutputCaptureExtension.class)
    void builtInErrorShouldThrow500(CapturedOutput output) throws Exception {
        mockMvc.perform(post("/test/notWritable"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.status").isNumber())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.code").value("INTERNAL_ERROR"))
                .andExpect(jsonPath("$.message").value("An unexpected error occurred"))
                .andExpect(jsonPath("$.path").value("/test/notWritable"))
                .andExpect(content().string(Matchers.not(Matchers.containsString("internal-sentinel"))));
        String logs = output.getAll();
        assertThat(logs).containsAnyOf("ERROR");
        assertThat(logs).containsAnyOf("/test/notWritable");
        assertThat(logs).containsAnyOf("GlobalApiExceptionHandler");
        assertThat(logs).containsAnyOf("HttpMessageNotWritableException");
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
            throw new IllegalStateException("internal-diagnostic-sentinel");
        }

        @PostMapping("/notWritable")
        void write() {
            throw new HttpMessageNotWritableException("framework-internal-sentinel");
        }
    }

    record RequestDTO(
            String name
    ) { }

}