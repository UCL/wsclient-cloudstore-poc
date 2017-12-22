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
package ucl.ircflagship2.wsclient.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.SortedMap;
import java.util.TreeMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author David Guzman <d.guzman at ucl.ac.uk>
 */
public class SignatureTest {

  @Test
  public void testSignInstagramCall() throws NoSuchAlgorithmException, InvalidKeyException {
    System.out.println("testSignForInstagram()");
    String expected = "260634b241a6cfef5e4644c205fb30246ff637591142781b86e2075faf1b163a";
    String endpoint = "/media/657988443280050001_25025320";
    SortedMap<String, String> parameters = new TreeMap<>();
    parameters.put("access_token", "fb2e77d.47a0479900504cb3ab4a1f626d174d2d");
    parameters.put("count", "10");
    String secret = "6dc1787668c64c939929c17683d7cb74";
    String actual = Signature.signInstagramCall(endpoint, parameters, secret);
    assertEquals(expected, actual);
  }

}
