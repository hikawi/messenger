package dev.frilly.messenger.server.controllers;

import dev.frilly.messenger.server.records.ErrorMessage;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

/**
 * Global controller's advice class.
 */
@RestControllerAdvice
public class GlobalAdvice {

  /**
   * Handles the ResponseStatusException.
   *
   * @param exception the exception
   *
   * @return the error message
   */
  @ExceptionHandler(exception = ResponseStatusException.class)
  public ErrorMessage onStatusCodeException(final ResponseStatusException exception) {
    return new ErrorMessage(exception.getStatusCode().value(),
        exception.getMessage(), exception.getReason());
  }

}
