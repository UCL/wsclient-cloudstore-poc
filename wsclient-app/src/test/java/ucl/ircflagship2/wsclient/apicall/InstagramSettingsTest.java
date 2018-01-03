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

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author David Guzman <d.guzman at ucl.ac.uk>
 */
public class InstagramSettingsTest {

  @Test
  public void testQueryParameters() {
    System.out.println("testQueryParameters()");
    String expected = "access_token=footoken&distance=5000&lat=51.538000&lng=-0.115000";
    InstagramSettings instance = new InstagramSettings();
    String actual = instance.queryParameters();
    assertEquals(expected, actual, "#instance.queryParameters() should return 4 parameters in alphabetical order separated by & ");
  }

  @Test
  public void testGetSignature() {
    System.out.println("testGetSignature()");
    String expected = "a4c20ef35ab3fe30cb06efc2b039329bca71971bb765dd5658c084cf55103b0a";
    InstagramSettings instance = new InstagramSettings();
    String actual = instance.getSignature().get();
    assertEquals(expected, actual, "#instance.getSignature() should return the right signature based on \"barsecret\" as secret");
  }

}
