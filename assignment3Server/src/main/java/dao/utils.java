package dao;

public class utils {
  public static String addQuotes(String string){
    return "'"+string+"'";
  }
  public static String paramParse(String... params){
    return "(" + String.join(",",params) + ")";
  }

}
