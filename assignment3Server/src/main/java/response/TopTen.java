package response;

import java.util.ArrayList;
import model.Skier;

public class TopTen {
  private ArrayList<Skier> topTenSkiers;

  public TopTen() {
    Skier dummy=new Skier();
    topTenSkiers=new ArrayList<Skier>();
    topTenSkiers.add(dummy);
  }

  public TopTen(ArrayList<Skier> topTenSkiers) {
    this.topTenSkiers = topTenSkiers;
  }
}
