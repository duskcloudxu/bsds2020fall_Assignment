package dao;

import java.sql.ResultSet;

public class ResortsDao {

  public boolean checkResortByName(String name) throws Exception {
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
