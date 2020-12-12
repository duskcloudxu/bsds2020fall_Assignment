package dao;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LiftRidesDao {
  public void clearLiftRideTable(){
    try(
      DBManager dbManager = new DBManager();
      ResultSet rs = dbManager.execQueryUpdate("TRUNCATE TABLE LiftRides")
      ) {
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public boolean addLiftRide(String ResortId, String DayId, String SkierId, String Time, String LifterId){
    ResortId = utils.addQuotes(ResortId);
    String Vertical = Integer.toString(Integer.parseInt(LifterId)*10);
    String query = "INSERT INTO LiftRides(ResortId, DayId, SkierId, Time, LifterId, Vertical) VALUES";
    query += utils.paramParse(ResortId,DayId,SkierId,Time,LifterId,Vertical);
    query += " ON DUPLICATE KEY UPDATE Vertical = Vertical";
    //    System.out.println(query);
    try(
      DBManager dbManager = new DBManager();
      ResultSet rs = dbManager.execQueryUpdate(query);
      ){
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
    return true;
  }

  public int getVertByDayAndSkiID(String ResortId, String DayId, String SkierId){
    String query = "SELECT IF(SUM(Vertical)!=0,SUM(Vertical),0) VerticalSum FROM LiftRides WHERE (ResortId,DayId,SkierId)=";
    query += utils.paramParse(utils.addQuotes(ResortId), DayId, SkierId);
//    System.out.println(query);
    try(
      DBManager dbManager = new DBManager();
      ResultSet rs = dbManager.execQuery(query)
      ) {
      rs.next();
      return(rs.getInt(1));

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return -1;
  }

  public int getVertBySkiID(String ResortId, String SkierId){
    String query = "SELECT IF(SUM(Vertical)!=0,SUM(Vertical),0) VerticalSum FROM LiftRides WHERE (ResortId,SkierId)=";
    query += utils.paramParse(utils.addQuotes(ResortId), SkierId);
//    System.out.println(query);
    try(
      DBManager dbManager = new DBManager();
      ResultSet rs = dbManager.execQuery(query)
    ) {
      rs.next();
      return(rs.getInt(1));

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return -1;
  }
}
