package dev.frilly.messenger.server.net;

import dev.frilly.messenger.api.data.ChatMessage;
import dev.frilly.messenger.api.data.FileMessage;
import dev.frilly.messenger.api.data.Message;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.*;
import java.util.*;

/**
 * A controller for handling saving and loading messages.
 */
@UtilityClass
public final class MessagesController {

  private final Map<String, List<Message>> messages = new HashMap<>();

  /**
   * Loads all messages already sent by all clients in all groups.
   */
  @SneakyThrows
  public void load() {
    final var dataFolder = new File(".data", "messages");
    if (!dataFolder.exists()) {
      return;
    }

    final var files = dataFolder.listFiles();
    if (files == null) {
      return;
    }

    messages.clear();
    for (final var file : files) {
      final @Cleanup var input = new DataInputStream(
          new BufferedInputStream(new FileInputStream(file)));
      final var groupName = input.readUTF();

      while (input.available() > 0) {
        final var typeBit = input.readByte();
        if (typeBit == 0) {
          final var chatMsg = new ChatMessage();
          chatMsg.setUsername(input.readUTF());
          chatMsg.setGroupName(groupName);
          chatMsg.setContent(input.readUTF());
          chatMsg.setTimestamp(input.readLong());
          saveMessage(chatMsg);
        } else if (typeBit == 1) {
          final var fileMsg = new FileMessage();
          fileMsg.setUsername(input.readUTF());
          fileMsg.setFileName(input.readUTF());
          fileMsg.setFilePath(input.readUTF());
          fileMsg.setTimestamp(input.readLong());
          saveMessage(fileMsg);
        }
      }
    }

    messages.values().forEach(Collections::sort);
  }

  /**
   * Saves a message to the controller.
   *
   * @param message the message
   */
  public void saveMessage(final Message message) {
    messages.putIfAbsent(message.getGroupName(), new ArrayList<>());
    messages.get(message.getGroupName()).add(message);
  }

  /**
   * Receives a list of messages.
   *
   * @param group the group to retrieve
   *
   * @return a list of message history.
   */
  public List<Message> getHistory(final String group) {
    messages.putIfAbsent(group, new ArrayList<>());
    return messages.get(group).stream().sorted().toList();
  }

  /**
   * Deletes a message.
   *
   * @param message the message
   */
  public void deleteMessage(final Message message) {
    final var msgs = messages.get(message.getGroupName());
    if (msgs == null) {
      return;
    }

    msgs.removeIf(node -> node.baseEquals(message));
  }

  /**
   * Saves all messages received by the server.
   */
  @SneakyThrows
  public void save() {
    final var dataFolder = new File(".data", "messages");
    dataFolder.mkdirs();

    for (final var entry : messages.entrySet()) {
      if (entry.getValue().isEmpty()) {
        continue;
      }

      final var file = new File(dataFolder, entry.getKey() + ".bin");
      file.createNewFile();

      final @Cleanup var output = new DataOutputStream(
          new BufferedOutputStream(new FileOutputStream(file)));
      output.writeUTF(entry.getKey());

      for (final var message : entry.getValue()) {
        if (message instanceof ChatMessage chatMsg) {
          output.writeByte(0);
          output.writeUTF(chatMsg.getUsername());
          output.writeUTF(chatMsg.getContent());
          output.writeLong(chatMsg.getTimestamp());
        } else if (message instanceof FileMessage fileMsg) {
          output.writeByte(1);
          output.writeUTF(fileMsg.getUsername());
          output.writeUTF(fileMsg.getFileName());
          output.writeUTF(fileMsg.getFilePath());
          output.writeLong(fileMsg.getTimestamp());
        }
      }

      output.flush();
    }
  }

}
