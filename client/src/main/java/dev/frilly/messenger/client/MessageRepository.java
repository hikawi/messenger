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

  @Setter
  @Getter
  private String currentGroup;

  @Getter
  @Setter
  private Consumer<ChatMessage> onAddConsumer;

  /**
   * Sends a message via the REST socket and returns the instance if successful.
   *
   * @param content the content
   *
   * @return the optional wrapper of a chat message.
   */
  public Optional<ChatMessage> sendMessage(final String content) {
    final var msg = new ChatMessage();
    msg.setUsername(AppContext.getUsername());
    msg.setContent(content);
    msg.setGroupName(getCurrentGroup());

    System.out.println(content);
    final var socket = AppContext.getRestHandler();
    final var res = socket.query(
        "sendmessage %s %s %s".formatted(msg.getUsername(), msg.getGroupName(),
            msg.getContent()));

    final var code = res.split(" ")[0];
    if (code.equals("201")) {
      addMessage(msg);
      return Optional.of(msg);
    } else {
      final var frame = AppContext.getFrame();
      JOptionPane.showMessageDialog(frame.getFrame(),
          "There was an error sending the message", "Error",
          JOptionPane.ERROR_MESSAGE);
      return Optional.empty();
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
    return Collections.unmodifiableList(messages.get(group));
  }

}