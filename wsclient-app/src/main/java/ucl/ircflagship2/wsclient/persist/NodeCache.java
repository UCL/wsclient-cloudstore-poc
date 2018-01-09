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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.ejb.LocalBean;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 * Cache for storage of application data obtained from remote servers, such as bearer tokens. When
 * scaling up to multiple servers, the consistency of the data will depend entirely on the
 * providers, like Twitter API.
 *
 * @author David Guzman <d.guzman at ucl.ac.uk>
 */
@Startup
@Singleton
@LocalBean
public class NodeCache {

  private final Map<CacheKey, String> cacheMap = new HashMap<>();

  @Lock(LockType.READ)
  public Optional<String> getValue(CacheKey key) {
    if (cacheMap.containsKey(key)) {
      return Optional.of(cacheMap.get(key));
    }
    return Optional.empty();
  }

  public void setValue(CacheKey key, String value) {
    cacheMap.put(key, value);
  }

}
