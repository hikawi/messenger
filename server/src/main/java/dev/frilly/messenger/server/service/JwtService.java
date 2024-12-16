package dev.frilly.messenger.server.service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

/**
 * The service for handling JWTs.
 */
@Service
public class JwtService {

  // I don't care lmao.
  private final SecretKey key = new SecretKeySpec(
      "tP7k3lA0jV/J3A34mV8mQyLt9G5jJ+jp+YzyEj6azH2CV34J14g3c9j0/38VhO+a".getBytes(),
      "HMACSHA512");

  /**
   * Generates a JWT from an account's id.
   *
   * @param id the id
   *
   * @return the token
   */
  public String generateToken(final long id) {
    return Jwts.builder()
        .claim("id", id)
        .expiration(new Date(System.currentTimeMillis() + 120 * 60 * 1000))
        .signWith(key, Jwts.SIG.HS512)
        .compact();
  }

  /**
   * Parses a JWT token back into an account's id.
   *
   * @param token the token
   *
   * @return the ID
   */
  public long parseToken(final String token) {
    return Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .get("id", Long.class);
  }

  /**
   * Parses a JWT token, and checks if it's a valid token.
   *
   * @param token the token
   *
   * @return true if it is valid
   */
  public boolean validateToken(String token) {
    try {
      Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }

}
