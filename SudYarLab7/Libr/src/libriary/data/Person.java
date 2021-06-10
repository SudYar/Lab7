package libriary.data;

import java.io.Serializable;

public class Person implements Serializable {
    private static final long serialVersionUID = 3L;

    private String name; //Поле не может быть null, Строка не может быть пустой
    private double weight; //Значение поля должно быть больше 0
    private String passportID; //Значение этого поля должно быть уникальным, Поле не может быть null

    public Person(String name, double weight, String passportID) {
        this.name = name;
        this.weight = weight;
        this.passportID = passportID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getPassportID() {
        return passportID;
    }

    public void setPassportID(String passportID) {
        this.passportID = passportID;
    }

    @Override
    public String toString() {
        return
                "\n\t\tName: " + name +
                "\n\t\tWeight: " + weight +
                "\n\t\tPassportID: " + passportID
                ;
    }
}
