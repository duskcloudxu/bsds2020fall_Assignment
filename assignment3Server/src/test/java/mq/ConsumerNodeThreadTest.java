package mq;

import static org.junit.Assert.*;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.junit.Test;
import pooling.MQChannelPool;

public class ConsumerNodeThreadTest {

  @Test
  public void main() {

    try {
      MQChannelPool mqChannelPool = MQChannelPool.getInstance();
      Channel channel = mqChannelPool.borrowObject();
      channel.queueDeclare(Setting.QUEUE_NAME, true, false, false, null);

      String jsonMsg = "{\n"
        + "    \"resortID\":\"Mission Ridge\",\n"
        + "    \"dayID\":4,\n"
        + "    \"skierID\":1997,\n"
        + "    \"time\":131,\n"
        + "    \"liftID\":13\n"
        + "}";
      channel.basicPublish("", Setting.QUEUE_NAME, null, jsonMsg.getBytes());
      mqChannelPool.returnObject(channel);
      System.out.println(" [x] Sent");
    }catch (Exception e){
      e.printStackTrace();
    }
  }
}
