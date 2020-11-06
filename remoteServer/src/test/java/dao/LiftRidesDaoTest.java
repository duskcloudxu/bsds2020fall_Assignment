package dao;

import static org.junit.Assert.*;

import org.junit.Test;

public class LiftRidesDaoTest {

  @Test
  public void clearLiftRideTable() {
    LiftRidesDao liftRidesDao = new LiftRidesDao();
    try {
      liftRidesDao.clearLiftRideTable();
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void addLiftRide() {
    try {
      LiftRidesDao liftRidesDao = new LiftRidesDao();
      liftRidesDao.addLiftRide("Mission Ridge", "2", "2", "2", "31");
    }catch (Exception e){
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void getVertByDayAndSkiID() {
    try {
      LiftRidesDao liftRidesDao = new LiftRidesDao();
      liftRidesDao.clearLiftRideTable();
      liftRidesDao.addLiftRide("Mission Ridge", "2", "2", "2", "15");
      liftRidesDao.addLiftRide("Mission Ridge", "2", "2", "3", "17");
      assertEquals(liftRidesDao.getVertByDayAndSkiID("Mission Ridge", "2", "2"),320);
    }catch (Exception e){
      e.printStackTrace();
      fail();
    }

  }

  @Test
  public void getVertBySkiID() {
    try {
      LiftRidesDao liftRidesDao = new LiftRidesDao();
      liftRidesDao.clearLiftRideTable();
      liftRidesDao.addLiftRide("Mission Ridge", "2", "2", "2", "15");
      liftRidesDao.addLiftRide("Mission Ridge", "2", "2", "3", "17");
      liftRidesDao.addLiftRide("Mission Ridge", "3", "2", "3", "10");
      assertEquals(liftRidesDao.getVertBySkiID("Mission Ridge",  "2"),420);
    }catch (Exception e){
      e.printStackTrace();
      fail();
    }
  }
}
