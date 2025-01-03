package dev.frilly.messenger.api.data;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Data class for a chat message.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public final class ChatMessage extends Message {

  private String content;

}
