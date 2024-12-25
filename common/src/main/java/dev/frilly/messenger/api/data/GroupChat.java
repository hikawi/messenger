package dev.frilly.messenger.api.data;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Data representation for a simple group chat.
 */
@Data
public class GroupChat {

  private UUID        uuid;
  private Set<String> members = new HashSet<>();

}
