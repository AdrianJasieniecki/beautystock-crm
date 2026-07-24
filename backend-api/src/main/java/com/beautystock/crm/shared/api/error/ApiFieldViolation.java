package com.beautystock.crm.shared.api.error;

public record ApiFieldViolation<T>(
        T field,
        String code,
        String message
) {
}
