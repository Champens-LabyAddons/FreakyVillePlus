package dk.fvtrademarket.fvplus.core.objects;

import dk.fvtrademarket.fvplus.api.misc.Location;

/**
 * Repræsenterer et område med celler på Prison.
 */
public class CellBlock {
  private final String cellType;
  private final int smallestId;
  private final int biggestId;
  private final String locationDescription;
  private final Location minecraftLocation;

  public CellBlock(String cellType, int smallestId, int biggestId, String locationDescription, Location minecraftLocation) {
    this.cellType = cellType.toLowerCase();
    this.smallestId = smallestId;
    this.biggestId = biggestId;
    this.locationDescription = locationDescription;
    this.minecraftLocation = minecraftLocation;
  }

  public String getCellType() {
    return cellType;
  }

  public int getSmallestId() {
    return smallestId;
  }

  public int getBiggestId() {
    return biggestId;
  }

  public String getLocationDescription() {
    return locationDescription;
  }

  public Location getMinecraftLocation() {
    return minecraftLocation;
  }

  public boolean isCellPartOfCellBlock(String typeAndId) {
    typeAndId = typeAndId.toLowerCase();
    StringBuilder type = new StringBuilder();
    StringBuilder tempId = new StringBuilder();
    for (int i = 0; i < typeAndId.length(); i++) {
      if (isNumeric(typeAndId.charAt(i))) {
        tempId.append(typeAndId.charAt(i));
        continue;
      }
      type.append(typeAndId.charAt(i));
    }
    int finalId = Integer.parseInt(String.valueOf(tempId));
    return type.toString().equals(this.cellType) && isInsideRange(finalId);
  }

  private boolean isInsideRange(int id) {
    return id >= this.smallestId && id <= this.biggestId;
  }

  private boolean isNumeric(char c) {
      return switch (c) {
          case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> true;
          default -> false;
      };
  }
}
