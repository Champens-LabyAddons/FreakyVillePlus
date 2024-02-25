package org.champenslabyaddons.fvp.internal;

import org.champenslabyaddons.fvp.objects.CellBlock;
import org.champenslabyaddons.fvp.util.Location;
import org.champenslabyaddons.fvp.util.csv.Csv;
import org.champenslabyaddons.fvp.util.http.HttpConnector;
import org.champenslabyaddons.fvp.util.resource.Resources;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Denne klasse håndtere listen over forskellige Cellegange / Områder.
 */
public class CellList implements Manager {
  private final List<CellBlock> cellBlocks;

  public CellList() {
    this.cellBlocks = new ArrayList<>();
  }

  @Override
  public void init() throws IOException {
    HttpURLConnection connection = HttpConnector.getOpenHttpConnection(new URL(
        Resources.CELL_LIST_URL));
    Csv csv = new Csv(new BufferedReader(new InputStreamReader(connection.getInputStream())));

    for (String[] strings : csv.readALl()) {
      this.cellBlocks.add(new CellBlock(
          strings[0],
          Integer.parseInt(strings[1]),
          Integer.parseInt(strings[2]),
          strings[3],
          new Location(strings[4], strings[5], strings[6])
      ));
    }

    connection.disconnect();
  }

  public boolean isCellListed(String typeAndId) {
    for (CellBlock cellBlock : this.cellBlocks) {
      if (cellBlock.isCellPartOfCellBlock(typeAndId)) {
        return true;
      }
    }
    return false;
  }

  public Optional<CellBlock> getCorrespondingCellBlock(String typeAndId) {
    for (CellBlock cellBlock : this.cellBlocks) {
      if (cellBlock.isCellPartOfCellBlock(typeAndId)) {
        return Optional.of(cellBlock);
      }
    }
    return Optional.empty();
  }
}
