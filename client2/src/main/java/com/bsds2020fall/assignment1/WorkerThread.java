package com.bsds2020fall.assignment1;

import com.bsds2020fall.assignment1.model.RequestRecord;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.SkiersApi;
import io.swagger.client.model.LiftRide;
import io.swagger.client.model.SkierVertical;
import java.time.Duration;
import java.time.LocalTime;
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


  public WorkerThread(int[] skierIDRange, int[] timeRange ,int numPost,
    int numGet, Phase phase) {
    this.skierIDRange = skierIDRange;
    this.timeRange = timeRange;
    this.numPost = numPost;
    this.numGet = numGet;
    this.phase = phase;

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

    try {

      skiersApi.writeNewLiftRide(liftRide);

      // timestamp at end
      LocalTime endTime= LocalTime.now();
      Duration duration= Duration.between(startTime,endTime);
      Counter.getInstance().addResRecord(new RequestRecord(startTime,"POST",duration.toMillis(),200));

      Counter.getInstance().addSuccessfulReq();

    } catch (ApiException e) {

      LocalTime endTime= LocalTime.now();
      Duration duration= Duration.between(startTime,endTime);
      Counter.getInstance().addResRecord(new RequestRecord(startTime,"POST",duration.toMillis(),e.getCode()));
      Counter.getInstance().addFailedReq();
      log.error("POST FAILED "+e.getMessage());
    }
  }

  private void get() {

    // timestamp at start
    LocalTime startTime=LocalTime.now();
    String dayIDStr = Integer.toString(Setting.getDayID());
    String randomSkierIDStr = Integer.toString(randomInRange(skierIDRange));
    try {
      SkierVertical t = skiersApi
        .getSkierDayVertical(Setting.getResortID(), dayIDStr, randomSkierIDStr);

      // timestamp at end
      LocalTime endTime= LocalTime.now();
      Duration duration= Duration.between(startTime,endTime);
      Counter.getInstance().addResRecord(new RequestRecord(startTime,"GET",duration.toMillis(),200));

      Counter.getInstance().addSuccessfulReq();
    } catch (ApiException e) {

      //timestamp at end
      LocalTime endTime= LocalTime.now();
      Duration duration= Duration.between(startTime,endTime);
      Counter.getInstance().addResRecord(new RequestRecord(startTime,"GET",duration.toMillis(),e.getCode()));

      Counter.getInstance().addFailedReq();
      log.error("GET FAILED "+e.getMessage());
    }
  }

  public void run() {
    for(int i=0;i< numPost;i++){
      post();
    }
    for (int i = 0; i < numGet; i++) {
      get();
    }
    Counter.getInstance().finishThead(phase);
  }

}
