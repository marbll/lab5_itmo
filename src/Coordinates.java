package Labs;

import java.util.Objects;

/**
 * Координаты объекта
 */
public class Coordinates implements Comparable<Coordinates> {
    private int x; //Значение поля должно быть больше -985
    private Double y; //Поле не может быть null

    Coordinates(int x, Double y){
        this.x = x;
        this.y = y;
    }
    public int getX() {
        return x;
    }
    public Double getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Coordinates {" +
                "x:" + x + ", y:" + y +  "} ";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coordinates)) return false;
        Coordinates coordinates = (Coordinates) o;
        return x == coordinates.getX() &&
                y.equals(coordinates.getY());
    }

    @Override
    public int hashCode() {
        return Objects.hash(x,y);
    }


    /**
     * сравнение по координатам по расстоянию от начала координат
     */
    @Override
    public int compareTo(Coordinates o) {
        return (int) (x*x + y*y - o.getX()*o.getX() - o.getY()*o.getY());
    }

}
