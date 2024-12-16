package dev.frilly.messenger.server.models;

import lombok.Data;

import java.io.Serializable;

/**
 * Composite ID class for {@link Message}.
 */
@Data
public class MessageId implements Serializable {

  private long accountId;
  private long groupId;

}
