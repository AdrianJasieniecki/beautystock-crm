package com.beautystock.crm.shared.api;

import java.time.Instant;
import java.util.List;

public record ErrorDTO<T>(
        Instant timestamp,
        Long status,
        String code,
        String message,
        String path,
        Long correlationId,
        List<Violation<T>> violations
) {
}
