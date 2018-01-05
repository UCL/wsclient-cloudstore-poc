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

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ucl.ircflagship2.wsclient.events.Twitter;
import ucl.ircflagship2.wsclient.persist.ObjectStore;

/**
 *
 * @author david
 */
@Stateless
@LocalBean
public class TwitterCall extends BaseCall {

  private Client client;
  private WebTarget webTarget;
  private String bearerToken;
  private String authHeader;

  @Inject
  private TwitterSettings twitterSettings;

  @EJB
  private ObjectStore objectStore;

  @PostConstruct
  public void init() {

    authHeader = "Basic " + twitterSettings.getBearerCredentials();

    client = ClientBuilder.newBuilder()
            .register(feature)
            .build();

  }

  public void onEvent(@Observes @Twitter Long timerLong) {

  }

  private void obtainBearerToken() {
    // move to init??
    webTarget = client.target(twitterSettings.getBaseUrl())
            .path(twitterSettings.getOauth2TokenEndpoint());

    Invocation.Builder builder = webTarget.queryParam("grant_type", "client_credentials")
            .request(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
            .header("Authorization", authHeader)
            .header("Content-Type", twitterSettings.getContentTypeHeader());

    // this should be the start of the method
    Response response = builder.get();

    if (response.getStatusInfo() == Response.Status.OK) {
      // create an entity class to facilitate the extraction of the token
      String entityString = response.readEntity(String.class);
    }
  }

}
