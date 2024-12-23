package dev.frilly.messenger.api.data;

import lombok.Data;

/**
 * Data class for a chat message.
 */
@Data
public final class ChatMessage {

  private String username;
  private String content;
  private String groupName;

}
