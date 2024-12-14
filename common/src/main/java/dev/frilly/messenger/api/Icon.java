package dev.frilly.messenger.api;

import lombok.Getter;

/**
 * Enumeration for icons.
 */
public enum Icon {

  WIFI;

  @Getter
  private final String value;

  Icon() {
    this.value = name().toLowerCase().replaceAll("\\s+", "-");
  }

  Icon(final String value) {
    this.value = value;
  }

}