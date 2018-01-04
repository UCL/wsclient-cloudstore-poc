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
package ucl.ircflagship2.wsclient.log;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;
import javax.ejb.Timer;
import javax.interceptor.InvocationContext;
import mockit.Expectations;
import mockit.Mocked;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author David Guzman <d.guzman at ucl.ac.uk>
 */
public class FireEventInterceptorTest {

  private static final Logger LOGGER = Logger.getLogger("EventLogger");
  private OutputStream outputStream;
  private StreamHandler streamHandler;

  @Mocked
  private InvocationContext invocationContext;

  @Mocked
  private Timer timer;

  @BeforeAll
  public static void setupLogger() {
    System.setProperty("java.util.logging.SimpleFormatter.format",
            "%4$s %5$s%6$s");
  }

  @BeforeEach
  public void setup() {
    outputStream = new ByteArrayOutputStream();
    Handler[] handlers = LOGGER.getParent().getHandlers();
    streamHandler = new StreamHandler(outputStream, handlers[0].getFormatter());
    LOGGER.addHandler(streamHandler);
  }

  @Test
  public void testInterceptFiring() throws Exception {
    System.out.println("testInterceptFiring()");
    FireEventInterceptor instance = new FireEventInterceptor();
    new Expectations() {
      {
        timer.getInfo();
        result = "Instagram";

        invocationContext.getTimer();
        result = timer;
      }
    };
    instance.interceptFiring(invocationContext);
    streamHandler.flush();
    String expected = "INFO Calling fireEvent() - Instagram";
    String actual = outputStream.toString();
    assertEquals(expected, actual, "#instance.interceptFiring() should log Instagram");
  }

}
