/*
 * The MIT License
 *
 * Copyright 2018 david.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ucl.ircflagship2.wsclient.apicall;

import java.time.LocalDate;
import java.util.Base64;
import javax.enterprise.context.ApplicationScoped;
import ucl.ircflagship2.wsclient.util.Converter;

/**
 *
 * @author david
 */
@ApplicationScoped
public class TwitterSettings {

  private final String BASE_URL = "https://api.twitter.com";
  private final String OAUTH2_TOKEN_ENDPOINT = "/oauth2/token";
  private final String STANDARD_SEARCH_ENDPOINT = "/1.1/search/tweets.json";
  private final String CONSUMER_KEY = System.getenv("TWITTER_KEY");
  private final String CONSUMER_SECRET = System.getenv("TWITTER_SECRET");
  private final String CONTENT_TYPE_HEADER = "application/x-www-form-urlencoded;charset=UTF-8";
  private final String bearerCredentials;

  public TwitterSettings() {
    String encodedKey = Converter.encodeRfc1738(CONSUMER_KEY).get();
    String encodedSecret = Converter.encodeRfc1738(CONSUMER_SECRET).get();
    String credentials = encodedKey + ":" + encodedSecret;
    bearerCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
  }

  public String getBearerCredentials() {
    return bearerCredentials;
  }

  public String getBaseUrl() {
    return BASE_URL;
  }

  public String getOauth2TokenEndpoint() {
    return OAUTH2_TOKEN_ENDPOINT;
  }

  public String getContentTypeHeader() {
    return CONTENT_TYPE_HEADER;
  }

  public String getStandardSearchEndpoint() {
    return STANDARD_SEARCH_ENDPOINT;
  }

  public String calculateUntilDate() {
    LocalDate today = LocalDate.now();
    LocalDate aWeekAgo = today.minusDays(6);
    return aWeekAgo.toString();
  }

}
