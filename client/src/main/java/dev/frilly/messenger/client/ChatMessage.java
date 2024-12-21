package dev.frilly.messenger.client;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * Data class for a chat message.
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ChatMessage {

  @EqualsAndHashCode.Include
  private long   id;
  private String username;
  private String content;
  private Date   timestamp;

}
