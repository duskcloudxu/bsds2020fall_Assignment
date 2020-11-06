package dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBManager implements AutoCloseable{
  private final String user = "admin";
  private final String pwd = "cs6650password";
  private final String host = "database-1.cixpuqflb19s.us-east-1.rds.amazonaws.com";
  private final int port = 3306;
  private final String schema = "SkiData";
  private Connection connection;

  /** Get the connection to the database instance.
   *
   */
  DBManager() throws SQLException {
    try {
      Properties connectionProperties = new Properties();
      // Ensure the JDBC driver is loaded by retrieving the runtime Class descriptor.
      // Otherwise, Tomcat may have issues loading libraries in the proper order.
      // One alternative is calling this in the HttpServlet init() override.
      try {
        Class.forName("com.mysql.cj.jdbc.Driver");
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
        throw new SQLException(e);
      }
      connection = DriverManager.getConnection(
        "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.schema,
        this.user,this.pwd);
    } catch (SQLException e) {
      e.printStackTrace();
      throw e;
    }

  }

  /** Close the connection to the database instance. */
  public void close() throws SQLException {
    try {
      connection.close();
    } catch (SQLException e) {
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
