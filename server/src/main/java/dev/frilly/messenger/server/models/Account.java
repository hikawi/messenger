package dev.frilly.messenger.server.models;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.Set;

/**
 * The user account.
 */
@Data
@Entity
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(AccessLevel.NONE)
  private long id;

  private String username;
  private String password;

  @ManyToMany(mappedBy = "members", cascade = CascadeType.ALL)
  private Set<GroupChat> groupChats;

  /**
   * Adds a group chat to the set of group chats.
   */
  public void addGroupChat(final GroupChat group) {
    groupChats.add(group);
  }

}
