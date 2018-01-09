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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.Serializable;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import ucl.ircflagship2.wsclient.scheduler.ServiceTag;

/**
 *
 * @author David Guzman <d.guzman at ucl.ac.uk>
 */
public class ConverterTest {

  /**
   * Test of timerInfoToString method, of class Converter.
   */
  @Test
  public void testToString() {
    System.out.println("testToString()");
    String expectedString = "INSTAGRAM";
    Serializable serialStr = ServiceTag.INSTAGRAM.toString();
    Optional<String> actual = Converter.timerInfoToString(serialStr);
    Optional<String> expected = Optional.of(expectedString);
    assertEquals(expected, actual, "Converter.timerInfoToString(serialStr) should return Optional.of(\"INSTAGRAM\")");
  }

  @Test
  public void testEncodeRfc1738() {
    System.out.println("testEncodeRfc1738()");
    String expectedString = "L8qq9PZyRg6ieKGEKhZolGC0vJWLw8iEJ88DRdyOg";
    String inputString = "L8qq9PZyRg6ieKGEKhZolGC0vJWLw8iEJ88DRdyOg";
    Optional<String> actual = Converter.encodeRfc1738(inputString);
    Optional<String> expected = Converter.encodeRfc1738(expectedString);
    assertEquals(expected, actual, "Converter.encodeRfc1738(inputStr) should not change String");
  }

}
