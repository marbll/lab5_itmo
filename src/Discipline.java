package Labs;

import java.util.Objects;

public class Discipline {
   private String name;
   private Long labsCount;

   Discipline(String name, Long labsCount) {
      this.name = name;
      this.labsCount = labsCount;
   }

   public int compareTo(Discipline e) {
      if (this.labsCount != null && e.getLabsCount() == null) {
         return 1;
      } else if (this.labsCount == null && e.getLabsCount() != null) {
         return -1;
      } else {
         return this.labsCount != null && e.getLabsCount() != null && this.labsCount.compareTo(e.getLabsCount()) != 0 ? this.labsCount.compareTo(e.labsCount) : this.name.compareTo(e.getName());
      }
   }

   public String getName() {
      return this.name;
   }

   public Long getLabsCount() {
      return this.labsCount;
   }

   public String toString() {
      return "Discipline {name:" + this.name + ", labs count:" + this.labsCount + "} ";
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof Discipline)) {
         return false;
      } else {
         Discipline discipline = (Discipline)o;
         return this.name.equals(discipline.getName()) && this.labsCount.equals(discipline.getLabsCount());
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.name, this.labsCount});
   }
}
