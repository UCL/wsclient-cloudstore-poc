/*
 * The MIT License
 *
 * Copyright 2017 David Guzman <d.guzman at ucl.ac.uk>.
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

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import ucl.ircflagship2.wsclient.util.Signature;

/**
 *
 * @author David Guzman <d.guzman at ucl.ac.uk>
 */
@ApplicationScoped
public class InstagramSettings {

  private final String BASE_URL = "https://api.instagram.com/v1";
  private final String ENDPOINT = "/media/search";
  private final String ACCESS_TOKEN = System.getenv("INSTAGRAM_ACCESS_TOKEN");
  private final String SECRET = System.getenv("INSTAGRAM_SECRET");
  private final String QUERY_FORMAT = "%s=%s";
  private final String SIGNATURE_KEY = "sig";
  private final SortedMap<String, String> parameterMap = new TreeMap<>();

  public InstagramSettings() {
    parameterMap.put("access_token", ACCESS_TOKEN);
    parameterMap.put("lat", "51.538000"); // Met Office station in Islington
    parameterMap.put("lng", "-0.115000"); // Met Office station in Islington
    parameterMap.put("distance", "5000"); // 5km radius
  }

  public String getBaseUrl() {
    return BASE_URL;
  }

  public SortedMap<String, String> getParameterMap() {
    return parameterMap;
  }

  public String queryParameters() {
    return parameterMap.entrySet().stream()
            .map((e) -> String.format(QUERY_FORMAT, e.getKey(), e.getValue()))
            .collect(Collectors.joining("&"));
  }

  public String getEndpoint() {
    return ENDPOINT;
  }

  public Optional<String> getSignature() {
    try {
      String sig = Signature.signInstagramCall(ENDPOINT, parameterMap, SECRET);
      return Optional.of(sig);
    } catch (NoSuchAlgorithmException | InvalidKeyException ex) {
      return Optional.empty();
    }
  }

  public String getSignatureKey() {
    return SIGNATURE_KEY;
  }

}
