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

import java.io.File;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.ResponseProcessingException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import ucl.ircflagship2.wsclient.persist.CacheKey;
import ucl.ircflagship2.wsclient.persist.NodeCache;

/**
 *
 * @author David Guzman <d.guzman at ucl.ac.uk>
 */
@Stateless
@LocalBean
public class MsGraphCall extends BaseCall {

  private final Client client;
  private Invocation.Builder authBuilder;
  private WebTarget uploadTarget;
  private Form authForm = new Form();
  private final Jsonb JSONB = JsonbBuilder.create();

  @Inject
  private MsGraphSettings msGraphSettings;

  @EJB
  private NodeCache nodeCache;

  public MsGraphCall() {
    client = ClientBuilder.newBuilder()
            .register(feature)
            .register(MultiPartFeature.class)
            .build();
  }

  @PostConstruct
  public void init() {
    // initial Refresh Token
    nodeCache.setValue(CacheKey.MSGRAPH_REFRESH_TOKEN, msGraphSettings.getMsGraphRefresh());

    authForm = authForm.param("client_id", msGraphSettings.getMsAppId())
            .param("scope", "offline_access files.read.all files.readwrite.all")
            .param("refresh_token", nodeCache.getValue(CacheKey.MSGRAPH_REFRESH_TOKEN).get())
            .param("redirect_uri", msGraphSettings.getRedirectUri())
            .param("grant_type", "refresh_token")
            .param("client_secret", msGraphSettings.getMsAppSecret());

    authBuilder = client.target(msGraphSettings.getAuthBaseUrl())
            .path(msGraphSettings.getAuthPath())
            .request(MediaType.APPLICATION_JSON_TYPE)
            .header("Content-Type", "application/x-www-form-urlencoded");

    uploadTarget = client.target(msGraphSettings.getMsGraphBaseUrl())
            .path(msGraphSettings.getUploadPath());

  }

  public String upload(final File fileToUpload) {
    FileDataBodyPart fileBodyPart = new FileDataBodyPart(
            "file",
            fileToUpload,
            MediaType.APPLICATION_OCTET_STREAM_TYPE
    );

    MultiPart multiPart = new FormDataMultiPart()
            .bodyPart(fileBodyPart);

    Entity fileEntity = Entity.entity(multiPart, multiPart.getMediaType());

    Response response = uploadTarget.resolveTemplate("filename", "graph/" + fileToUpload.getName())
            .request(MediaType.APPLICATION_JSON_TYPE)
            .header("Authorization", obtainAccessToken())
            .put(fileEntity);

    if (response.getStatus() != Response.Status.CREATED.getStatusCode()) {
      throw new ResponseProcessingException(response, "Failed to upload to OneDrive");
    }

    // Return or push a message with the id of the drive item as returned in the response
    return Response.Status.CREATED.toString();
  }

  private String obtainAccessToken() {
    Entity<Form> entity = Entity.entity(authForm, MediaType.APPLICATION_FORM_URLENCODED_TYPE);
    Response response = authBuilder.post(entity);

    if (response.getStatus() == Response.Status.OK.getStatusCode()) {
      String entityString = response.readEntity(String.class);
      MsGraphAuthEntity authEntity = JSONB.fromJson(entityString, MsGraphAuthEntity.class);
      nodeCache.setValue(CacheKey.MSGRAPH_REFRESH_TOKEN, authEntity.getRefreshToken());
      return "Bearer " + authEntity.getAccessToken();
    } else {
      throw new IllegalStateException("Cannot obtain bearer token from Microsoft Login API");
    }

  }

}
