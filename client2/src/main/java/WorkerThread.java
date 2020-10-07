import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.SkiersApi;
import io.swagger.client.model.LiftRide;
import io.swagger.client.model.SkierVertical;
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
    try {
      LiftRide liftRide = new LiftRide();
      liftRide.setResortID(Setting.getResortID());
      liftRide.setDayID(Integer.toString(Setting.getDayID()));
      liftRide.setLiftID(Integer.toString(randomInRange(lifterIDRange)));
      liftRide.setTime(Integer.toString(randomInRange(timeRange)));
      skiersApi.writeNewLiftRide(liftRide);
      Counter.getInstance().addSuccessfulReq();

    } catch (ApiException e) {
      Counter.getInstance().addFailedReq();
      log.error("POST FAILED "+e.getMessage());
    }
  }

  private void get() {
    try {
      String dayIDStr = Integer.toString(Setting.getDayID());
      String randomSkierIDStr = Integer.toString(randomInRange(skierIDRange));
      SkierVertical t = skiersApi
        .getSkierDayVertical(Setting.getResortID(), dayIDStr, randomSkierIDStr);
      Counter.getInstance().addSuccessfulReq();
    } catch (Exception e) {
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
