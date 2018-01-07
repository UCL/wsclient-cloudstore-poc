/*
 * The MIT License
 *
 * Copyright 2018 David Guzman <d.guzman at ucl.ac.uk>.
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

import javax.json.bind.annotation.JsonbProperty;

/**
 *
 * @author David Guzman <d.guzman at ucl.ac.uk>
 */
public class MsGraphAuthEntity {

  @JsonbProperty("token_type")
  private String tokenType;

  private String scope;

  @JsonbProperty("expires_in")
  private Integer expiresIn;

  @JsonbProperty("ext_expires_in")
  private Integer extExpiresIn;

  @JsonbProperty("access_token")
  private String accessToken;

  @JsonbProperty("refresh_token")
  private String refreshToken;

  public void setTokenType(String tokenType) {
    this.tokenType = tokenType;
  }

  public void setScope(String scope) {
    this.scope = scope;
  }

  public void setExpiresIn(Integer expiresIn) {
    this.expiresIn = expiresIn;
  }

  public void setExtExpiresIn(Integer extExpiresIn) {
    this.extExpiresIn = extExpiresIn;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

}
