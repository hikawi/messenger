package dev.frilly.messenger.api.data;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * A message with an embedded file.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public final class FileMessage extends Message {

  private String fileName;
  private String filePath;

}
