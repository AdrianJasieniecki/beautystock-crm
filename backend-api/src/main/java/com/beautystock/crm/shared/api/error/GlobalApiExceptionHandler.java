package com.beautystock.crm.shared.api.error;

import com.beautystock.crm.shared.application.exception.ResourceConflictException;
import com.beautystock.crm.shared.application.exception.ResourceNotFoundException;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalApiExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String MALFORMED_REQUEST_CODE = "MALFORMED_REQUEST";
    private static final String MALFORMED_JSON_MESSAGE =
            "Request body contains malformed or unreadable JSON";

    private static final String INTERNAL_ERROR_CODE =
            "INTERNAL_ERROR";
    private static final String INTERNAL_SERVER_ERROR_MESSAGE =
            "An unexpected error occurred";

    private static final String RESOURCE_NOT_FOUND_ERROR_CODE =
            "RESOURCE_NOT_FOUND";
    private static final String RESOURCE_NOT_FOUND_MESSAGE =
            "Requested resource could not be found";

    private static final String RESOURCE_CONFLICT_ERROR_CODE =
            "CONFLICT";

    private static final String VALIDATION_ERROR_CODE = "VALIDATION_ERROR";
    private static final String VALIDATION_ERROR_MESSAGE = "Request validation failed";

    private static final String FIELD_REQUIRED_ERROR_CODE = "FIELD_REQUIRED";
    private static final String FIELD_REQUIRED_ERROR_MESSAGE = "Field is required";
    private static final String INVALID_EMAIL_ERROR_CODE = "INVALID_EMAIL";
    private static final String INVALID_EMAIL_ERROR_MESSAGE = "Invalid email format";
    private static final String INVALID_SIZE_ERROR_CODE = "INVALID_SIZE";
    private static final String INVALID_SIZE_ERROR_MESSAGE = "Invalid field size";
    private static final String OUT_OF_RANGE_ERROR_CODE = "OUT_OF_RANGE";
    private static final String OUT_OF_RANGE_ERROR_MESSAGE = "Value is out of range";
    private static final String INVALID_VALUE_ERROR_CODE = "INVALID_VALUE";
    private static final String INVALID_VALUE_ERROR_MESSAGE = "Invalid field value";

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception exception,
            Object body,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        if (status.is5xxServerError()) {
            logUnexpectedException(exception, request);
            ApiErrorResponse responseBody = createErrorResponse(
                    status,
                    INTERNAL_ERROR_CODE,
                    INTERNAL_SERVER_ERROR_MESSAGE,
                    request
            );
            return new ResponseEntity<>(responseBody, headers, status);
        } else if (body instanceof ApiErrorResponse) {
            return new ResponseEntity<>(body, headers, status);
        } else {
            ApiErrorResponse responseBody = createErrorResponse(
                    status,
                    defaultCode(status),
                    defaultMessage(status),
                    request
            );
            return new ResponseEntity<>(responseBody, headers, status);
        }
    }

    @Override
    protected @Nullable ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        List<ApiFieldViolation> violations = new ArrayList<>();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            String field;
            String code;
            String message;
            if (error instanceof FieldError fieldError) {
                field = fieldError.getField();
                ErrorDetails details = mapValidationCode(fieldError.getCode());
                code = details.code;
                message = details.message;
            } else {
                field = "_global";
                code = "INVALID_REQUEST";
                message = "Invalid value";
            }
            violations.add(new ApiFieldViolation(
                    field,
                    code,
                    message
            ));
        }
        ApiErrorResponse response = createErrorResponse(
                status,
                VALIDATION_ERROR_CODE,
                VALIDATION_ERROR_MESSAGE,
                request,
                violations
        );
        return handleExceptionInternal(
                ex,
                response,
                headers,
                status,
                request
        );
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
                MALFORMED_REQUEST_CODE,
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

    @Override
    protected @Nullable ResponseEntity<Object> handleNoResourceFoundException(
            NoResourceFoundException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        ApiErrorResponse response = createErrorResponse(
                status,
                RESOURCE_NOT_FOUND_ERROR_CODE,
                RESOURCE_NOT_FOUND_MESSAGE,
                request
        );

        return handleExceptionInternal(
                ex,
                response,
                headers,
                status,
                request
        );
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<Object> handleResourceConflictException(
            ResourceConflictException exception,
            WebRequest request
    ) {
        HttpStatus status = HttpStatus.CONFLICT;

        ApiErrorResponse response = createErrorResponse(
                status,
                RESOURCE_CONFLICT_ERROR_CODE,
                exception.getMessage(),
                request
        );

        return new ResponseEntity<>(
                response,
                HttpHeaders.EMPTY,
                status
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(
            ResourceNotFoundException exception,
            WebRequest request
    ) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        ApiErrorResponse response = createErrorResponse(
                status,
                RESOURCE_NOT_FOUND_ERROR_CODE,
                exception.getMessage(),
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
        return handleExceptionInternal(exception, null, HttpHeaders.EMPTY, status, request);
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

    private ApiErrorResponse createErrorResponse(
            HttpStatusCode status,
            String code,
            String message,
            WebRequest request,
            List<ApiFieldViolation> violations
    ) {
        return new ApiErrorResponse(
                Instant.now(),
                (long) status.value(),
                code,
                message,
                requestPath(request),
                null,
                violations
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

    private ErrorDetails mapValidationCode(@Nullable String code) {
        if (code != null) {
            return switch (code) {
                case "NotBlank", "NotNull", "NotEmpty" -> new ErrorDetails(FIELD_REQUIRED_ERROR_CODE, FIELD_REQUIRED_ERROR_MESSAGE);
                case "Email" -> new ErrorDetails(INVALID_EMAIL_ERROR_CODE, INVALID_EMAIL_ERROR_MESSAGE);
                case "Size" -> new ErrorDetails(INVALID_SIZE_ERROR_CODE, INVALID_SIZE_ERROR_MESSAGE);
                case "Min", "Max", "DecimalMin", "DecimalMax", "Positive",
                     "PositiveOrZero", "Negative", "NegativeOrZero" -> new ErrorDetails(OUT_OF_RANGE_ERROR_CODE, OUT_OF_RANGE_ERROR_MESSAGE);
                default -> new ErrorDetails(INVALID_VALUE_ERROR_CODE, INVALID_VALUE_ERROR_MESSAGE);
            };
        } else {
            return new ErrorDetails(INVALID_VALUE_ERROR_CODE, INVALID_VALUE_ERROR_MESSAGE);
        }
    }

    private void logUnexpectedException(Exception exception, WebRequest request) {
        logger.error("Unexpected exception for request path " + requestPath(request), exception);
    }

    private record ErrorDetails(
            String code,
            String message
    ){}
}
