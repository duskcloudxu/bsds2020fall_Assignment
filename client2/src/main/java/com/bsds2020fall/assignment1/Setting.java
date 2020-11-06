package com.bsds2020fall.assignment1;

public class Setting {


//  private static String baseURL = "http://54.227.95.209:8080/remoteServer_war/";
//  private static String baseURL = "http://localhost:8080/remoteServer_war_exploded/";
  private static String baseURL = "http://assignment2-2130851594.us-east-1.elb.amazonaws.com:8080 /remoteServer_war/";
  private static String resortID = "silverMt";
  private static int dayID = 1;
  private static int numSkier = 50000;
  private static int numLift = 40;
  private static int numThread = 256;

  private Setting() {
  }

  public static int getDayID() {
    return dayID;
  }

  public static String getResortID() {
    return resortID;
  }

  public static int getNumSkier() {
    return numSkier;
  }

  public static int getNumLift() {
    return numLift;
  }

  public static int getNumThread() {
    return numThread;
  }

  public static void setBaseURL(String baseURL) {
    Setting.baseURL = baseURL;
  }

  public static void setResortID(String resortID) {
    Setting.resortID = resortID;
  }

  public static void setDayID(int dayID) {
    Setting.dayID = dayID;
  }

  public static void setNumSkier(int numSkier) {
    Setting.numSkier = numSkier;
  }

  public static void setNumLift(int numLift) throws Exception {
    if(numLift <5|| numLift >60){
      throw new Exception("numRift out of range");
    }
    Setting.numLift = numLift;
  }

  public static void setNumThread(int numThread) throws Exception {
    if(numThread<0 || numThread>512){
      throw new Exception("numThread out of range");
    }
    Setting.numThread = numThread;
  }

  public static String getBaseURL() {
    return baseURL;
  }

}
