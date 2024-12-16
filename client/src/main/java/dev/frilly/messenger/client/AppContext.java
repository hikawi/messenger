package dev.frilly.messenger.client;

import dev.frilly.messenger.api.ApplicationFrame;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;

/**
 * Utility class holding application contexts.
 */
@UtilityClass
public final class AppContext {

  @Getter
  @Setter
  private String authToken;

  @Getter
  @Setter
  private ApplicationFrame frame;

}
