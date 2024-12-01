package dk.fvtrademarket.fvplus.core.util;

import net.labymod.api.util.io.web.request.Request;
import net.labymod.api.util.io.web.request.Response;
import java.util.ArrayList;

public class DataFormatter {

  public static ArrayList<String[]> csv(String url, boolean skipHeader) {
    Response<String> response = Request.ofString()
        .url(url)
        .executeSync();
    ArrayList<String[]> data = new ArrayList<>();
    for (String line : response.get().split("\n")) {
      data.add(line.split(","));
    }
    if (!data.isEmpty() && skipHeader) {
      data.removeFirst();
    }
    return data;
  }

}
