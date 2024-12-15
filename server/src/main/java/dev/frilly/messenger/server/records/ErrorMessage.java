package dev.frilly.messenger.server.records;

import lombok.Data;

/**
 * Data class for an error message.
 */
@Data
public class ErrorMessage {

  private final int    status;
  private final String statusMessage;
  private final String message;

}
