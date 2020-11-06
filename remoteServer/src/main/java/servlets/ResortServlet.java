package servlets;

import com.google.gson.JsonObject;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "servlets.ResortServlet")
public class ResortServlet extends HttpServlet {

  static String MSG_INVALID = "Invalid Format";

  protected void doPost(HttpServletRequest req,
    HttpServletResponse res)
    throws ServletException, IOException {
    res.setContentType("application/json");
    String urlPath = req.getPathInfo();
    if (urlPath == null || urlPath.isEmpty()) {
      badRequest(res, MSG_INVALID);
      return;
    }


  }

  protected void doGet(HttpServletRequest req,
    HttpServletResponse res)
    throws ServletException, IOException {
    res.setContentType("application/json");
    String urlPath = req.getPathInfo();
    if (urlPath == null || urlPath.isEmpty()) {
      badRequest(res, MSG_INVALID);
      return;
    }
    String[] parameters = urlPath.substring(1).split("/");
    System.out.println(parameters.toString());
    res.setStatus(HttpServletResponse.SC_OK);
    res.getWriter().print("{\n"
      + "  \"topTenSkiers\": [\n"
      + "    {\n"
      + "      \"skierID\": 888899,\n"
      + "      \"VertcialTotal\": 30400\n"
      + "    }\n"
      + "  ]\n"
      + "}");
    res.getWriter().flush();
  }

  protected void badRequest(HttpServletResponse res)
    throws IOException {
    res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    JsonObject jsonRes = new JsonObject();
    jsonRes.addProperty("message", "string");
    res.getWriter().write(jsonRes.toString());
  }

  protected void badRequest(HttpServletResponse res, String message)
    throws IOException {
    res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    JsonObject jsonRes = new JsonObject();
    jsonRes.addProperty("message", message);
    res.getWriter().write(jsonRes.toString());
  }
}
