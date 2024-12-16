package dev.frilly.messenger.server.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

/**
 * Represents a simple group chat.
 */
@Data
public final class GroupChat {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(
      joinColumns = @JoinColumn(name = "group_id"),
      inverseJoinColumns = @JoinColumn(name = "account_id")
  )
  private Set<Account> members;

  /**
   * Adds a member to this group chat.
   *
   * @param account the account
   */
  public void addMember(final Account account) {
    members.add(account);
  }

  /**
   * Removes a member from this group chat.
   *
   * @param account the account
   */
  public void removeMember(final Account account) {
    members.remove(account);
  }

}
