package dev.frilly.messenger.client.components;

import dev.frilly.messenger.api.gui.LayoutBuilder;

import javax.swing.*;

/**
 * The panel to display a simple chat message.
 */
public final class ChatMessagePanel extends JPanel {

  private final String  username;
  private final String  content;
  private final boolean self;

  private final JLabel    usernameLabel;
  private final JTextArea contentArea;

  /**
   * Creates a new chat message panel.
   *
   * @param username the username sending the message.
   * @param content  the content
   * @param self     is it mine?
   */
  public ChatMessagePanel(String username, String content, boolean self) {
    this.content  = content;
    this.self     = self;
    this.username = self ? "You" : username;
    setup();
  }

  private void setup() {
    final var l = new LayoutBuilder(this);
  }

}
