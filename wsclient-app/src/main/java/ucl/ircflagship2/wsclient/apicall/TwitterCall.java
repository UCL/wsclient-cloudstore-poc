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
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ucl.ircflagship2.wsclient.events.Twitter;
import ucl.ircflagship2.wsclient.persist.CacheKey;
import ucl.ircflagship2.wsclient.persist.NodeCache;
import ucl.ircflagship2.wsclient.persist.FileStore;
import ucl.ircflagship2.wsclient.scheduler.ServiceTag;

/**
 *
 * @author David Guzman <d.guzman at ucl.ac.uk>
 */
@Stateless
@LocalBean
public class TwitterCall extends BaseCall {

  private Client client;
  private String authHeader;
  private Invocation.Builder authBuilder;
  private Invocation.Builder sampleBuilder;
  private Form authForm = new Form();
  private final Jsonb JSONB = JsonbBuilder.create();

  @Inject
  private TwitterSettings twitterSettings;

  @EJB
  private NodeCache nodeCache;

  @EJB
  private FileStore store;

  @PostConstruct
  public void init() {

    authHeader = "Basic " + twitterSettings.getBearerCredentials();

    authForm = authForm.param("grant_type", "client_credentials");

    client = ClientBuilder.newBuilder()
            .register(feature)
            .build();

    authBuilder = client.target(twitterSettings.getBaseUrl())
            .path(twitterSettings.getOauth2TokenEndpoint())
            .request(MediaType.APPLICATION_JSON_TYPE)
            .header("Authorization", authHeader)
            .header("Content-Type", twitterSettings.getContentTypeHeader());

    sampleBuilder = client.target(twitterSettings.getBaseUrl())
            .path("/1.1/search/tweets.json")
            .queryParam("q", "flu")
            .queryParam("geocode", "51.538000,-0.115000,10mi")
            .queryParam("include_entities", "false")
            .queryParam("include_user_entities", "false")
            .queryParam("until", twitterSettings.calculateUntilDate())
            .queryParam("lang", "en")
            .queryParam("result_type", "recent")
            .queryParam("count", "100")
            .request(MediaType.APPLICATION_JSON_TYPE);

  }

  public void onEvent(@Observes @Twitter Long timerLong) {

    if (nodeCache.hasTransactionCode(ServiceTag.TWITTER, timerLong)) {
      return;
    }

    String token = nodeCache.getValue(CacheKey.TWITTER_BEARER_TOKEN)
            .orElse(obtainBearerToken());

    System.out.println("in public #onEvent() " + token);

    Response response = sampleBuilder.header("Authorization", "Bearer " + token)
            .get();

    if (response.getStatus() == Response.Status.OK.getStatusCode()) {
      String entityString = response.readEntity(String.class);

      // Parse JSON to validate
      store.save(entityString, timerLong, ServiceTag.TWITTER);
    }

  }

  private String obtainBearerToken() {
    Response response = authBuilder.post(Entity.entity(authForm, MediaType.APPLICATION_FORM_URLENCODED_TYPE));

    if (response.getStatus() == Response.Status.OK.getStatusCode()) {
      String entityString = response.readEntity(String.class);
      TwitterAuthEntity entity = JSONB.fromJson(entityString, TwitterAuthEntity.class);
      nodeCache.setValue(CacheKey.TWITTER_BEARER_TOKEN, entity.getAccessToken());
      return entity.getAccessToken();
    } else {
      throw new IllegalStateException("Cannot obtain bearer token from Twitter API");
    }

  }

}
