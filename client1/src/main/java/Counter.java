import java.util.concurrent.atomic.AtomicInteger;

public class Counter {

  private static Counter counter=new Counter();
  private AtomicInteger phase1CompleteNum;
  private AtomicInteger phase2CompleteNum;
  private AtomicInteger phase3CompleteNum;
  private AtomicInteger successfulReq;
  private AtomicInteger failedReq;

  private Counter() {
    phase1CompleteNum = new AtomicInteger(0);
    phase2CompleteNum = new AtomicInteger(0);
    phase3CompleteNum = new AtomicInteger(0);
    successfulReq = new AtomicInteger(0);
    failedReq = new AtomicInteger(0);
  }

  public static Counter getInstance() {
    return counter;
  }

  /*
  *  add corresponding thread number
  * */
  public void finishThead(Phase phase) {
    switch (phase) {
      case PEAK: {
        phase2CompleteNum.addAndGet(1);
        break;
      }
      case CLOSING: {
        phase3CompleteNum.addAndGet(1);
        break;
      }
      case START_UP: {
        phase1CompleteNum.addAndGet(1);
        break;
      }
    }
  }

  /*
  *  get number of finished thread of target phase
  * */
  public int getCompleteNum(Phase phase) {
    switch (phase) {
      case PEAK: {
        return phase2CompleteNum.get();
      }
      case CLOSING: {
        return phase3CompleteNum.get();
      }
      case START_UP: {
        return phase1CompleteNum.get();
      }
    }
    return -1;
  }

  /*
  *  add successfulReq by 1
  * */
  public void addSuccessfulReq(){
    successfulReq.addAndGet(1);
  }

  /*
   *  get successfulReq number
   * */
  public int getSuccessfulReq(){
    return successfulReq.get();
  }


  /*
   *  add failedReq by 1
   * */
  public void addFailedReq(){
    failedReq.addAndGet(1);
  }

  /*
   *  get failedReq number
   * */
  public int getFailedReq(){
    return failedReq.get();
  }





}
