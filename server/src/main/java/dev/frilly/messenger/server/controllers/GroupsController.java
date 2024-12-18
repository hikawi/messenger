package dev.frilly.messenger.server.controllers;

import dev.frilly.messenger.server.models.GroupChat;
import dev.frilly.messenger.server.repository.AccountRepository;
import dev.frilly.messenger.server.repository.GroupRepository;
import dev.frilly.messenger.server.response.PostGroupResponse;
import dev.frilly.messenger.server.service.JwtService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * REST controller for groups-related actions.
 */
@RestController
public class GroupsController {

  @Autowired
  private JwtService jwtService;

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private GroupRepository groupRepository;

  @Autowired
  private EntityManager em;

  /**
   * REST controller for GET /groups. Retrieves all groups of an account.
   *
   * @return the group chats.
   */
  @GetMapping("/groups")
  public List<GroupChat> getGroups() {
    final var id = (String) SecurityContextHolder.getContext()
        .getAuthentication()
        .getPrincipal();
    final var account = accountRepository.findById(Long.parseLong(id));
    if (account.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    return account.get().getGroupChats().stream().toList();
  }

  /**
   * REST controller for POST /groups. Create a new group on an account.
   */
  @PostMapping("/groups")
  public PostGroupResponse createGroup() {
    final var id      = (String) SecurityContextHolder.getContext()
        .getAuthentication()
        .getPrincipal();
    final var account = accountRepository.findById(Long.parseLong(id));
    if (account.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    final var group = new GroupChat();
    group.addMember(account.get());
    account.get().addGroupChat(group);
    em.persist(group);
    em.persist(account);

    return new PostGroupResponse(group.getId());
  }

}
