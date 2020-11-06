package dao;

import static org.junit.Assert.*;

import java.sql.SQLException;

public class ResortsDaoTest {

  @org.junit.Test
  public void checkResortByName() throws SQLException {
    ResortsDao resortsDao = new ResortsDao();
    assertFalse(resortsDao.checkResortByName("mountainRainier"));
    assertTrue(resortsDao.checkResortByName("Mission Ridge"));

  }
}
