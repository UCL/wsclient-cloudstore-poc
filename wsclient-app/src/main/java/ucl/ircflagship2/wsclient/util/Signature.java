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
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author David Guzman <d.guzman at ucl.ac.uk>
 */
public class Signature {

  private static final String PARAM_FORMAT = "|%s=%s";
  private static final String MAC_INSTANCE = "HmacSHA256";

  public static String signInstagramCall(
          String endpoint,
          SortedMap<String, String> paramMap,
          String secret
  ) throws NoSuchAlgorithmException, InvalidKeyException {
    String signature = endpoint;
    signature = paramMap.entrySet().stream()
            .map((e) -> String.format(PARAM_FORMAT, e.getKey(), e.getValue()))
            .reduce(signature, String::concat);
    Mac macHasher = Mac.getInstance(MAC_INSTANCE);
    macHasher.init(new SecretKeySpec(secret.getBytes(), MAC_INSTANCE));
    byte[] hash = macHasher.doFinal(signature.getBytes());
    signature = DatatypeConverter.printHexBinary(hash);

    return signature.toLowerCase();
  }

}
