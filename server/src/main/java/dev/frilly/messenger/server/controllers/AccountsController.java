package dev.frilly.messenger.server.controllers;

import dev.frilly.messenger.server.forms.PostLoginBody;
import dev.frilly.messenger.server.forms.PostRegisterBody;
import dev.frilly.messenger.server.models.Account;
import dev.frilly.messenger.server.repository.AccountRepository;
import dev.frilly.messenger.server.response.LoginResponse;
import dev.frilly.messenger.server.response.RegisterResponse;
import dev.frilly.messenger.server.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * The controllers for handling accounts management of a user.
 */
@RestController
public final class AccountsController {

  @Autowired
  private JwtService jwtService;

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

  /**
   * POST Login handler.
   */
  @PostMapping("/login")
  public LoginResponse login(@RequestBody PostLoginBody body) {
    final var account = accountRepository.findByUsername(body.getUsername());
    if (account == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    if (!passwordEncoder.matches(body.getPassword(), account.getPassword())) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    return LoginResponse.of(account.getUsername(),
        jwtService.generateToken(account.getId()));
  }

  /**
   * POST register handler.
   *
   * @param body the body
   *
   * @return the response
   */
  @PostMapping("/register")
  public RegisterResponse register(@RequestBody PostRegisterBody body) {
    final var account = accountRepository.findByUsername(body.getUsername());
    if (account != null) {
      throw new ResponseStatusException(HttpStatus.CONFLICT);
    }

    final var newAccount = new Account();
    newAccount.setUsername(body.getUsername());
    newAccount.setPassword(passwordEncoder.encode(body.getPassword()));
    final var entity = accountRepository.save(newAccount);
    return RegisterResponse.of(entity.getId(), entity.getUsername());
  }

}
