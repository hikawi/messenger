package dev.frilly.messenger.server.net;

import dev.frilly.messenger.api.data.FileMessage;
import dev.frilly.messenger.server.ServerContext;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

/**
 * A socket handler for file receiving.
 */
public final class FileSocketHandler extends Thread {

  private final Socket client;

  /**
   * Constructs a new instance of file socket.
   *
   * @param client the client
   */
  public FileSocketHandler(final Socket client) {
    this.client = client;
  }

  @Override
  public void run() {
    while (client.isConnected() && !client.isClosed()) {
      try (final var input = new DataInputStream(client.getInputStream())) {
        // Retrieve file metadata.
        final var username  = input.readUTF();
        final var groupName = input.readUTF();
        final var fileName  = input.readUTF();
        final var filePath  = UUID.randomUUID().toString();

        // Create file handle.
        final var folder = new File(".data", "files/%s".formatted(groupName));
        final var file   = new File(folder, filePath);
        if (!file.exists()) {
          folder.mkdirs();
          file.createNewFile();
        }

        // Transfer the file.
        final var fileOutput = new FileOutputStream(file);
        final var buffer     = new byte[8000];
        var       length     = 0;
        while ((length = input.read(buffer)) > 0) {
          fileOutput.write(buffer, 0, length);
        }
        fileOutput.flush();
        fileOutput.close();

        System.out.println("Received file length " + file.length());

        // Save a message instance.
        final var fileMsg = new FileMessage();
        fileMsg.setFileName(fileName);
        fileMsg.setUsername(username);
        fileMsg.setGroupName(groupName);
        fileMsg.setFilePath("%s/%s".formatted(groupName, filePath));
        fileMsg.setNow();
        MessagesController.saveMessage(fileMsg);

        // Send back the message instance.
        for (final var ws : ServerContext.getWsHandlers()) {
          ws.write("sendfile %s %s %d %s".formatted(username, groupName,
              fileMsg.getTimestamp(), fileMsg.getFilePath()));
        }

        client.close();
        break;
      } catch (final IOException ex) {
        ex.printStackTrace();
      }
    }
  }

}
