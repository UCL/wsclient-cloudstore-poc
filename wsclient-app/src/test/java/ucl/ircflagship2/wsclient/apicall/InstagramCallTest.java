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

import java.util.Optional;
import java.util.TreeMap;
import javax.ws.rs.client.WebTarget;
import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Injectable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author David Guzman <d.guzman at ucl.ac.uk>
 */
public class InstagramCallTest {

  private InstagramCall instagramCall;

  @Injectable
  private InstagramSettings settings;

  @BeforeEach
  public void setUp() {
    instagramCall = new InstagramCall();
    Deencapsulation.setField(instagramCall, settings);

    new Expectations() {
      {
        settings.getBaseUrl();
        result = "http://localhost:8080/_mock";

        settings.getEndpoint();
        result = "mockEndpoint";

        settings.getParameterMap();
        result = new TreeMap<String, String>() {
          {
            put("a", "b");
          }
        };

        settings.getSignature();
        result = Optional.of("mockSignature");

        settings.getSignatureKey();
        result = "sig";
      }
    };
    instagramCall.init();
  }

  @Test
  public void testWebTarget() {
    System.out.println("testWebTarget()");
    WebTarget webTarget = Deencapsulation.getField(instagramCall, WebTarget.class);
    assertEquals("/_mock/mockEndpoint", webTarget.getUri().getPath(), "webTarget path should be _mock/mockEndpoint");
    assertEquals("localhost", webTarget.getUri().getHost(), "webTarget path should be localhost");
    // TODO: assert for query parameters
  }

}
