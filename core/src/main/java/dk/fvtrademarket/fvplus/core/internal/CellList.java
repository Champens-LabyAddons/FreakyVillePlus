package dk.fvtrademarket.fvplus.core.internal;

import dk.fvtrademarket.fvplus.core.objects.CellBlock;
import dk.fvtrademarket.fvplus.core.util.DataFormatter;
import dk.fvtrademarket.fvplus.core.util.Location;
import dk.fvtrademarket.fvplus.core.util.Resource;
import java.io.IOException;
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
    ArrayList<String[]> csv = DataFormatter.csv(Resource.CELL_LIST_URL);
    for (String[] strings : csv) {
      this.cellBlocks.add(new CellBlock(
          strings[0],
          Integer.parseInt(strings[1]),
          Integer.parseInt(strings[2]),
          strings[3],
          new Location(strings[4], strings[5], strings[6])
      ));
    }
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
