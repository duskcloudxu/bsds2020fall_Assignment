package com.bsds2020fall.assignment1;

import com.bsds2020fall.assignment1.model.RequestRecord;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class Counter {

  private static Counter counter = new Counter();
  private AtomicInteger phase1CompleteNum;
  private AtomicInteger phase2CompleteNum;
  private AtomicInteger phase3CompleteNum;
  private AtomicInteger successfulReq;
  private AtomicInteger failedReq;
  private ArrayList<Long> responses;
  private ArrayList<RequestRecord>records;

  private Counter() {
    phase1CompleteNum = new AtomicInteger(0);
    phase2CompleteNum = new AtomicInteger(0);
    phase3CompleteNum = new AtomicInteger(0);
    successfulReq = new AtomicInteger(0);
    failedReq = new AtomicInteger(0);
    responses = new ArrayList<>();
    records= new ArrayList<>();
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
  public void addSuccessfulReq() {
    successfulReq.addAndGet(1);
  }

  /*
   *  get successfulReq number
   * */
  public int getSuccessfulReq() {
    return successfulReq.get();
  }


  /*
   *  add failedReq by 1
   * */
  public void addFailedReq() {
    failedReq.addAndGet(1);
  }

  /*
   *  get failedReq number
   * */
  public int getFailedReq() {
    return failedReq.get();
  }

  /*
   *  add response Time
   * */
  public synchronized void addResRecord(RequestRecord requestRecord) {
    records.add(requestRecord);
  }

  /*
  *  print csv file contains records
  * */
  public void printRecordCSV() throws IOException {
    FileWriter out= new FileWriter("record_output.csv");
    String[]Headers={"startTime", "requestType","latency","responseCode"};
    try{
      CSVPrinter printer= new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(Headers));
      for(RequestRecord item:records){
        printer.printRecord(item.getStartTime(),item.getType(),item.getLatency(),item.getCode());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /*
  *  calculate mean running time for post request
  * */
  public long getMeanOfPostRequest(){
    long sumLatency=0;
    long numPost=0;
    for(RequestRecord item:records){
      if(item.getType().equals("POST")){
        sumLatency+=item.getLatency();
        numPost++;
      }
    }
    return sumLatency/numPost;
  }

  /*
   *  calculate mean running time for get request
   * */
  public long getMeanOfGetRequest(){
    long sumLatency=0;
    long numGet=0;
    for(RequestRecord item:records){
      if(item.getType().equals("GET")){
        sumLatency+=item.getLatency();
        numGet++;
      }
    }
    return sumLatency/numGet;
  }

  public ArrayList<Long>getSortedArrayofLatencyOfType(String type){

    ArrayList<Long>res=new ArrayList<>();
    for(RequestRecord item:records){
      if(item.getType().equals(type)){
        res.add(item.getLatency());
      }
    }
    Collections.sort(res);
    return res;
  }

  /*
  *  calculate median response time for POST
  * */
  public long getMedianOfPostRequest(){
    ArrayList<Long> postLatencies=getSortedArrayofLatencyOfType("POST");
    if(postLatencies.size()%2==0){
      return (postLatencies.get(postLatencies.size()/2)+postLatencies.get(postLatencies.size()/2-1))/2;
    }
    else{
      return postLatencies.get(postLatencies.size()/2);
    }
  }


  /*
   *  calculate median response time for GET
   * */
  public long getMedianOfGetRequest(){
    ArrayList<Long> getLatencies=getSortedArrayofLatencyOfType("GET");
    if(getLatencies.size()%2==0){
      return (getLatencies.get(getLatencies.size()/2)+getLatencies.get(getLatencies.size()/2-1))/2;
    }
    else{
      return getLatencies.get(getLatencies.size()/2);
    }
  }

  /*
  *  calculate P99 of POST
  * */
  public long getP99OfPostRequest(){
    ArrayList<Long> postLatencies=getSortedArrayofLatencyOfType("POST");
    int targetIndex=postLatencies.size()-(int)(postLatencies.size()*0.01);
    return postLatencies.get(targetIndex);
  }

  /*
   *  calculate P99 of GET
   * */
  public long getP99OfGetRequest(){
    ArrayList<Long> getLatencies=getSortedArrayofLatencyOfType("GET");
    int targetIndex=getLatencies.size()-(int)(getLatencies.size()*0.01);
    return getLatencies.get(targetIndex);
  }


  /*
   *  calculate max latency of POST
   * */
  public long getMaxLatencyOfPostRequest(){
    ArrayList<Long> postLatencies=getSortedArrayofLatencyOfType("POST");
    return postLatencies.get(postLatencies.size()-1);
  }

  /*
   *  calculate maxLatency of GET
   * */
  public long getMaxLatencyGetRequest(){
    ArrayList<Long> getLatencies=getSortedArrayofLatencyOfType("GET");
    return getLatencies.get(getLatencies.size()-1);
  }




}
