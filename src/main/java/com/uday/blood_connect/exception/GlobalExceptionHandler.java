package com.uday.blood_connect.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {

        ErrorResponse errorResponse = new ErrorResponse(
                "NOT_FOUND",
                ex.getMessage()
        );
        return ResponseEntity.status(404).body(errorResponse);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(UserAlreadyExistsException ex) {

        ErrorResponse errorResponse = new ErrorResponse(
                "CONFLICT",
                ex.getMessage()
        );
        return ResponseEntity.status(409).body(errorResponse);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleExpired(ExpiredJwtException ex) {

        ErrorResponse errorResponse = new ErrorResponse(
                "EXPIRED_TOKEN",
                "Session expired. Please log in again."
        );
        return ResponseEntity.status(401).body(errorResponse);
    }

    @ExceptionHandler({SignatureException.class, MalformedJwtException.class})
    public ResponseEntity<ErrorResponse> handleInvalidToken(JwtException ex) {

        ErrorResponse errorResponse = new ErrorResponse(
                "INVALID_TOKEN",
                "Invalid or corrupted token."
        );
        return ResponseEntity.status(401).body(errorResponse);
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    public ResponseEntity<ErrorResponse> handleUnsupported(UnsupportedJwtException ex) {

        ErrorResponse errorResponse = new ErrorResponse(
                "UNSUPPORTED_TOKEN",
                "Unsupported token format."
        );
        return ResponseEntity.status(401).body(errorResponse);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {

        ErrorResponse errorResponse = new ErrorResponse(
                "BAD_CREDENTIALS",
                ex.getMessage()
        );
        return ResponseEntity.status(401).body(errorResponse);
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientAuthentication(InsufficientAuthenticationException ex) {

        ErrorResponse errorResponse = new ErrorResponse(
                "AUTHENTICATION_REQUIRED",
                "Authentication is required to access this resource."
        );
        return ResponseEntity.status(401).body(errorResponse);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthentication(AuthenticationException ex) {

        ErrorResponse errorResponse = new ErrorResponse(
                "UNAUTHORIZED",
                ex.getMessage()
        );
        return ResponseEntity.status(401).body(errorResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {

        ErrorResponse errorResponse = new ErrorResponse(
                "FORBIDDEN",
                "You do not have permission to access this resource."
        );
        return ResponseEntity.status(403).body(errorResponse);
    }

    @ExceptionHandler(UnauthorizedActionExcepition.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedAction(UnauthorizedActionExcepition ex) {

        ErrorResponse errorResponse = new ErrorResponse(
                "UNAUTHORIZED",
                ex.getMessage()
        );
        return ResponseEntity.status(401).body(errorResponse);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRoleNotFound(RoleNotFoundException ex) {

        ErrorResponse errorResponse = new ErrorResponse(
                "ROLE_NOT_FOUND",
                ex.getMessage()
        );
        return ResponseEntity.status(500).body(errorResponse);
    }

    @ExceptionHandler(RequestAlreadyFulFilledException.class)
    public ResponseEntity<ErrorResponse> handleRequestAlreadyFulFilled(RequestAlreadyFulFilledException ex) {

        ErrorResponse errorResponse = new ErrorResponse(
                "BAD_REQUEST",
                ex.getMessage()
        );
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(InvalidEnumException.class)
    public ResponseEntity<ErrorResponse> handleInvalidEnum(InvalidEnumException ex) {

        ErrorResponse errorResponse = new ErrorResponse(
                "BAD_REQUEST",
                ex.getMessage()
        );
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {

        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> fieldErrors.put(error.getField(), error.getDefaultMessage()));

        ErrorResponse errorResponse = new ErrorResponse(
                "VALIDATION_FAILED",
                fieldErrors
        );
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {

        ErrorResponse errorResponse = new ErrorResponse(
                "BAD_REQUEST",
                ex.getMessage()
        );
        return ResponseEntity.badRequest().body(errorResponse);
    }
}
