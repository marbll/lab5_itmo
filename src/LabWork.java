package Labs;

import java.time.LocalDate;
import java.util.Objects;

public class LabWork implements Comparable<LabWork> {
   private Integer id;
   private String name;
   private Coordinates coordinates;
   private LocalDate creationDate;
   private long minimalPoint;
   private String description;
   private Double averagePoint;
   private Difficulty difficulty;
   private Discipline discipline;

   public LabWork(Integer id, String name, Coordinates coordinates, LocalDate creationDate, long minimalPoint, String description, Double averagePoint, Difficulty difficulty, Discipline discipline) {
      this.id = id;
      this.name = name;
      this.coordinates = coordinates;
      this.creationDate = creationDate;
      this.minimalPoint = minimalPoint;
      this.description = description;
      this.averagePoint = averagePoint;
      this.difficulty = difficulty;
      this.discipline = discipline;
   }

   public int compareTo(LabWork e) {
      if (!this.averagePoint.equals(e.getAveragePoint())) {
         return this.averagePoint.compareTo(e.getAveragePoint());
      } else if (this.minimalPoint - e.getMinimalPoint() != 0L) {
         return (int)(this.minimalPoint - e.getMinimalPoint());
      } else if (this.difficulty != null && e.getDifficulty() == null) {
         return 1;
      } else if (this.difficulty == null && e.getDifficulty() != null) {
         return -1;
      } else if (this.difficulty != null && e.getDifficulty() != null && this.difficulty.compareTo(e.getDifficulty()) != 0) {
         return this.difficulty.compareTo(e.getDifficulty());
      } else if (this.discipline.compareTo(e.getDiscipline()) != 0) {
         return this.discipline.compareTo(e.getDiscipline());
      } else if (this.coordinates.compareTo(e.getCoordinates()) != 0) {
         return this.coordinates.compareTo(e.getCoordinates());
      } else {
         return this.description.compareTo(e.getDescription()) != 0 ? this.description.compareTo(e.getDescription()) : this.name.compareTo(e.getName());
      }
   }

   public String getName() {
      return this.name;
   }

   public Coordinates getCoordinates() {
      return this.coordinates;
   }

   public Integer getId() {
      return this.id;
   }

   public LocalDate getCreationDate() {
      return this.creationDate;
   }

   public long getMinimalPoint() {
      return this.minimalPoint;
   }

   public Difficulty getDifficulty() {
      return this.difficulty;
   }

   public Discipline getDiscipline() {
      return this.discipline;
   }

   public String getDescription() {
      return this.description;
   }

   public Double getAveragePoint() {
      return this.averagePoint;
   }

   public String toString() {
      return "LabWork { id: " + this.id + ", name: " + this.name + "," + this.coordinates.toString() + ", creationDate:" + this.creationDate + ", minimalPoint:" + this.minimalPoint + ", description:" + this.description + ", averagePoint:" + this.averagePoint + ", difficulty:" + this.difficulty + ", " + this.discipline.toString() + "}";
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof LabWork)) {
         return false;
      } else {
         LabWork labWork = (LabWork)o;
         return this.id.equals(labWork.getId()) && this.name.equals(labWork.getName()) && this.coordinates.equals(labWork.getCoordinates()) && this.creationDate.equals(labWork.getCreationDate()) && this.minimalPoint == labWork.getMinimalPoint() && this.description.equals(labWork.getDescription()) && this.averagePoint.equals(labWork.getAveragePoint()) && this.difficulty.equals(labWork.getDifficulty()) && this.discipline.equals(labWork.getDiscipline());
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.id, this.name, this.coordinates, this.creationDate, this.minimalPoint, this.description, this.averagePoint, this.difficulty, this.discipline});
   }
}
