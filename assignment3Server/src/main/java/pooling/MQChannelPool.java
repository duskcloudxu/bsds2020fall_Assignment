package pooling;

import com.rabbitmq.client.Channel;
import org.apache.commons.pool2.impl.GenericObjectPool;

public class MQChannelPool {
  private  static MQChannelPool instance = null;
  private GenericObjectPool<Channel> pool = null;

  private static int MAX_OBJECT_NUM = 50;
  private static boolean IS_BLOCK = true;
  private static int MAX_WAIT_TIME_IN_SEC = 10;

  private MQChannelPool(){
    pool = new GenericObjectPool<>(new MQChannelFactory());
    pool.setMaxTotal(MAX_OBJECT_NUM);
    pool.setBlockWhenExhausted(IS_BLOCK);
    pool.setMaxTotal(MAX_WAIT_TIME_IN_SEC*1000);
  }

  public static MQChannelPool getInstance(){
    if(instance == null){
      instance = new MQChannelPool();
    }
    return instance;
  }

  public Channel borrowObject() throws Exception {
    return pool.borrowObject();
  }

  public void returnObject(Channel channel) {
    pool.returnObject(channel);
  }

}
