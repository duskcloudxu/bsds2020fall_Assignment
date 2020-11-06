package com.bsds2020fall.assignment1;

import com.bsds2020fall.assignment1.model.RequestRecord;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.SkiersApi;
import io.swagger.client.model.LiftRide;
import io.swagger.client.model.SkierVertical;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorkerThread extends Thread {

  private int[] skierIDRange;
  private int[] lifterIDRange;
  private int[] timeRange;
  private int numPost;
  private int numGet;
  private Phase phase;
  private SkiersApi skiersApi;
  private Logger log = LogManager.getLogger();
  private ArrayList<RequestRecord>reqList;
  private int numFailedRequest = 0;
  private int numSucceedRequest = 0;
  private CountDownLatch doneSignal = null;
  private CountDownLatch partialDS = null;


  public WorkerThread(int[] skierIDRange, int[] timeRange ,int numPost,
    int numGet, Phase phase, CountDownLatch doneSignal,CountDownLatch partialDS) {
    this.skierIDRange = skierIDRange;
    this.timeRange = timeRange;
    this.numPost = numPost;
    this.numGet = numGet;
    this.phase = phase;
    this.reqList = new ArrayList<>();
    this.doneSignal = doneSignal;
    this.partialDS = partialDS;


    // initialize lifeIDRange
    this.lifterIDRange=new int[]{0,Setting.getNumLift()};

    // initialize API
    String SERVER_URL = Setting.getBaseURL();
    skiersApi = new SkiersApi();
    ApiClient client = skiersApi.getApiClient();
    client.setBasePath(SERVER_URL);

  }

  private int randomInRange(int[] range) {
    return (int) (Math.random() * (range[1] - range[0]) + range[0]);
  }

  private void post() {

    // timestamp at start
    LocalTime startTime=LocalTime.now();
    // initialize post body
    LiftRide liftRide = new LiftRide();
    liftRide.setResortID(Setting.getResortID());
    liftRide.setDayID(Integer.toString(Setting.getDayID()));
    liftRide.setLiftID(Integer.toString(randomInRange(lifterIDRange)));
    liftRide.setTime(Integer.toString(randomInRange(timeRange)));
    liftRide.setSkierID(Integer.toString(randomInRange(skierIDRange)));

    try {

      skiersApi.writeNewLiftRide(liftRide);

      // timestamp at end
      LocalTime endTime= LocalTime.now();
      Duration duration= Duration.between(startTime,endTime);
      reqList.add(new RequestRecord(startTime,"POST",duration.toMillis(),200));
      numSucceedRequest++;

    } catch (ApiException e) {

      LocalTime endTime= LocalTime.now();
      Duration duration= Duration.between(startTime,endTime);
      reqList.add(new RequestRecord(startTime,"POST",duration.toMillis(),e.getCode()));
      numFailedRequest++;
      log.error("POST FAILED "+e.getMessage());
    }
  }

  private void get(String type) {

    // timestamp at start
    LocalTime startTime=LocalTime.now();
    String dayIDStr = Integer.toString(Setting.getDayID());
    String randomSkierIDStr = Integer.toString(randomInRange(skierIDRange));
    try {
      if(type.equals("vertSkiDay")){
        SkierVertical t = skiersApi
          .getSkierDayVertical(Setting.getResortID(), dayIDStr, randomSkierIDStr);
      }
      else{
        List<String> resorts = new ArrayList<>();
        resorts.add(Setting.getResortID());
        SkierVertical t = skiersApi
          .getSkierResortTotals(randomSkierIDStr,resorts);
      }

      // timestamp at end
      LocalTime endTime= LocalTime.now();
      Duration duration= Duration.between(startTime,endTime);
      reqList.add(new RequestRecord(startTime,"GET",duration.toMillis(),200));
      numSucceedRequest++;
    } catch (ApiException e) {

      //timestamp at end
      LocalTime endTime= LocalTime.now();
      Duration duration= Duration.between(startTime,endTime);
      reqList.add(new RequestRecord(startTime,"GET",duration.toMillis(),e.getCode()));
      numFailedRequest++;
      log.error("GET FAILED "+e.getMessage());
    }
  }

  public void run() {
    for(int i=0;i< numPost;i++){
      post();
    }
    if(phase==Phase.CLOSING){
      for (int i = 0; i < numGet; i++) {
        get("vertSkiDay");
      }
      for (int i = 0; i < numGet; i++) {
        get("vertSki");
      }

    }
    else {
      for (int i = 0; i < numGet; i++) {
        get("vertSkiDay");
      }
    }
    doneSignal.countDown();
    partialDS.countDown();
    Counter.getInstance().addSuccessfulReq(numSucceedRequest);
    Counter.getInstance().addFailedReq(numFailedRequest);
    Counter.getInstance().addResRecord(reqList);
    Counter.getInstance().finishThead(phase);
  }

}
