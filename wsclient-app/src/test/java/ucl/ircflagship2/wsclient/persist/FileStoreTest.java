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
package ucl.ircflagship2.wsclient.persist;

import java.io.File;
import javax.ws.rs.core.Response;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.junit.jupiter.api.Test;
import ucl.ircflagship2.wsclient.apicall.MsGraphCall;
import ucl.ircflagship2.wsclient.scheduler.ServiceTag;

/**
 *
 * @author David Guzman <d.guzman at ucl.ac.uk>
 */
public class FileStoreTest {

  @Tested
  private FileStore instance;

  @Injectable
  private MsGraphCall msGraphCall;

  @Test
  public void testSave() {
    System.out.println("testSave()");

    new Expectations() {
      {
        msGraphCall.upload((File) any);
        result = Response.Status.OK.toString();
      }
    };
    instance.init();
    instance.save("test", 86489364289L, ServiceTag.TWITTER);

  }

}
