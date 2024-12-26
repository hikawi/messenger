package dev.frilly.messenger.client.components;

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

  private final JLabel    sender;
  private final JTextArea content;
  private final JLabel    timestamp;

  /**
   * Creates a new chat message panel with the provided message.
   *
   * @param message the message
   */
  public ChatMessagePanel(final ChatMessage message) {
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
    setup();
  }

  private void setup() {
    final var l = new LayoutBuilder(this).gaps();

    l.hoz(l.leadingPara()
        .group(l.seq().comp(sender).fullGap().comp(timestamp))
        .comp(content));
    l.ver(l.seq()
        .group(l.basePara().comp(sender).comp(timestamp))
        .gap(2)
        .comp(content));

    this.setMaximumSize(
        new Dimension(Short.MAX_VALUE, this.getPreferredSize().height));
  }

}
