package Labs;

import java.util.Objects;

public class Coordinates implements Comparable<Coordinates> {
   private int x;
   private Double y;

   Coordinates(int x, Double y) {
      this.x = x;
      this.y = y;
   }

   public int getX() {
      return this.x;
   }

   public Double getY() {
      return this.y;
   }

   public String toString() {
      return "Coordinates {x:" + this.x + ", y:" + this.y + "} ";
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof Coordinates)) {
         return false;
      } else {
         Coordinates coordinates = (Coordinates)o;
         return this.x == coordinates.getX() && this.y.equals(coordinates.getY());
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.x, this.y});
   }

   public int compareTo(Coordinates o) {
      return (int)((double)(this.x * this.x) + this.y * this.y - (double)(o.getX() * o.getX()) - o.getY() * o.getY());
   }
}
