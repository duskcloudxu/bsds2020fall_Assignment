package listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import mq.ConsumerNodeThread;

@WebListener
public class Listener implements ServletContextListener {
  static final int CONSUMER_NUM = 10;
  @Override
  public void contextInitialized(ServletContextEvent sce) {
    for (int i = 0; i < CONSUMER_NUM; i++) {
      ConsumerNodeThread consumerNodeThread = new ConsumerNodeThread();
      consumerNodeThread.start();
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    System.out.println("application shut down");

  }
}
