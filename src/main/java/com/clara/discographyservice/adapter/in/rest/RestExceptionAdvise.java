package com.clara.discographyservice.adapter.in.rest;

import com.clara.discographyservice.application.port.out.DiscogsException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class RestExceptionAdvise {

    private static final Logger logger = LoggerFactory.getLogger(RestExceptionAdvise.class);

    @ExceptionHandler(DiscogsException.class)
    public ResponseEntity<ErrorResponseDTO> handleDiscogsException(DiscogsException e) {
        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                                                         .message("Discogs API returns error")
                                                         .code(HttpStatus.BAD_GATEWAY.value())
                                                         .details(e.getMessage())
                                                         .timestamp(java.time.LocalDateTime.now())
                                                         .build();
        logger.error("Discogs API returns error. exception: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                             .body(errorResponse);
    }
    @ExceptionHandler( {
            IllegalArgumentException.class,
            ConstraintViolationException.class,
            MissingServletRequestParameterException.class})
    public ResponseEntity<ErrorResponseDTO> handleIllegalArgumentException(Exception e) {
        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                                                         .message("Bad request")
                                                         .code(HttpStatus.BAD_REQUEST.value())
                                                         .details(e.getMessage())
                                                         .timestamp(java.time.LocalDateTime.now())
                                                         .build();
        logger.error("Bad request. exception: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(errorResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                                                         .message("Bad request")
                                                         .code(HttpStatus.BAD_REQUEST.value())
                                                         .details(e.getMostSpecificCause().getMessage())
                                                         .timestamp(java.time.LocalDateTime.now())
                                                         .build();
        logger.error("Bad request. exception: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleException(Exception e) {
        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                                                         .message("An unexpected error occurred.")
                                                         .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                                         .details(e.getMessage())
                                                         .timestamp(java.time.LocalDateTime.now())
                                                         .build();
        logger.error("An unexpected error occurred. exception", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(errorResponse);
    }
}
