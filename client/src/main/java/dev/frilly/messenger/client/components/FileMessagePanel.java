package dev.frilly.messenger.client.components;

import dev.frilly.messenger.api.Icon;
import dev.frilly.messenger.api.component.Components;
import dev.frilly.messenger.api.data.FileMessage;
import dev.frilly.messenger.api.gui.LayoutBuilder;
import dev.frilly.messenger.client.AppContext;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A panel for display an embedded file.
 */
public final class FileMessagePanel extends JPanel {

  private final FileMessage msg;

  private final JLabel  username;
  private final JLabel  timestamp;
  private final JButton deleteButton;

  private final JPanel  filePanel;
  private final JButton fileIcon;
  private final JLabel  fileName;

  /**
   * Constructs a new panel to hold a file message.
   *
   * @param msg the message
   */
  public FileMessagePanel(final FileMessage msg) {
    this.msg = msg;
    final var self = msg.getUsername().equals(AppContext.getUsername());

    this.username = Components.label(msg.getUsername())
        .fg(self ? Color.BLUE : Color.BLACK)
        .h4()
        .build();

    final var date       = new Date(msg.getTimestamp());
    final var dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
    this.timestamp    = Components.label(dateFormat.format(date)).build();
    this.deleteButton = Components.button("")
        .icon(Icon.DELETE, 4)
        .toolbar()
        .build();

    this.filePanel = new JPanel();
    this.fileIcon  = Components.button("")
        .icon(Icon.FILE, 16)
        .rounded()
        .build();
    this.fileName  = Components.label(msg.getFileName()).h4().build();

    setupFilePanel();
    setup();
    setupActions();
  }

  private void setupFilePanel() {
    final var l = new LayoutBuilder(filePanel).gaps().border(24, 16);
    l.hoz(l.seq().comp(fileIcon).gap(4).comp(fileName));
    l.ver(l.centerPara().comp(fileIcon).comp(fileName));

    filePanel.setBackground(Color.LIGHT_GRAY);
    filePanel.setBorder(
        BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.BLACK,
            Color.DARK_GRAY));
  }

  private void setup() {
    final var l = new LayoutBuilder(this).gaps();

    l.hoz(l.leadingPara()
        .group(
            l.seq().comp(username).fullGap().comp(deleteButton).comp(timestamp))
        .comp(filePanel));
    l.ver(l.seq()
        .group(l.centerPara().comp(username).comp(deleteButton).comp(timestamp))
        .gap(2)
        .comp(filePanel));

    this.setMaximumSize(
        new Dimension(Short.MAX_VALUE, this.getPreferredSize().height));
    final var user = AppContext.getUsername();
    deleteButton.setVisible(msg.getUsername().equals(user));
    deleteButton.setEnabled(msg.getUsername().equals(user));
  }

  private void setupActions() {
  }

}
