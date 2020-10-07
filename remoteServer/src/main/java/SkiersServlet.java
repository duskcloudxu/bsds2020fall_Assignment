//import com.google.gson.Gson;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "SkiersServlet")
public class SkiersServlet extends HttpServlet {
  private Gson gson=new Gson();
  protected void doPost(HttpServletRequest req,
    HttpServletResponse res)
    throws ServletException, IOException {
    res.setContentType("application/json");
    String urlPath = req.getPathInfo();
    if (urlPath == null || urlPath.isEmpty()) {
      badRequest(res);
      return;
    }
    StringBuilder sb = new StringBuilder();
    BufferedReader bf = req.getReader();
    try{
      String line;
      while((line = bf.readLine())!=null){
        sb.append(line + "\n");
      }
    }finally {
      bf.close();
    }

    Object jsonObj;
    try{
      jsonObj=gson.fromJson(sb.toString(), Object.class);
    }catch (Exception e){
      badRequest(res);
      return;
    }

    res.setStatus(HttpServletResponse.SC_CREATED);
    return;



  }


  protected void doGet(HttpServletRequest req,
    HttpServletResponse res)
    throws ServletException, IOException {
    res.setContentType("application/json");
    String urlPath = req.getPathInfo();
    if (urlPath == null || urlPath.isEmpty()) {
      badRequest(res);
      return;
    }
    String[]params=urlPath.substring(1).split("/");
    System.out.println(params.toString());
    switch (params.length){
      case 2:{
        if(!params[1].equals("vertical")){
          badRequest(res);
          return;
        }
        res.setStatus(HttpServletResponse.SC_OK);
        PrintWriter pw= res.getWriter();
        pw.write("{\n"
          + "  \"resortID\": \"Mission Ridge\",\n"
          + "  \"totalVert\": 56734\n"
          + "}");
        break;
      }
      case 5:{
        res.setStatus(HttpServletResponse.SC_OK);
        PrintWriter pw= res.getWriter();
        pw.write("{\n"
          + "  \"resortID\": \""+params[0]+"\",\n"
          + "  \"totalVert\": 56734\n"
          + "}");
        break;
      }
      default:{
        badRequest(res);
      }

    }


  }

  protected void badRequest(HttpServletResponse res)
    throws ServletException, IOException {
    res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    res.getWriter().write("{\n"
      + "  \"message\": \"string\"\n"
      + "}");
  }
}
