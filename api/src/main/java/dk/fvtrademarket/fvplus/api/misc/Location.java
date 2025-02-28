package dk.fvtrademarket.fvplus.api.misc;

import net.labymod.api.util.I18n;
import net.labymod.api.util.math.MathHelper;
import java.util.Objects;

public class Location {
  private float x;
  private float y;
  private float z;

  public Location(float x, float y, float z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public Location(String x, String y, String z) {
    this.x = Float.parseFloat(x);
    this.y = Float.parseFloat(y);
    this.z = Float.parseFloat(z);
  }

  public float getX() {
    return x;
  }

  public void setX(float x) {
    this.x = x;
  }

  public float getY() {
    return y;
  }

  public void setY(float y) {
    this.y = y;
  }

  public float getZ() {
    return z;
  }

  public void setZ(float z) {
    this.z = z;
  }

  public double distance(Location other) {
    return Math.sqrt(distanceSquared(other));
  }

  public double distanceSquared(Location other) {
    if (other == null) {
      throw new IllegalArgumentException(I18n.translate("fvplus.logging.error.nullLocation"));
    }
    return MathHelper.square(x - other.x) + MathHelper.square(y - other.y)
        + MathHelper.square(z - other.z);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Location location = (Location) o;
    return Double.compare(location.x, x) == 0 &&
        Double.compare(location.y, y) == 0 &&
        Double.compare(location.z, z) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y, z);
  }

  @Override
  public String toString() {
    return "Location{" +
        "x=" + x +
        ", y=" + y +
        ", z=" + z +
        '}';
  }
}
