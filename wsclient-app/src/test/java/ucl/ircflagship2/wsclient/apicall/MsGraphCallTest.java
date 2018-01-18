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
import java.io.IOException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.ResponseProcessingException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mock;
import mockit.MockUp;
import mockit.Verifications;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

/**
 *
 * @author David Guzman <d.guzman at ucl.ac.uk>
 */
public class MsGraphCallTest {

  @Test
  public void testUpload(@Injectable WebTarget webTarget,
          @Injectable Response response, @Injectable Invocation.Builder builder) throws IOException {
    System.out.println("testUpload()");
    new MockUp<MsGraphCall>() {
      @Mock
      void $init() {
      }

      @Mock
      private String obtainAccessToken() {
        return "TEST";
      }
    };
    MsGraphCall instance = new MsGraphCall();
    Deencapsulation.setField(instance, webTarget);
    new Expectations() {
      {
        webTarget.resolveTemplate(anyString, any)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(anyString, any)
                .put((Entity) any);
        result = response;

        response.getStatus();
        result = Response.Status.CREATED.getStatusCode();
      }
    };
    File file = File.createTempFile("test", "json");
    String actualCreated = instance.upload(file);
    assertEquals("Created", actualCreated);

    new Expectations() {
      {
        response.getStatus();
        result = Response.Status.BAD_REQUEST.getStatusCode();
      }
    };
    ResponseProcessingException ex = assertThrows(ResponseProcessingException.class,
            () -> instance.upload(file));
    assertEquals("Failed to upload to OneDrive", ex.getMessage());
    new Verifications() {
      {
        file.delete();
      }
    };
  }

}
