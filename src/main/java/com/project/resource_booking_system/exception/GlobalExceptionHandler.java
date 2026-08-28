package com.project.resource_booking_system.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    Logger logger = LoggerFactory.getLogger(this.getClass());


    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<ErrorMessage> handleException(Exception ex){
        logger.error("Ops! {}",ex.getMessage());

        CustomValidationException validationException = (CustomValidationException) ex;
        ErrorHandle errorHandle = validationException.getErrorCode();
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage(errorHandle.getMessage());
        errorMessage.setErrorCode(errorHandle.getErrorCode());
        return new ResponseEntity<>(errorMessage,HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleSecurityException(Exception exception, HttpServletRequest request) {
        ProblemDetail errorDetail;

        // Log exception stack trace for developers / observability
        exception.printStackTrace();

        if (exception instanceof UsernameNotFoundException) {
            errorDetail = buildError(HttpStatus.NOT_FOUND, "USERNAME_NOT_FOUND", "User Not Found", request);
        }
        else if (exception instanceof BadCredentialsException) {
            errorDetail = buildError(HttpStatus.UNAUTHORIZED, "BAD_CREDENTIALS", "Incorrect username or password", request);
        }
        else if (exception instanceof AccountStatusException) {
            errorDetail = buildError(HttpStatus.FORBIDDEN, "ACCOUNT_LOCKED", "This account is locked", request);
        }
        else if (exception instanceof AccessDeniedException) {
            errorDetail = buildError(HttpStatus.FORBIDDEN, "ACCESS_DENIED", "Not authorized to access this resource", request);
        }
        else if (exception instanceof SignatureException) {
            errorDetail = buildError(HttpStatus.FORBIDDEN, "INVALID_SIGNATURE", "JWT signature is invalid", request);
        }
        else if (exception instanceof ExpiredJwtException) {
            errorDetail = buildError(HttpStatus.FORBIDDEN, "JWT_EXPIRED", "The JWT token has expired", request);
        }
        else {
            errorDetail = buildError(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", exception.getMessage(), request);
        }

        return errorDetail;
    }

    private ProblemDetail buildError(HttpStatus status, String code, String message, HttpServletRequest request) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(status, message);
        detail.setProperty("error", code);
        detail.setProperty("timestamp", LocalDateTime.now().toString());
        detail.setProperty("path", request.getRequestURI());
        return detail;
    }
}