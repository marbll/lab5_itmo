package Labs;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Класс, объекты которого хранятся в коллекции
 */
public class LabWork implements Comparable<LabWork> {
    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private long minimalPoint; //Значение поля должно быть больше 0
    private String description; //Строка не может быть пустой, Поле не может быть null
    private Double averagePoint; //Поле не может быть null, Значение поля должно быть больше 0
    private Difficulty difficulty; //Поле может быть null
    private Discipline discipline; //Поле не может быть null

    public LabWork(Integer id, String name, Coordinates coordinates, LocalDate creationDate, long minimalPoint, String description, Double averagePoint, Difficulty difficulty, Discipline discipline){
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

    /**
     * Сравнивает данную лабораторную работу с переданной в качестве параметра по averagePoint (другим полям в случае равенства)
     * @param e Лабораторная работа, сравниваемая с данной
     * @return число
     */
    @Override
    public int compareTo(LabWork e) {
        if (!averagePoint.equals(e.getAveragePoint())) return (int) (averagePoint.compareTo(e.getAveragePoint()));
        if (minimalPoint - e.getMinimalPoint() != 0) return (int) (minimalPoint - e.getMinimalPoint());
        if (difficulty != null && e.getDifficulty() == null) return 1;
        else if (difficulty == null && e.getDifficulty() != null) return -1;
        else if (difficulty != null && e.getDifficulty() != null && difficulty.compareTo(e.getDifficulty()) != 0) return difficulty.compareTo(e.getDifficulty());
        if (discipline.compareTo(e.getDiscipline()) != 0) return discipline.compareTo(e.getDiscipline());
        if (coordinates.compareTo(e.getCoordinates()) != 0) return coordinates.compareTo(e.getCoordinates());
        if (description.compareTo(e.getDescription()) != 0) return description.compareTo(e.getDescription());
        return name.compareTo(e.getName());
    }

    public String getName() {
        return name;
    }
    public Coordinates getCoordinates() {
        return coordinates;
    }
    public Integer getId() {
        return id;
    }
    public LocalDate getCreationDate() {
        return creationDate;
    }
    public long getMinimalPoint() {
        return minimalPoint;
    }
    public Difficulty getDifficulty() {
        return difficulty;
    }
    public Discipline getDiscipline() {
        return discipline;
    }
    public String getDescription() {
        return description;
    }
    public Double getAveragePoint() {
        return averagePoint;
    }

    @Override
    public String toString() {
        return "LabWork { " +
                "id: " + id + ", name: " + name + "," + coordinates.toString() + ", creationDate:"
                + creationDate + ", minimalPoint:" + minimalPoint + ", description:" + description + ", averagePoint:" + averagePoint +
                ", difficulty:" + difficulty + ", " + discipline.toString() + "}" ;
    }

    /**
     * проверяет на равенство объекты LabWork (равенство только при равенстве всех полей)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LabWork)) return false;
        LabWork labWork = (LabWork) o;
        return id.equals(labWork.getId()) &&
                name.equals(labWork.getName()) &&
                coordinates.equals(labWork.getCoordinates()) &&
                creationDate.equals(labWork.getCreationDate()) &&
                minimalPoint == labWork.getMinimalPoint() &&
                description.equals(labWork.getDescription()) &&
                averagePoint.equals(labWork.getAveragePoint()) &&
                difficulty.equals(labWork.getDifficulty()) &&
                discipline.equals(labWork.getDiscipline());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, minimalPoint, description, averagePoint, difficulty, discipline);
    }
}
