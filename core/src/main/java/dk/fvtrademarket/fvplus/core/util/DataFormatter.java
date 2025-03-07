package dk.fvtrademarket.fvplus.core.util;

import net.labymod.api.util.io.web.request.Request;
import net.labymod.api.util.io.web.request.Response;
import java.util.ArrayList;

public class DataFormatter {

  public static ArrayList<String[]> csv(String url, boolean skipHeader) {
    return seperatedValues(url, ",", skipHeader);
  }

  public static ArrayList<String[]> tsv(String url, boolean skipHeader) {
    return seperatedValues(url, "\t", skipHeader);
  }

  public static ArrayList<String[]> seperatedValues(String url, String delimiter, boolean skipHeader) {
    Response<String> response = Request.ofString()
        .url(url)
        .executeSync();
    ArrayList<String[]> data = new ArrayList<>();
    for (String line : response.get().split("\n")) {
      data.add(line.split(delimiter));
    }
    if (!data.isEmpty() && skipHeader) {
      data.removeFirst();
    }
    return data;
  }

}
