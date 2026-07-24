package com.beautystock.crm.shared.api;

public record Violation<T>(
        T field,
        String code,
        String message
) {
}
