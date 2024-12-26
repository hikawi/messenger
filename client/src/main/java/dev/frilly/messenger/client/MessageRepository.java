package dev.frilly.messenger.client;

import dev.frilly.messenger.api.data.ChatMessage;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.util.*;
import java.util.function.Consumer;

/**
 * A repository for handling loading/reading messages.
 */
public final class MessageRepository {

  private final Map<String, List<ChatMessage>> messages = new HashMap<>();

  @Getter
  private String currentGroup = "public";

  @Setter
  private Consumer<String> onGroupSwap;

  @Getter
  @Setter
  private Consumer<ChatMessage> onAddConsumer;

  /**
   * Sets the new current group.
   *
   * @param group the group
   */
  public void setCurrentGroup(final String group) {
    currentGroup = group;
    Optional.ofNullable(onGroupSwap).ifPresent(hook -> hook.accept(group));
  }

  /**
   * Sends a message via the REST socket and returns the instance if successful.
   *
   * @param content the content
   *
   * @return the optional wrapper of a chat message.
   */
  public void sendMessage(final String content) {
    final var msg = new ChatMessage();
    msg.setUsername(AppContext.getUsername());
    msg.setContent(content);
    msg.setGroupName(getCurrentGroup());

    final var socket = AppContext.getRestHandler();
    final var res = socket.query(
        "sendmessage %s %s %s".formatted(msg.getUsername(), msg.getGroupName(),
            msg.getContent()));

    final var code = res.split(" ")[0];
    if (!code.equals("201")) {
      final var frame = AppContext.getFrame();
      JOptionPane.showMessageDialog(frame.getFrame(),
          "There was an error sending the message", "Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Adds a message to the repository.
   *
   * @param msg the message
   */
  public void addMessage(final ChatMessage msg) {
    messages.putIfAbsent(msg.getGroupName(), new ArrayList<>());
    messages.get(msg.getGroupName()).add(msg);

    if (onAddConsumer != null && msg.getGroupName().equals(getCurrentGroup())) {
      onAddConsumer.accept(msg);
    }
  }

  /**
   * Retrieves a collection view of all messages within a group.
   *
   * @param group the group
   *
   * @return the list of messages
   */
  public List<ChatMessage> getMessages(final String group) {
    if (!messages.containsKey(group)) {
      return List.of();
    }
    return messages.get(group).stream().sorted().toList();
  }

}
