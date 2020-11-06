package servlets;//import com.google.gson.Gson;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dao.LiftRidesDao;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "servlets.SkiersServlet")
public class SkiersServlet extends HttpServlet {

  private Gson gson = new Gson();


  protected void doPost(HttpServletRequest req,
    HttpServletResponse res)
    throws ServletException, IOException {
    res.setContentType("application/json");
    String urlPath = req.getPathInfo();
    if (urlPath == null || urlPath.isEmpty()) {
      badRequest(res);
      return;
    }
    String[] params = urlPath.substring(1).split("/");
    if (!params[0].equals("liftrides")) {
      badRequest(res, "invalid parameters");
      return;
    }

    JsonObject jsonReq;
    try {
      jsonReq = readJson(req);
    } catch (Exception e) {
      badRequest(res, "Body not in JSON format");
      return;
    }

    System.out.println(jsonReq.toString());
    JsonElement resortObj = jsonReq.get("resortID");
    JsonElement dayIdObj = jsonReq.get("dayID");
    JsonElement skierIdObj = jsonReq.get("skierID");
    JsonElement timeObj = jsonReq.get("time");
    JsonElement liftID = jsonReq.get("liftID");
    if (resortObj == null || dayIdObj == null || skierIdObj == null || timeObj == null
      || liftID == null) {
      badRequest(res, "missing parameters in request body");
      return;
    }
    LiftRidesDao liftRidesDao = new LiftRidesDao();
    boolean postStatus = liftRidesDao.addLiftRide(
      resortObj.getAsString(),
      dayIdObj.getAsString(),
      skierIdObj.getAsString(),
      timeObj.getAsString(),
      liftID.getAsString()
    );

    if(postStatus)
      res.setStatus(HttpServletResponse.SC_CREATED);
    else
      res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
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
    String[] params = urlPath.substring(1).split("/");
    switch (params.length) {
      // resolve routing /clearData
      case 1: {
        if (params[0].equals("clearData")) {
          LiftRidesDao liftRidesDao = new LiftRidesDao();
          liftRidesDao.clearLiftRideTable();
        }
        break;
      }
      // resolve routing /{skierID}/vertical
      case 2: {
        if (!params[1].equals("vertical")) {
          badRequest(res, "invalid parameters");
          return;
        }
        String resort = req.getParameter("resort");
        String skiId = params[0];
        if (resort == null) {
          badRequest(res, "No resort param provided");
          return;
        }
        try {
          Integer.parseInt(skiId);
        } catch (Exception e) {
          badRequest(res, "skier number should only contain digits");
          return;
        }
        LiftRidesDao liftRidesDao = new LiftRidesDao();
        int vert = liftRidesDao.getVertBySkiID(resort, skiId);
        res.setStatus(HttpServletResponse.SC_OK);
        JsonObject jsonRes = new JsonObject();
        jsonRes.addProperty("resortID", resort);
        jsonRes.addProperty("totalVert", vert);

        PrintWriter pw = res.getWriter();
        pw.write(jsonRes.toString());
        break;
      }
      // resolve routing /{resortID}/days/{dayID}/skiers/{skierID}
      case 5: {
        String resort = params[0];
        String dayId = params[2];
        String skierId = params[4];

        LiftRidesDao liftRidesDao = new LiftRidesDao();
        int vert = liftRidesDao.getVertByDayAndSkiID(resort, dayId, skierId);

        JsonObject jsonRes = new JsonObject();
        jsonRes.addProperty("resortID", resort);
        jsonRes.addProperty("totalVert", vert);

        res.setStatus(HttpServletResponse.SC_OK);
        PrintWriter pw = res.getWriter();
        pw.write(jsonRes.toString());
        break;
      }
      default: {
        badRequest(res, "invalid parameters");
      }

    }


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

  protected JsonObject readJson(HttpServletRequest req) throws IOException {

    StringBuilder sb = new StringBuilder();
    BufferedReader bf = req.getReader();
    try {
      String line;
      while ((line = bf.readLine()) != null) {
        sb.append(line + "\n");
      }
    } finally {
      bf.close();
    }

    JsonObject jsonObj;
    jsonObj = gson.fromJson(sb.toString(), JsonObject.class);

    return jsonObj;

  }
}
