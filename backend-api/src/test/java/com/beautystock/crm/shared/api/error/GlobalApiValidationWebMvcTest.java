package com.beautystock.crm.shared.api.error;

import com.beautystock.crm.shared.application.exception.ResourceConflictException;
import com.beautystock.crm.shared.application.exception.ResourceNotFoundException;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.bind.annotation.*;

@WebMvcTest
public class GlobalApiValidationWebMvcTest {

    @RestController
    @RequestMapping("/test")
    static class TestController {



    }

    private record RequestDTO(
            @NotNull
            String name,
            @Email
            String email,
            @NumberFormat
            int age
    ) { }

}
