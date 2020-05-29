package Labs;

import java.util.Objects;

/**
 * Описывает предмет лабораторной работы
 */
public class Discipline {
    /**
     * название предмета
     */
    private String name; //Поле не может быть null, Строка не может быть пустой
    /**
     * Из скольких лабороторных состоит курс предмета
     */
    private Long labsCount; //Поле может быть null

    Discipline(String name, Long labsCount){
        this.name = name;
        this.labsCount = labsCount;
    }

    /**
     * Сравнивает объекты Discipline (предмет лабораторной) по labsCount, или по name в случае некорректности сравнения по labscount
     */
    public int compareTo(Discipline e) {
        if (labsCount != null && e.getLabsCount() == null) return 1;
        else if (labsCount == null && e.getLabsCount() != null) return -1;
        else if (labsCount != null && e.getLabsCount() != null && labsCount.compareTo(e.getLabsCount()) != 0) return labsCount.compareTo(e.labsCount);
        return name.compareTo(e.getName());
    }

    /**
     *
     * @return Возвращает имя Discipline
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return Возврашает количество лабораторных в курсе этой Discipline
     */
    public Long getLabsCount() {
        return labsCount;
    }

    @Override
    public String toString() {
        return "Discipline {" +
                "name:" + name + ", labs count:" + labsCount +  "} ";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Discipline)) return false;
        Discipline discipline = (Discipline) o;
        return name.equals(discipline.getName()) &&
                labsCount.equals(discipline.getLabsCount());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name,labsCount);
    }

}
