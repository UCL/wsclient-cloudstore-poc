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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ucl.ircflagship2.wsclient.events.Instagram;
import ucl.ircflagship2.wsclient.persist.ObjectStore;

/**
 *
 * @author David Guzman <d.guzman at ucl.ac.uk>
 */
@Stateless
@LocalBean
public class InstagramCall extends BaseCall {

  private Client client;
  private WebTarget webTarget;

  @Inject
  private InstagramSettings settings;

  @EJB
  private ObjectStore objectStore;

  @PostConstruct
  public void init() {

    client = ClientBuilder.newBuilder()
            .register(feature)
            .build();

    webTarget = client.target(settings.getBaseUrl())
            .path(settings.getEndpoint());

    settings.getParameterMap().entrySet().forEach((Map.Entry<String, String> entry) -> {
      webTarget = webTarget.queryParam(entry.getKey(), entry.getValue());
    });

    settings.getSignature().ifPresent((String s) -> {
      webTarget = webTarget.queryParam(settings.getSignatureKey(), s);
    });

  }

  public void onEvent(@Observes @Instagram Long timerLong) {

    Response response = webTarget.request(MediaType.APPLICATION_JSON).get();

    if (response.getStatus() == Response.Status.OK.getStatusCode()) {

      String entityString = response.readEntity(String.class);

      if (!entityString.isEmpty()) {
        InputStream inputStream = new ByteArrayInputStream(entityString.getBytes());
        // Persist to object store
        objectStore.save(inputStream, timerLong);
      }

    }

  }

  @PreDestroy
  public void close() {
    client.close();
  }

}
