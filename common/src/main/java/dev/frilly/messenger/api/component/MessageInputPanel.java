package dev.frilly.messenger.api.component;

import dev.frilly.messenger.api.Icon;
import dev.frilly.messenger.api.gui.LayoutBuilder;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * A panel for inputting a message to send.
 */
public final class MessageInputPanel extends JPanel {

  private final JTextField field = Components.textField()
      .placeholder("Send a message")
      .showClear()
      .build();
  private final JButton    send  = Components.button("")
      .icon(Icon.SEND, 16)
      .build();

  private Optional<Consumer<String>> callback;

  /**
   * Constructs a new message input panel.
   */
  public MessageInputPanel() {
    setup();
    setupActions();
  }

  private void setup() {
    final var l = new LayoutBuilder(this).border(8, 4);
    l.hoz(l.seq().comp(field).comp(send));
    l.ver(l.basePara().comp(field).comp(send));
  }

  private void setupActions() {
    field.addKeyListener(new KeyListener() {
      @Override
      public void keyTyped(KeyEvent e) {
        // Intentionally left blank.
      }

      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          send();
        }
        send.setEnabled(!field.getText().isBlank());
      }

      @Override
      public void keyReleased(KeyEvent e) {
        // Intentionally left blank.
      }
    });

    send.addActionListener(e -> send());
  }

  private void send() {
    if (field.getText().isBlank()) {
      return;
    }

    callback.ifPresent(c -> c.accept(field.getText()));
    field.requestFocusInWindow();
    field.setText("");
  }

  /**
   * Hooks a consumer to be ran whenever send button was clicked with a text
   * message.
   *
   * @param callback the callback
   */
  public void onSend(final Consumer<String> callback) {
    this.callback = Optional.ofNullable(callback);
  }

}
