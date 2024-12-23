package dev.frilly.messenger.client.components;

import dev.frilly.messenger.api.Icon;
import dev.frilly.messenger.api.component.Components;
import dev.frilly.messenger.api.gui.LayoutBuilder;
import dev.frilly.messenger.client.AppContext;
import dev.frilly.messenger.client.gui.AppScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * The panel to view chat messages and a text field to send messages.
 */
public final class ChatViewPanel extends JPanel {

  private final AppScreen app;

  private final JPanel      messagesList = new JPanel();
  private final JScrollPane scrollPane   = new JScrollPane(messagesList);

  private final JPanel     chatField  = new JPanel();
  private final JTextField textField  = Components.textField()
      .showClear()
      .placeholder("Send a message...")
      .build();
  private final JButton    sendButton = Components.button("Send")
      .icon(Icon.SEND, 12)
      .rounded()
      .build();

  public ChatViewPanel(final AppScreen app) {
    this.app = app;
    setup();
    setupActions();
    setupHook();
  }

  private void setup() {
    setupChatField();
    messagesList.setLayout(new BoxLayout(messagesList, BoxLayout.Y_AXIS));
    this.setMinimumSize(new Dimension(800, 800));

    final var l = new LayoutBuilder(this).gaps();
    l.ver(l.seq().comp(scrollPane).comp(chatField));
    l.hoz(l.trailingPara().comp(scrollPane).comp(chatField));
  }

  private void setupActions() {
    textField.addKeyListener(new KeyListener() {
      @Override
      public void keyTyped(KeyEvent e) {
        sendButton.setEnabled(!textField.getText().isBlank());
      }

      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          sendButton.doClick();
        }
      }

      @Override
      public void keyReleased(KeyEvent e) {
      }
    });

    sendButton.addActionListener(e -> {
      final var opt = AppContext.getMessageRepository()
          .sendMessage(textField.getText());
      opt.ifPresent(
          chatMessage -> messagesList.add(new ChatMessagePanel(chatMessage)));
    });
  }

  private void setupHook() {
    //    AppContext.getMessageRepository().setOnAddConsumer(msg -> {
    //      messagesList.add(new ChatMessagePanel(msg));
    //    });
  }

  private void setupChatField() {
    textField.setPreferredSize(new Dimension(700, 20));
    final var l = new LayoutBuilder(chatField).gaps();
    l.hoz(l.seq().comp(textField).gap(4).comp(sendButton));
    l.ver(l.basePara().comp(textField).comp(sendButton));
    l.linkY(textField, sendButton);
  }

}
