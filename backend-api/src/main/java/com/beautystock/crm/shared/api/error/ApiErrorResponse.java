package com.beautystock.crm.shared.api.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.NumberFormat;

import java.time.Instant;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiErrorResponse(
        @NotNull
        Instant timestamp,
        @NotNull
        @NumberFormat
        Long status,
        @NotNull
        String code,
        @NotNull
        String message,
        @NotNull
        String path,
        @Nullable
        String correlationId,
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        @Nullable
        List<ApiFieldViolation> violations
) {

    public ApiErrorResponse(
            Instant timestamp,
            Long status,
            String code,
            String message,
            String path,
            String correlationId,
            List<ApiFieldViolation> violations
    ) {
        this.timestamp = timestamp;
        this.status = status;
        this.code = code;
        this.message = message;
        this.path = path;
        this.correlationId = correlationId;
        if (violations != null) {
            this.violations = List.copyOf(violations);
        } else this.violations = null;
    }

    public ApiErrorResponse(
            Instant timestamp,
            Long status,
            String code,
            String message,
            String path
    ) {
        this(
                timestamp,
                status,
                code,
                message,
                path,
                null,
                null
        );
    }
}
