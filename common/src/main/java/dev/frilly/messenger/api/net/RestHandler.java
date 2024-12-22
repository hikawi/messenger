package dev.frilly.messenger.api.net;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * A threaded class for handling sockets.
 */
public final class RestHandler extends Thread {

  private final Socket         socket;
  private final BufferedReader input;
  private final BufferedWriter output;
  private       boolean        isRunning = true;

  /**
   * Creates a new socket handler.
   *
   * @param socket the socket.
   */
  @SneakyThrows
  public RestHandler(final Socket socket) {
    this.socket = socket;
    this.input  = new BufferedReader(
        new InputStreamReader(socket.getInputStream()));
    this.output = new BufferedWriter(
        new OutputStreamWriter(socket.getOutputStream()));
  }

  /**
   * Sends a query over the socket and waits for a response.
   *
   * @param cmd the command to send.
   *
   * @return the response from the server.
   */
  @SneakyThrows
  public synchronized String query(final String cmd) {
    if (!socket.isConnected() || socket.isClosed()) {
      throw new IllegalStateException("Socket is closed or not connected.");
    }

    output.write(cmd + "\n");
    output.flush();
    return input.readLine();
  }

  /**
   * Gracefully closes the socket and stops the handler.
   */
  @SneakyThrows
  public synchronized void close() {
    isRunning = false;
    input.close();
    output.close();
    socket.close();
  }

  @Override
  public void run() {
    while (isRunning && !socket.isClosed()) {
      // Intentionally left blank.
    }
  }

}
