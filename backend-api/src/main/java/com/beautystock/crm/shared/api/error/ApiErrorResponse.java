package com.beautystock.crm.shared.api.error;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiErrorResponse<T>(
        Instant timestamp,
        Long status,
        String code,
        String message,
        String path,
        Long correlationId,
        List<ApiFieldViolation<T>> violations
) {
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
