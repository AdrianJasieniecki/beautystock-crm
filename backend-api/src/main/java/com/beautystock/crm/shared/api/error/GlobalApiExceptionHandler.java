package com.beautystock.crm.shared.api.error;

import com.beautystock.crm.shared.application.exception.ResourceConflictException;
import com.beautystock.crm.shared.application.exception.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;

@RestControllerAdvice
public class GlobalApiExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String MALFORMED_JSON_CODE = "MALFORMED_JSON";
    private static final String MALFORMED_JSON_MESSAGE =
            "Request body contains malformed or unreadable JSON";

    private static final String INTERNAL_SERVER_ERROR_CODE =
            "INTERNAL_SERVER_ERROR";
    private static final String INTERNAL_SERVER_ERROR_MESSAGE =
            "An unexpected error occurred";

    private static final String RESOURCE_NOT_FOUND_ERROR_CODE =
            "RESOURCE_NOT_FOUND";
    private static final String RESOURCE_NOT_FOUND_ERROR_MESSAGE =
            "Requested resource could not be found";

    private static final String RESOURCE_CONFLICT_ERROR_CODE =
            "RESOURCE_CONFLICT";
    private static final String RESOURCE_CONFLICT_ERROR_MESSAGE =
            "Resource conflict occurred";

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception exception,
            Object body,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        Object responseBody = body instanceof ApiErrorResponse
                ? body
                : createErrorResponse(
                status,
                defaultCode(status),
                defaultMessage(status),
                request
        );

        return new ResponseEntity<>(responseBody, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        ApiErrorResponse response = createErrorResponse(
                status,
                MALFORMED_JSON_CODE,
                MALFORMED_JSON_MESSAGE,
                request
        );

        return handleExceptionInternal(
                exception,
                response,
                headers,
                status,
                request
        );
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<Object> handleResourceConflictException(
            Exception exception,
            WebRequest request
    ) {
        HttpStatus status = HttpStatus.CONFLICT;

        ApiErrorResponse response = createErrorResponse(
                status,
                RESOURCE_CONFLICT_ERROR_CODE,
                RESOURCE_CONFLICT_ERROR_MESSAGE,
                request
        );

        return new ResponseEntity<>(
                response,
                HttpHeaders.EMPTY,
                status
        );
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<Object> handleResourceNotFoundException(
            Exception exception,
            WebRequest request
    ) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        ApiErrorResponse response = createErrorResponse(
                status,
                RESOURCE_NOT_FOUND_ERROR_CODE,
                RESOURCE_NOT_FOUND_ERROR_MESSAGE,
                request
        );

        return new ResponseEntity<>(
                response,
                HttpHeaders.EMPTY,
                status
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUnexpectedException(
            Exception exception,
            WebRequest request
    ) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        ApiErrorResponse response = createErrorResponse(
                status,
                INTERNAL_SERVER_ERROR_CODE,
                INTERNAL_SERVER_ERROR_MESSAGE,
                request
        );

        return new ResponseEntity<>(
                response,
                HttpHeaders.EMPTY,
                status
        );
    }

    private ApiErrorResponse createErrorResponse(
            HttpStatusCode status,
            String code,
            String message,
            WebRequest request
    ) {
        return new ApiErrorResponse(
                Instant.now(),
                (long) status.value(),
                code,
                message,
                requestPath(request),
                null,
                null
        );
    }

    private String defaultCode(HttpStatusCode status) {
        HttpStatus resolvedStatus = HttpStatus.resolve(status.value());

        return resolvedStatus != null
                ? resolvedStatus.name()
                : "HTTP_" + status.value();
    }

    private String defaultMessage(HttpStatusCode status) {
        HttpStatus resolvedStatus = HttpStatus.resolve(status.value());

        return resolvedStatus != null
                ? resolvedStatus.getReasonPhrase()
                : "Request could not be processed";
    }

    private String requestPath(WebRequest request) {
        if (request instanceof ServletWebRequest servletWebRequest) {
            return servletWebRequest.getRequest().getRequestURI();
        }

        return "";
    }
}