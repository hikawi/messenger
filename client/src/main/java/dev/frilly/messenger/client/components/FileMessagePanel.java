package dev.frilly.messenger.client.components;

import dev.frilly.messenger.api.Icon;
import dev.frilly.messenger.api.component.Components;
import dev.frilly.messenger.api.data.FileMessage;
import dev.frilly.messenger.api.gui.LayoutBuilder;
import dev.frilly.messenger.client.AppContext;
import lombok.Cleanup;
import lombok.SneakyThrows;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.io.*;
import java.net.Socket;
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
    deleteButton.addActionListener(e -> {
      final var rest = AppContext.getRestHandler();
      final var res = rest.query(
          "deletemessage %s %s %d".formatted(msg.getUsername(),
              msg.getGroupName(), msg.getTimestamp()));

      if (!res.startsWith("200")) {
        final var frame = AppContext.getFrame().getFrame();
        JOptionPane.showMessageDialog(frame,
            "There was an error deleting this message?", "Error?",
            JOptionPane.ERROR_MESSAGE);
      }
    });

    fileIcon.addActionListener(e -> {
      fileIcon.setEnabled(false);
      new Thread(this::downloadFile).start();
    });
  }

  @SneakyThrows
  private void downloadFile() {
    final var socket = new Socket("localhost", 8083);

    final @Cleanup var output = new DataOutputStream(socket.getOutputStream());
    output.writeUTF(msg.getFilePath());

    final var          buffer = new byte[8000];
    final @Cleanup var input  = new DataInputStream(socket.getInputStream());

    final var folder = new File(System.getProperty("user.home"), "Downloads");
    final var file   = new File(folder, msg.getFileName());
    if (!file.exists()) {
      folder.mkdirs();
      file.createNewFile();
    }

    final var fileOutput = new BufferedOutputStream(new FileOutputStream(file));
    var       length     = 0;
    while ((length = input.read(buffer)) > 0) {
      fileOutput.write(buffer, 0, length);
    }
    fileOutput.flush();
    fileOutput.close();

    System.out.println("Downloaded file length " + file.length());

    SwingUtilities.invokeLater(() -> {
      final var frame = AppContext.getFrame().getFrame();
      fileIcon.setEnabled(true);
      JOptionPane.showMessageDialog(frame,
          "Downloaded file \"%s\"".formatted(msg.getFileName()), "Success!",
          JOptionPane.INFORMATION_MESSAGE);
    });
  }

}
