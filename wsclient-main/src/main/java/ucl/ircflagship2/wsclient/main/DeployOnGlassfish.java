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
package ucl.ircflagship2.wsclient.main;

import java.io.File;
import java.net.URISyntaxException;
import org.glassfish.embeddable.Deployer;
import org.glassfish.embeddable.GlassFish;
import org.glassfish.embeddable.GlassFishException;
import org.glassfish.embeddable.GlassFishRuntime;

/**
 *
 * @author David Guzman <d.guzman at ucl.ac.uk>
 */
public class DeployOnGlassfish {

  /**
   * @param args the command line arguments
   * @throws org.glassfish.embeddable.GlassFishException
   * @throws java.net.URISyntaxException
   */
  public static void main(String[] args) throws GlassFishException,
          URISyntaxException {

    GlassFish glassfish = GlassFishRuntime.bootstrap().newGlassFish();
    glassfish.start();

    Deployer deployer = glassfish.getDeployer();

    for (String arg : args) {
      String ejbref[] = arg.split("=");
      File ejbjar = new File(ejbref[1]);
      deployer.deploy(ejbjar, "--name=" + ejbref[0], "--contextroot=" + ejbref[0], "--force=true");
    }
    //glassfish.stop();
  }

}
