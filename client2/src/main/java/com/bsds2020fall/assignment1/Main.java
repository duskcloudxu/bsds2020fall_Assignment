package com.bsds2020fall.assignment1;
import io.swagger.client.ApiException;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;


public class Main {

  /*
  *  parse commandline parameters
  * */
  public static void main(String[] args) throws InterruptedException, ApiException, IOException {
    CommandLineParser parser= new DefaultParser();

    // create the Options
    Options options = new Options();
    options.addOption("T","threads",true,"max numer of threads to run");
    options.addOption("S","skiers",true,"number of skier to generate lift rides for");
    options.addOption("L","lifts",true,"number of ski lifts");
    options.addOption("D","day",true,"the ski day number");
    options.addOption("R","resort",true,"the resort name which is the resortID");
    options.addOption("U","url",true,"IP/port address of the server");
    // add option to setting
    try{
      CommandLine line=parser.parse(options,args);
      if(line.hasOption("T")){
        Setting.setNumThread(Integer.parseInt(line.getOptionValue("T")));
      }
      if(line.hasOption("S")){
        Setting.setNumSkier(Integer.parseInt(line.getOptionValue("S")));
      }
      if(line.hasOption("L")){
        Setting.setNumLift(Integer.parseInt(line.getOptionValue("L")));
      }
      if(line.hasOption("D")){
        Setting.setDayID(Integer.parseInt(line.getOptionValue("D")));
      }
      if(line.hasOption("R")){
        Setting.setResortID(line.getOptionValue("R"));
      }
      if(line.hasOption("U")){
        Setting.setBaseURL(line.getOptionValue("U"));
      }
    }catch (ParseException e){
      System.out.println("Unexpected exception"+e.getMessage());
      return;
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }
    execute();

  }

  /*
  *  main stage of program, create threads and monitor the process
  * */
  public static void execute() throws ApiException, InterruptedException, IOException {
    // initialize parameters
    int maxThread = Setting.getNumThread();
    int numStartUpThread = (int) Math.ceil(maxThread / 4.0);
    int numSkierInterval = Setting.getNumSkier() / numStartUpThread;
    LocalTime startTime = LocalTime.now();
    Counter cntInstance = Counter.getInstance();

    // start-up phase
    for (int i = 0; i < numStartUpThread; i++) {
      int[] numSkierRange = {i * numSkierInterval, (i + 1) * numSkierInterval - 1};
      if (i == numStartUpThread - 1) {
        numSkierRange[1] = Setting.getNumSkier();
      }
      int[] timeRange = {1, 90};
      WorkerThread workerThread = new WorkerThread(numSkierRange, timeRange, 100, 5,
        Phase.START_UP);
      workerThread.start();
    }

    //block until 10% finished
    while (cntInstance.getCompleteNum(Phase.START_UP) < numStartUpThread / 10) {
      Thread.sleep(100);
    }

    // peak phase
    int numPeakThread = Setting.getNumThread();
    numSkierInterval = Setting.getNumSkier() / numPeakThread;
    for (int i = 0; i < numPeakThread; i++) {
      int[] numSkierRange = {i * numSkierInterval, (i + 1) * numSkierInterval - 1};
      if (i == maxThread - 1) {
        numSkierRange[1] = Setting.getNumSkier();
      }
      int[] timeRange = {91, 360};
      WorkerThread workerThread = new WorkerThread(numSkierRange, timeRange, 100, 5, Phase.PEAK);
      workerThread.start();
    }

    //block until 10% finished
    while (Counter.getInstance().getCompleteNum(Phase.PEAK) < numPeakThread / 10) {
      Thread.sleep(100);
    }

    // closing phase
    int numClosingThread = numStartUpThread;
    numSkierInterval = Setting.getNumSkier() / numClosingThread;
    for (int i = 0; i < numClosingThread; i++) {
      int[] numSkierRange = {i * numSkierInterval, (i + 1) * numSkierInterval - 1};
      if (i == numClosingThread - 1) {
        numSkierRange[1] = Setting.getNumSkier();
      }
      int[] timeRange = {361, 420};
      WorkerThread workerThread = new WorkerThread(numSkierRange, timeRange, 100, 10,
        Phase.CLOSING);
      workerThread.start();
    }

    //block before all thread finish
    while (cntInstance.getCompleteNum(Phase.START_UP) < numStartUpThread
      || cntInstance.getCompleteNum(Phase.PEAK) < numPeakThread
      || cntInstance.getCompleteNum(Phase.CLOSING) < numClosingThread
    ) {
      Thread.sleep(100);
    }

    LocalTime endTime = LocalTime.now();
    Duration duration = Duration.between(startTime, endTime);

    //part 2
    System.out.println("mean latency for POST");
    System.out.println(Counter.getInstance().getMeanOfPostRequest()+"ms");

    System.out.println("mean latency for GET");
    System.out.println(Counter.getInstance().getMeanOfGetRequest()+"ms");


    System.out.println("median latency for POST");
    System.out.println(Counter.getInstance().getMedianOfPostRequest()+"ms");

    System.out.println("median latency for GET");
    System.out.println(Counter.getInstance().getMedianOfGetRequest()+"ms");

    System.out.println("Wall Time:");
    System.out.println(duration.getSeconds());
    System.out.println("Throughput:");
    System.out.println(
      (cntInstance.getFailedReq() + cntInstance.getSuccessfulReq()) / duration.getSeconds());
    Counter.getInstance().printRecordCSV();



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
