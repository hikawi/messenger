package dev.frilly.messenger.client.components;

import dev.frilly.messenger.api.component.MessageInputPanel;
import dev.frilly.messenger.api.gui.LayoutBuilder;
import dev.frilly.messenger.client.ChatMessage;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The panel that shows chat messages.
 */
public final class ChatPanel extends JPanel {

  private final List<ChatMessagePanel> messagePanels;
  private final MessageInputPanel      inputPanel;

  private final JPanel      messageList;
  private final JScrollPane scrollPane;

  /**
   * Constructs a new chat panel.
   */
  public ChatPanel() {
    messagePanels = new ArrayList<>();
    inputPanel    = new MessageInputPanel();

    messageList = new JPanel();
    scrollPane  = new JScrollPane(messageList);
    scrollPane.setPreferredSize(new Dimension(800, 800));
    setup();
  }

  private void setup() {
    messageList.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    final var l = new LayoutBuilder(this);
    l.ver(l.seq().comp(scrollPane).comp(inputPanel));
    l.hoz(l.centerPara().comp(scrollPane).comp(inputPanel));
  }

  public void addMessage(final ChatMessage message) {

  }

}
