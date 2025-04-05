package lv.gennadyyonov.hellookta.bff.config.rest;

import lombok.extern.slf4j.Slf4j;
import lv.gennadyyonov.hellookta.bff.config.ErrorCodes;
import lv.gennadyyonov.hellookta.exception.AccessDeniedException;
import lv.gennadyyonov.hellookta.exception.ErrorResponse;
import lv.gennadyyonov.hellookta.exception.ExternalSystemException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
    log.error("Access denied: {}", ex.getMessage(), ex);
    ErrorResponse errorResponse =
        ErrorResponse.builder().code(ErrorCodes.ACCESS_DENIED).message(ex.getMessage()).build();
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
  }

  @ExceptionHandler(ExternalSystemException.class)
  public ResponseEntity<ErrorResponse> handleExternalSystemException(ExternalSystemException ex) {
    log.error("Error occurred while calling external system: {}", ex.getMessage(), ex);
    ErrorResponse errorResponse =
        ErrorResponse.builder().code(ErrorCodes.EXTERNAL_SYSTEM).message(ex.getMessage()).build();
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }
}
