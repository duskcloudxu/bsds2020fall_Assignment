package model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class LiftRide {
  String resortId;
  String dayId;
  String skierId;
  String time;
  String lifeId;

  public LiftRide(JsonObject jsonObject){
    JsonElement resortObj = jsonObject.get("resortID");
    JsonElement dayIdObj = jsonObject.get("dayID");
    JsonElement skierIdObj = jsonObject.get("skierID");
    JsonElement timeObj = jsonObject.get("time");
    JsonElement liftIdObj = jsonObject.get("liftID");
    if (resortObj == null || dayIdObj == null || skierIdObj == null || timeObj == null
      || liftIdObj == null) {
      throw new IllegalArgumentException();
    }
    this.resortId = resortObj.getAsString();
    this.dayId = dayIdObj.getAsString();
    this.skierId = skierIdObj.getAsString();
    this.time = timeObj.getAsString();
    this.lifeId = liftIdObj.getAsString();
  }

  public String getResortId() {
    return resortId;
  }

  public String getDayId() {
    return dayId;
  }

  public String getSkierId() {
    return skierId;
  }

  public String getTime() {
    return time;
  }

  public String getLifeId() {
    return lifeId;
  }
}
