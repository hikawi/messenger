package dev.frilly.messenger.client.components;

import dev.frilly.messenger.api.Icon;
import dev.frilly.messenger.api.component.Components;
import dev.frilly.messenger.api.data.ChatMessage;
import dev.frilly.messenger.api.gui.LayoutBuilder;
import dev.frilly.messenger.client.AppContext;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The panel to display a single chat message.
 */
public final class ChatMessagePanel extends JPanel {

  private final ChatMessage message;

  private final JLabel    sender;
  private final JTextArea content;
  private final JLabel    timestamp;
  private final JButton   deleteButton;

  /**
   * Creates a new chat message panel with the provided message.
   *
   * @param message the message
   */
  public ChatMessagePanel(final ChatMessage message) {
    this.message = message;
    this.sender  = Components.label(message.getUsername())
        .h4()
        .fg(message.getUsername().equals(AppContext.getUsername()) ? Color.BLUE
                                                                   :
            Color.BLACK)
        .build();
    this.content = Components.textArea(message.getContent())
        .transparent()
        .noEdit()
        .wrap()
        .build();

    final var date       = new Date(message.getTimestamp());
    final var dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
    this.timestamp = Components.label(dateFormat.format(date)).build();

    this.deleteButton = Components.button("")
        .icon(Icon.DELETE, 4)
        .toolbar()
        .build();

    setup();
    setupActions();
  }

  private void setup() {
    final var l = new LayoutBuilder(this).gaps();

    l.hoz(l.leadingPara()
        .group(
            l.seq().comp(sender).fullGap().comp(deleteButton).comp(timestamp))
        .comp(content));
    l.ver(l.seq()
        .group(l.centerPara().comp(sender).comp(deleteButton).comp(timestamp))
        .gap(2)
        .comp(content));

    this.setMaximumSize(
        new Dimension(Short.MAX_VALUE, this.getPreferredSize().height));
    final var user = AppContext.getUsername();
    deleteButton.setVisible(message.getUsername().equals(user));
    deleteButton.setEnabled(message.getUsername().equals(user));
  }

  private void setupActions() {
    deleteButton.addActionListener(e -> {
      final var rest = AppContext.getRestHandler();
      final var res = rest.query(
          "deletemessage %s %s %d".formatted(message.getUsername(),
              message.getGroupName(), message.getTimestamp()));

      if (!res.startsWith("200")) {
        final var frame = AppContext.getFrame().getFrame();
        JOptionPane.showMessageDialog(frame,
            "There was an error deleting this message?", "Error?",
            JOptionPane.ERROR_MESSAGE);
      }
    });
  }

}
