package dev.frilly.messenger.api.net;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.io.BufferedInputStream;
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
  @SneakyThrows
  public static HttpFetch fetch(final String url) {
    return new HttpFetch(url);
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

}
