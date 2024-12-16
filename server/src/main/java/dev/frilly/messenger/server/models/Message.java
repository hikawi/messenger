package dev.frilly.messenger.server.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

/**
 * Model for a message.
 */
@Entity
@Data
public class Message {

  @EmbeddedId
  private MessageId messageId;

  @ManyToOne
  @MapsId("accountId")
  @JoinColumn(name = "account_id", nullable = false)
  private Account user;

  @ManyToOne
  @MapsId("groupId")
  @JoinColumn(name = "group_id", nullable = false)
  private GroupChat groupChat;

  @NotNull
  private String content;

  @NotNull
  @CreationTimestamp
  private Timestamp timestamp;

}
