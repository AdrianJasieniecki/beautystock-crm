package com.beautystock.crm.shared.api.error;

public record ApiFieldViolation(
        String field,
        String code,
        String message
) {
}
