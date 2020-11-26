package pooling;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.DestroyMode;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class DatabaseConnectionFactory extends BasePooledObjectFactory<Connection> {

  private final String user = "admin";
  private final String pwd = "cs6650password";
  private final String host = "database-1.cixpuqflb19s.us-east-1.rds.amazonaws.com";
  private final int port = 3306;
  private final String schema = "SkiData";

  @Override
  public Connection create() throws Exception {
    Connection connection;
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
    return connection;
  }

  @Override
  public PooledObject<Connection> wrap(Connection connection) {
    return new DefaultPooledObject<>(connection);
  }

}
