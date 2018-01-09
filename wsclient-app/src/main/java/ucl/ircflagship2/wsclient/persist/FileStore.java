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
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import ucl.ircflagship2.wsclient.apicall.MsGraphCall;
import ucl.ircflagship2.wsclient.scheduler.ServiceTag;

/**
 *
 * @author David Guzman <d.guzman at ucl.ac.uk>
 */
@Stateless
@LocalBean
public class FileStore {

  private final Map<String, String> zipenv = new HashMap<>();

  @EJB
  private MsGraphCall msGraphCall;

  @PostConstruct
  public void init() {
    zipenv.put("create", "true");
  }

  public void save(InputStream inputStream, Long timestamp) {

  }

  public void save(String jsonString, Long timestamp, ServiceTag tag) {

    URI fsUri = createUri("jar:file:%s/%s-%s.zip", tag, timestamp);

    URI jsonUri = createUri("%s/%s-%s.json", tag, timestamp);

    try (FileSystem zipfs = FileSystems.newFileSystem(fsUri, zipenv)) {

      Path jsonPath = Paths.get(jsonUri.toString());

      Files.write(jsonPath, jsonString.getBytes(), StandardOpenOption.CREATE);

      Path jsonInZipPath = zipfs.getPath(String.format("/%s-%s.json",
              tag.toString().toLowerCase(),
              timestamp.toString()));

      Files.copy(jsonPath, jsonInZipPath, StandardCopyOption.REPLACE_EXISTING);

      Files.delete(jsonPath);

    } catch (IOException ex) {
      throw new UncheckedIOException(ex);
    }

    URI zipUri = createUri("file:%s/%s-%s.zip", tag, timestamp);

    msGraphCall.upload(new File(zipUri));

    try {
      Files.delete(Paths.get(zipUri));
    } catch (IOException ex) {
      throw new UncheckedIOException(ex);
    }

  }

  private URI createUri(final String fmt, final ServiceTag tag, final Long timestamp) {
    return URI.create(String.format(fmt,
            System.getProperty("java.io.tmpdir"),
            tag.toString().toLowerCase(),
            timestamp.toString()));
  }

}
