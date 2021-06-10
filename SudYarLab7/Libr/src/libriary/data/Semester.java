package libriary.data;

import java.io.Serializable;

public enum Semester implements Serializable {

    FIRST,
    FIFTH,
    SEVENTH;

    private static final long serialVersionUID = 2L;
    public static String nameList() {
        String nameList = "";
        for (Semester semester : values()) {
            nameList += semester.name() + ", ";
        }
        return nameList.substring(0, nameList.length()-2);

    }
}

