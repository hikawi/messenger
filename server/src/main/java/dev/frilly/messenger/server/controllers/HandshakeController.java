package dev.frilly.messenger.server.controllers;

import dev.frilly.messenger.server.records.ApiVersion;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The controller for handshaking (first initial actions to establish some
 * common grounds) actions.
 */
@RestController
public final class HandshakeController {

  /**
   * Initiates a handshake to tell the client they're connecting to the
   * correct server.
   */
  @GetMapping("handshake")
  public ApiVersion handshake() {
    return new ApiVersion("v1");
  }

}
