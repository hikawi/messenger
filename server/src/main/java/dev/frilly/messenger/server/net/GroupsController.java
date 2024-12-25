package dev.frilly.messenger.server.net;

import dev.frilly.messenger.api.data.GroupChat;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.*;
import java.util.*;

/**
 * A controller for groups-related stuff.
 */
@UtilityClass
public final class GroupsController {

  private final Map<UUID, GroupChat> groupChats = new HashMap<>();

  /**
   * Loads saved group chats.
   */
  @SneakyThrows
  public void load() {
    final var dataFolder = new File(".data");
    final var groupsFile = new File(dataFolder, "groups.bin");
    if (!groupsFile.exists()) {
      return;
    }

    final @Cleanup var input = new DataInputStream(
        new BufferedInputStream(new FileInputStream(groupsFile)));
    final var length = input.readInt();
    for (int i = 0; i < length; i++) {
      final var group = new GroupChat();
      group.setUuid(UUID.fromString(input.readUTF()));

      final var membersCount = input.readInt();
      for (int j = 0; j < membersCount; j++) {
        final var username = input.readUTF();
        group.getMembers().add(username);
      }

      groupChats.put(group.getUuid(), group);
    }
  }

  @SneakyThrows
  public void save() {
    final var dataFolder = new File(".data");
    final var groupsFile = new File(dataFolder, "groups.bin");
    if (!groupsFile.exists()) {
      dataFolder.mkdirs();
      groupsFile.createNewFile();
    }

    final @Cleanup var output = new DataOutputStream(
        new BufferedOutputStream(new FileOutputStream(groupsFile)));
    output.writeInt(groupChats.size());

    for (final var group : groupChats.values()) {
      output.writeUTF(group.getUuid().toString());
      output.writeInt(group.getMembers().size());
      for (final var member : group.getMembers()) {
        output.writeUTF(member);
      }
    }
  }

  /**
   * Retrieves all group chats someone is in.
   *
   * @param username the username
   *
   * @return the group chats
   */
  public List<GroupChat> getGroupChatsOf(final String username) {
    return groupChats.values()
        .stream()
        .filter(g -> g.getMembers()
            .stream()
            .map(String::toLowerCase)
            .anyMatch(m -> m.equalsIgnoreCase(username)))
        .toList();
  }

  /**
   * Creates a new group chat and binds it.
   *
   * @return the group chat
   */
  public GroupChat createGroup() {
    var uuid = UUID.randomUUID();
    while (groupChats.containsKey(uuid)) {
      uuid = UUID.randomUUID();
    }

    final var group = new GroupChat();
    group.setUuid(uuid);
    groupChats.put(uuid, group);
    return group;
  }

  /**
   * Tries to get the group with the UUID provided.
   *
   * @param uuid the uuid
   *
   * @return the group chat optional
   */
  public Optional<GroupChat> getGroup(final UUID uuid) {
    return Optional.ofNullable(groupChats.get(uuid));
  }

}
