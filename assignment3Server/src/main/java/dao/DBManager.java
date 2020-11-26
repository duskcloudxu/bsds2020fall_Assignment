package dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import pooling.DatabasePool;

public class DBManager implements AutoCloseable{
  private Connection connection;
  private DatabasePool databasePool;

  /** Get the connection to the database instance.
   *
   */
  DBManager() throws Exception {
    try {
      databasePool = DatabasePool.getInstance();
      connection = databasePool.borrowObject();
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }

  }

  /** Close the connection to the database instance. */
  public void close() throws Exception {
    try {
      databasePool.returnObject(connection);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }

  /**
   *  execute read-only query in the input, the query should be a valid query query and it executed successfully, it would return
   *  the result in form of ResultSet. Refer corresponding test for example.
   *
   * @param {String} query an read-only SQL query
   * **/
  public ResultSet execQuery(String query) throws SQLException {
    try{
      Statement stmt=connection.createStatement();
      return stmt.executeQuery(query);
    }catch (Exception e){
      e.printStackTrace();
    }
    return null;
  }

  /**
   *  execute write-only query in the input, the query should be a valid query query and it executed successfully, it would return
   *  the result in form of ResultSet. Refer corresponding test for example.
   *
   * @param {String} query an write-only SQL query
   * **/
  public ResultSet execQueryUpdate(String query) throws SQLException {
    try{
      Statement stmt=connection.createStatement();
      stmt.executeUpdate(query,stmt.RETURN_GENERATED_KEYS);
      return stmt.getGeneratedKeys();
    }catch (Exception e){
      e.printStackTrace();
    }
    return null;
  }

}
