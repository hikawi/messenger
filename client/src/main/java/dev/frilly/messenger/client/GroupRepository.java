package dev.frilly.messenger.client;

import dev.frilly.messenger.api.data.GroupChat;
import lombok.Setter;

import java.util.*;
import java.util.function.Consumer;

/**
 * A repository for holding group chats.
 */
public final class GroupRepository {

  private final Map<UUID, GroupChat> groupChats = new HashMap<>();

  @Setter
  private Consumer<GroupChat> addHook;

  /**
   * Receives a group chat by id.
   *
   * @param uuid the uuid
   *
   * @return the group chat
   */
  public GroupChat getGroupChat(final UUID uuid) {
    return groupChats.get(uuid);
  }

  /**
   * Appends a new group chat.
   *
   * @param chat the group chat
   */
  public void addGroupChat(final GroupChat chat) {
    groupChats.put(chat.getUuid(), chat);
    Optional.ofNullable(addHook).ifPresent(hook -> hook.accept(chat));
  }

  /**
   * Checks if the group is registered.
   *
   * @param str the string
   *
   * @return the group
   */
  public boolean hasGroup(final String str) {
    try {
      return groupChats.containsKey(UUID.fromString(str));
    } catch (IllegalArgumentException ex) {
      return false;
    }
  }

  /**
   * Retrieves a collection view of all group chats.
   *
   * @return the group chats.
   */
  public Collection<UUID> getGroups() {
    return groupChats.keySet();
  }

}
