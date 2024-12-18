package dev.frilly.messenger.api.net;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for a HTTP fetch request build.
 */
public final class HttpFetch {

  private final String              originalURI;
  private final Map<String, String> query   = new HashMap<>();
  private final Map<String, String> headers = new HashMap<>();
  private final Map<String, Object> body    = new HashMap<>();

  @SneakyThrows
  private HttpFetch(final String uri) {
    this.originalURI = uri;
  }

  /**
   * Opens a new fetch request to the provided URL.
   *
   * @param url the url
   *
   * @return the http fetch
   */
  public static HttpFetch fetch(final String url) {
    return new HttpFetch(url);
  }

  /**
   * Opens a new authorized fetch request, given a URL and a token.
   *
   * @param url   the url
   * @param token the token
   *
   * @return the http fetch instance
   */
  public static HttpFetch authFetch(final String url, final String token) {
    return new HttpFetch(url).header("Authorization",
        "Bearer %s".formatted(token));
  }

  /**
   * Appends a header to the URI.
   *
   * @param key   the key
   * @param value the value
   *
   * @return this
   */
  public HttpFetch header(final String key, final String value) {
    headers.put(key, value);
    return this;
  }

  /**
   * Appends a query to the URI.
   *
   * @param key   the key
   * @param value the value
   *
   * @return this
   */
  public HttpFetch query(final String key, final String value) {
    query.put(key, URLEncoder.encode(value, StandardCharsets.UTF_8));
    return this;
  }

  /**
   * Appends a key-value pair to the body.
   *
   * @param key   the key
   * @param value the value
   *
   * @return this
   */
  public HttpFetch body(final String key, final Object value) {
    body.put(key, value);
    return this;
  }

  /**
   * Makes a GET request.
   */
  @SneakyThrows
  public HttpResponse get() {
    final var conn = connect();
    conn.setRequestMethod("GET");

    final var stream = new BufferedInputStream(conn.getInputStream());
    final var code   = conn.getResponseCode();
    final var mapper = new ObjectMapper();
    final var body   = mapper.readTree(stream);
    conn.disconnect();

    return new HttpResponse(code, body);
  }

  @SneakyThrows
  private HttpURLConnection connect() {
    final var builder = new StringBuilder(originalURI);
    if (!query.isEmpty()) {
      builder.append("?");
      query.forEach((k, v) -> {
        builder.append(k).append("=").append(v).append("&");
      });
      builder.deleteCharAt(builder.length() - 1);
    }

    final var url  = new URI(builder.toString()).toURL();
    final var conn = (HttpURLConnection) url.openConnection();
    headers.forEach(conn::setRequestProperty);
    return conn;
  }

  /**
   * Makes a POST request
   */
  @SneakyThrows
  public HttpResponse post() {
    final var conn   = connect();
    final var mapper = new ObjectMapper();

    try {
      // Open connection
      conn.setRequestMethod("POST");
      conn.setRequestProperty("Content-Type", "application/json");
      conn.setDoOutput(true);

      // Write request body
      try (OutputStream out = new BufferedOutputStream(conn.getOutputStream())
      ) {
        mapper.writeValue(out, body);
      }

      // Read response
      int responseCode = conn.getResponseCode();
      InputStream responseStream = (responseCode >= 200 && responseCode < 300)
                                   ? conn.getInputStream() // Success
                                   : conn.getErrorStream(); // Error

      try (InputStream in = new BufferedInputStream(responseStream)) {
        var resBody = mapper.readTree(in);
        return new HttpResponse(responseCode, resBody);
      }
    } finally {
      if (conn != null) {
        conn.disconnect(); // Clean up connection
      }
    }
  }

}
