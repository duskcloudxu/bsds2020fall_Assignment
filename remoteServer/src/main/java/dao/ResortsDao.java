package dao;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResortsDao {

  public boolean checkResortByName(String name) throws SQLException {
    boolean res = false;
    try (
      DBManager dbManager = new DBManager();
      ResultSet rs = dbManager.execQuery("SELECT * FROM Resorts WHERE ResortId = \""+ name+"\""  );
    ) {
      res = rs.next();
    }
    return res;
  }


}
