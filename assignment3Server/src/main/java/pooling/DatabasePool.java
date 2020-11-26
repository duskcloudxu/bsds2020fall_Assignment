package pooling;

import java.sql.Connection;
import org.apache.commons.pool2.impl.GenericObjectPool;


public class DatabasePool {
  private static DatabasePool instance = null;
  private GenericObjectPool<Connection> pool = null;

  private static int MAX_OBJECT_NUM = 30;
  private static boolean IS_BLOCK = true;
  private static int MAX_WAIT_TIME_IN_SEC = 10;

  private DatabasePool(){
    pool = new GenericObjectPool<>(new DatabaseConnectionFactory());
    pool.setMaxTotal(MAX_OBJECT_NUM);
    pool.setBlockWhenExhausted(IS_BLOCK);
    pool.setMaxTotal(MAX_WAIT_TIME_IN_SEC);
  }

  public static DatabasePool getInstance(){
    if(instance == null){
      instance = new DatabasePool();
    }
    return instance;
  }

  public Connection borrowObject() throws Exception {
    return pool.borrowObject();
  }

  public void returnObject(Connection connection) {
    pool.returnObject(connection);
  }
}
