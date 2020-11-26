package dao;

import static org.junit.Assert.*;

import com.google.gson.JsonObject;
import org.junit.Test;

public class utilsTest {

  @Test
  public void addQuotes() {
    assertEquals(utils.addQuotes("test"),"'test'");
  }

  @Test
  public void paramParse() {
    assertEquals(utils.paramParse("test","test2"),"(test,test2)");
  }
  @Test
  public void customTest(){
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("name","Bob");
    System.out.println(jsonObject.toString());
  }
}
