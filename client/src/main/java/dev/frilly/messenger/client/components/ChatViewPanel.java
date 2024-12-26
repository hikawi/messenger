package dev.frilly.messenger.client.components;

import dev.frilly.messenger.api.Icon;
import dev.frilly.messenger.api.component.Components;
import dev.frilly.messenger.api.data.ChatMessage;
import dev.frilly.messenger.api.data.FileMessage;
import dev.frilly.messenger.api.gui.LayoutBuilder;
import dev.frilly.messenger.client.AppContext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * The panel to view chat messages and a text field to send messages.
 */
public final class ChatViewPanel extends JPanel {

  private final JLabel      groupName    = Components.label("public")
      .h4()
      .build();
  private final JPanel      messagesList = new JPanel();
  private final JScrollPane scrollPane   = new JScrollPane(messagesList);

  private final JPanel     chatField  = new JPanel();
  private final JButton    fileButton = Components.button("")
      .icon(Icon.FILE, 12)
      .rounded()
      .build();
  private final JTextField textField  = Components.textField()
      .showClear()
      .placeholder("Send a message...")
      .build();
  private final JButton    sendButton = Components.button("Send")
      .icon(Icon.SEND, 12)
      .rounded()
      .build();

  /**
   * Creates a new panel instance to view chat messages.
   */
  public ChatViewPanel() {
    setup();
    setupActions();
    setupHook();
  }

  private void setup() {
    setupChatField();
    messagesList.setLayout(new BoxLayout(messagesList, BoxLayout.Y_AXIS));
    scrollPane.setVerticalScrollBarPolicy(
        ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    this.setMinimumSize(new Dimension(800, 800));

    final var l = new LayoutBuilder(this).gaps();
    l.ver(l.seq().comp(groupName).comp(scrollPane).comp(chatField));
    l.hoz(l.trailingPara().comp(groupName).comp(scrollPane).comp(chatField));
  }

  private void setupActions() {
    textField.addKeyListener(new KeyListener() {
      @Override
      public void keyTyped(KeyEvent e) {
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
      if (textField.getText().isBlank()) {
        return;
      }

      AppContext.getMessageRepository().sendMessage(textField.getText());
      textField.setText("");
      textField.requestFocusInWindow();
    });

    fileButton.addActionListener(e -> {
      final var file      = new JFileChooser(".");
      final var twentyMb  = 20 * 1024 * 1024;
      final var frame     = AppContext.getFrame().getFrame();
      final var returnVal = file.showOpenDialog(frame);

      if (returnVal != JFileChooser.APPROVE_OPTION) {
        return;
      }

      final var selection = file.getSelectedFile();
      if (selection == null) {
        return;
      }

      if (!selection.isFile()) {
        JOptionPane.showMessageDialog(frame, "That isn't a file", "Error!",
            JOptionPane.ERROR_MESSAGE);
        return;
      }

      if (selection.length() >= twentyMb) {
        JOptionPane.showMessageDialog(frame, "Max is 20MB.", "Error!",
            JOptionPane.ERROR_MESSAGE);
      }

      try (final var fileSocket = new Socket("localhost", 8082)) {
        writeDataOutput(fileSocket, selection);
      } catch (final Exception exception) {
        exception.printStackTrace();
        JOptionPane.showMessageDialog(frame, "Error uploading file!", "Error!",
            JOptionPane.ERROR_MESSAGE);
      }
    });
  }

  private void setupHook() {
    AppContext.getMessageRepository().setOnAddConsumer(msg -> {
      if (msg instanceof ChatMessage chat) {
        messagesList.add(new ChatMessagePanel(chat));
      } else if (msg instanceof FileMessage file) {
        messagesList.add(new FileMessagePanel(file));
      }

      scrollPane.revalidate();
      scrollPane.repaint();

      final var scroll = scrollPane.getVerticalScrollBar();
      scroll.setValue(scroll.getMaximum());
    });

    AppContext.getMessageRepository().setOnGroupSwap(group -> {
      if (group.equals("public")) {
        groupName.setText("public");
        pullChatMessages("public");
        return;
      }

      final var groupRepo = AppContext.getGroupRepository();
      final var groupInst = groupRepo.getGroupChat(UUID.fromString(group));
      groupName.setText(groupInst.getMembers()
          .stream()
          .map(mem -> mem.equals(AppContext.getUsername()) ? "You" : mem)
          .collect(Collectors.joining(", ")));
      pullChatMessages(groupInst.getUuid().toString());
    });

    AppContext.getMessageRepository()
        .setOnDeleteConsumer(msg -> pullChatMessages(msg.getGroupName()));
  }

  private void setupChatField() {
    textField.setPreferredSize(new Dimension(700, 20));
    final var l = new LayoutBuilder(chatField).gaps();
    l.hoz(l.seq()
        .comp(fileButton)
        .gap(4)
        .comp(textField)
        .gap(4)
        .comp(sendButton));
    l.ver(l.basePara().comp(fileButton).comp(textField).comp(sendButton));
    l.linkY(textField, sendButton, fileButton);
  }

  private static void writeDataOutput(Socket fileSocket, File selection) throws
                                                                         IOException {
    final var output = new DataOutputStream(fileSocket.getOutputStream());

    output.writeUTF(AppContext.getUsername());
    output.writeUTF(AppContext.getMessageRepository().getCurrentGroup());
    output.writeUTF(selection.getName());

    final var buffer    = new byte[8000];
    var       read      = 0;
    final var fileInput = new FileInputStream(selection);

    while (fileInput.available() > 0) {
      read = fileInput.read(buffer);
      output.write(read);
    }

    fileInput.close();
    output.flush();
    output.close();
  }

  private void pullChatMessages(final String groupId) {
    messagesList.removeAll();
    final var repo     = AppContext.getMessageRepository();
    final var messages = repo.getMessages(groupId);

    for (final var msg : messages) {
      if (msg instanceof ChatMessage chat) {
        messagesList.add(new ChatMessagePanel(chat));
      } else if (msg instanceof FileMessage file) {
        messagesList.add(new FileMessagePanel(file));
      }
    }

    scrollPane.revalidate();
    scrollPane.repaint();
    final var scroll = scrollPane.getVerticalScrollBar();
    scroll.setValue(scroll.getMaximum());
  }

}
