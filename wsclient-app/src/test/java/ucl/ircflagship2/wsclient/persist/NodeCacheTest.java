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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoField;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import ucl.ircflagship2.wsclient.scheduler.ServiceTag;

/**
 *
 * @author David Guzman <d.guzman at ucl.ac.uk>
 */
public class NodeCacheTest {

  @Test
  public void testTransactionCode() {
    System.out.println("testTransactionCode()");
    NodeCache instance = new NodeCache();
    Long timestamp = LocalDateTime.of(LocalDate.now(), LocalTime.MIN)
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli();
    Long anHourLater = timestamp + 3600000L;
    boolean beforeFirst = instance.hasTransactionCode(ServiceTag.TWITTER, timestamp);
    assertFalse(beforeFirst);
    boolean firstAdd = instance.addTransactionCode(ServiceTag.TWITTER, timestamp);
    assertTrue(firstAdd);
    boolean afterFirst = instance.hasTransactionCode(ServiceTag.TWITTER, timestamp);
    boolean afterFirstLater = instance.hasTransactionCode(ServiceTag.TWITTER, anHourLater);
    assertTrue(afterFirst);
    assertTrue(afterFirstLater);
    boolean addAgain = instance.addTransactionCode(ServiceTag.TWITTER, timestamp);
    assertFalse(addAgain);
  }

}
