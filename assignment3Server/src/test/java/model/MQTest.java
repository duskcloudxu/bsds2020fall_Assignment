package model;

import static org.junit.Assert.*;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import org.junit.Test;

public class MQTest {
  @Test
  public void  testInsert(){
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    try (Connection connection = factory.newConnection();
      Channel channel = connection.createChannel()) {
      channel.queueDeclare("testQueue", false, false, false, null);
      String message = "Hello World!";
      channel.basicPublish("", "testQueue", null, message.getBytes());
      System.out.println(" [x] Sent '" + message + "'");
    }catch (Exception e){
      e.printStackTrace();
    }
  }
  @Test
  public void  testReceive() throws IOException, TimeoutException {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.queueDeclare("testQueue", false, false, false, null);
    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
      String message = new String(delivery.getBody(), "UTF-8");
      System.out.println(" [x] Received '" + message + "'");
    };
    channel.basicConsume("testQueue", true, deliverCallback, consumerTag -> { });
  }



}
