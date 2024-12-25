package dev.frilly.messenger.api.data;

import lombok.Data;

/**
 * A base class for a message.
 */
@Data
public sealed class Message implements Comparable<Message>
    permits ChatMessage, FileMessage {

  private String username;
  private String groupName;
  private long   timestamp;

  /**
   * Sets the timestamp to the current now.
   */
  public final void setNow() {
    timestamp = System.currentTimeMillis();
  }

  @Override
  public int compareTo(Message o) {
    return Long.compare(timestamp, o.timestamp);
  }

}
