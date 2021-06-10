package libriary.data;

import java.io.Serializable;
import java.util.Objects;

public class Coordinates implements Serializable {
    private static final long serialVersionUID = 5L;

    private double x;
    private float y; //Значение поля должно быть больше -386
    public static final float yMaxValue = Float.MAX_VALUE;
    public static final float yMinValue = -386;

    public static final double xMaxValue = Double.MAX_VALUE;
    public static final double xMinValue = -Double.MAX_VALUE;

    /**
     *
     * @param x
     * @param y > -386
     */
    public Coordinates(double x, float y) {
        this.setX(x);

        this.setY(y);

    }


    public Coordinates() {
        x = 0;
        y = 0;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        if (x < xMaxValue) {
            if (x > xMinValue)
                this.x = x;
            else x = xMinValue;
        }
        else x = xMaxValue;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        if (y < yMaxValue) {
            if (y > yMinValue)
                this.y = y;
            else y = yMinValue;
        }
        else y = yMaxValue;
    }

    @Override
    public String toString() {
        return "X=" + x + ", Y=" + y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return Double.compare(that.x, x) == 0 && Float.compare(that.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
