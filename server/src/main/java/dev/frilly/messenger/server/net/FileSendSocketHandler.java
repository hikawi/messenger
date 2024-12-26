package dev.frilly.messenger.server.net;

import lombok.Cleanup;
import lombok.SneakyThrows;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;

/**
 * A socket handler for sending files away.
 */
public final class FileSendSocketHandler extends Thread {

  private final Socket client;

  /**
   * Creates a new socket handler for sending files to client.
   *
   * @param client the client
   */
  public FileSendSocketHandler(final Socket client) {
    this.client = client;
  }

  @Override
  @SneakyThrows
  public void run() {
    final @Cleanup var input    = new DataInputStream(client.getInputStream());
    final var          filePath = input.readUTF();

    final @Cleanup var output = new DataOutputStream(client.getOutputStream());
    final var          file   = new File(".data/files", filePath);
    if (!file.exists()) {
      client.close();
      return;
    }

    final @Cleanup var fileInput = new FileInputStream(file);
    final var          buffer    = new byte[8000];
    var                length    = 0;
    while ((length = fileInput.read(buffer)) > 0) {
      output.write(buffer, 0, length);
    }

    client.close();
  }

}
