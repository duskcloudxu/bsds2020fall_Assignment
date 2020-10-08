package com.bsds2020fall.assignment1.model;

import java.time.LocalTime;

public class RequestRecord {
  private LocalTime startTime;
  private String type;
  private long latency;
  private int code;

  public RequestRecord(LocalTime startTime, String type, long latency, int code) {
    this.startTime = startTime;
    this.type = type;
    this.latency = latency;
    this.code = code;
  }

  public LocalTime getStartTime() {
    return startTime;
  }

  public String getType() {
    return type;
  }

  public long getLatency() {
    return latency;
  }

  public int getCode() {
    return code;
  }

}
