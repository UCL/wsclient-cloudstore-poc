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
package ucl.ircflagship2.wsclient.scheduler;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import ucl.ircflagship2.wsclient.events.Instagram;
import ucl.ircflagship2.wsclient.events.Twitter;
import ucl.ircflagship2.wsclient.log.FireEventInterceptor;
import ucl.ircflagship2.wsclient.util.Converter;

/**
 *
 * @author David Guzman <d.guzman at ucl.ac.uk>
 */
@Singleton
@LocalBean
@Startup
@Interceptors(FireEventInterceptor.class)
public class CallTimer {

  @Resource
  private TimerService timerService;

  @Resource(name = "instagramInterval")
  private Integer instagramInterval = 10000;

  @Resource(name = "twitterInterval")
  private Integer twitterInterval = 10000;

  @Inject
  @Instagram
  private Event<Long> callInstagram;

  @Inject
  @Twitter
  private Event<Long> callTwitter;

  @PostConstruct
  public void initialise() {
    timerService.createTimer(0, instagramInterval, ServiceTag.INSTAGRAM.toString());
    timerService.createTimer(0, twitterInterval, ServiceTag.TWITTER.toString());
  }

  @Timeout
  public void fireEvent(Timer timer) {
    Converter.timerInfoToString(timer.getInfo()).ifPresent((String s) -> {
      ServiceTag tag = ServiceTag.valueOf(s);
      switch (tag) {
        case INSTAGRAM:
          callInstagram.fire(timer.getNextTimeout().getTime());
          break;
        case TWITTER:
          callTwitter.fire(timer.getNextTimeout().getTime());
          break;
      }
    });

  }

}
