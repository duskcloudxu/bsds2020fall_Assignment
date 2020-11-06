package com.bsds2020fall.assignment1;

import static org.junit.Assert.*;

import io.swagger.client.model.LiftRide;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.junit.Test;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.SkiersApi;
import io.swagger.client.model.LiftRide;
import io.swagger.client.model.SkierVertical;

public class WorkerThreadTest {

  @Test
  public void CRUDTest() {

    SkiersApi skiersApi = new SkiersApi();
    ApiClient client = skiersApi.getApiClient();
    client.setBasePath(Setting.getBaseURL());
    LiftRide liftRide = new LiftRide();
    liftRide.setResortID(Setting.getResortID());
    liftRide.setDayID(Integer.toString(Setting.getDayID()));
    liftRide.setLiftID(Integer.toString(44));
    liftRide.setTime(Integer.toString(2));
    liftRide.setSkierID(Integer.toString(9));

    try {
      skiersApi.writeNewLiftRide(liftRide);
      SkierVertical t = skiersApi
        .getSkierDayVertical(Setting.getResortID(), "1", "9");
      System.out.println(t.toString());
      List<String> temp = new ArrayList<>();
      temp.add(Setting.getResortID());
      SkierVertical t1 = skiersApi
        .getSkierResortTotals("8",temp);
      System.out.println(t1.toString());
    } catch (ApiException e) {
      System.out.println(e.getResponseBody());
      e.printStackTrace();
    }
  }
  @Test
  public void threadTest() throws InterruptedException, IOException {
    int[] numSkierRange ={22,144};
    int[] timeRange = {1, 90};
    CountDownLatch doneSignal = new CountDownLatch(1);
    CountDownLatch partialDS = new CountDownLatch(0);
    WorkerThread workerThread = new WorkerThread(numSkierRange, timeRange, 30 , 5, Phase.START_UP,doneSignal,partialDS);
    workerThread.start();
    while(Counter.getInstance().getCompleteNum(Phase.START_UP)<1){
      Thread.sleep(100);
    }
    System.out.println("mean latency for POST");
    System.out.println(Counter.getInstance().getMeanOfPostRequest()+"ms");

    System.out.println("mean latency for GET");
    System.out.println(Counter.getInstance().getMeanOfGetRequest()+"ms");


    System.out.println("median latency for POST");
    System.out.println(Counter.getInstance().getMedianOfPostRequest()+"ms");

    System.out.println("median latency for GET");
    System.out.println(Counter.getInstance().getMedianOfGetRequest()+"ms");



    System.out.println("P99 for POST");
    System.out.println(Counter.getInstance().getP99OfPostRequest()+"ms");

    System.out.println("P99 for GET");
    System.out.println(Counter.getInstance().getP99OfGetRequest()+"ms");

    System.out.println("Max Latency for POST");
    System.out.println(Counter.getInstance().getMaxLatencyOfPostRequest()+"ms");

    System.out.println("Max Latency for GET");
    System.out.println(Counter.getInstance().getMaxLatencyGetRequest()+"ms");

  }
}
