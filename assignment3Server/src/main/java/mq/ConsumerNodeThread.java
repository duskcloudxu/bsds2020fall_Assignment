package mq;

import dao.LiftRidesDao;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import model.LiftRide;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConsumerNodeThread extends Thread {

  public static void main(String[] args) {
    ConsumerNodeThread consumerNodeThread = new ConsumerNodeThread();
    consumerNodeThread.start();
  }

  public void run(){

    ConnectionFactory factory = new ConnectionFactory();
    LiftRidesDao liftRideDao = new LiftRidesDao();
    Logger logger = LogManager.getLogger();
    logger.info("Start working");
    factory.setHost("localhost");
    try {
      Connection connection = factory.newConnection();
      Channel channel = connection.createChannel();

      //ensure load balance between diff consumer thread
      channel.basicQos(5);

      boolean isDurable = true;
      channel.queueDeclare(Setting.QUEUE_NAME, isDurable, false, false, null);
      System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

      DeliverCallback deliverCallback = (consumerTag, delivery) -> {
        String message = new String(delivery.getBody(), "UTF-8");
        Gson gson = new Gson();
        JsonObject jsonObj = gson.fromJson(message, JsonObject.class);
        LiftRide liftRide = new LiftRide(jsonObj);
        liftRideDao.addLiftRide(liftRide);
        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        logger.info("record added");
      };
      channel.basicConsume(Setting.QUEUE_NAME, false, deliverCallback, consumerTag -> {
      });
    }catch (Exception e){
      logger.error("Thread quited");
      e.printStackTrace();

    }
  }


}
