package pooling;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import org.junit.Test;

public class DatabasePoolTest {

  @Test
  public void unitTest() throws Exception {
    DatabasePool databasePool = DatabasePool.getInstance();
    Connection connection = databasePool.borrowObject();
    Statement statement = connection.createStatement();
    ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM LiftRides");
    while(rs.next()){
      System.out.println(rs.getString(1));
    }
    databasePool.returnObject(connection);
  }
}
