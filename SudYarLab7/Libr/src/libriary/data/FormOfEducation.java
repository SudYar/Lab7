package libriary.data;

import java.io.Serializable;

public enum FormOfEducation implements Serializable {

    DISTANCE_EDUCATION,
    FULL_TIME_EDUCATION,
    EVENING_CLASSES;

    private static final long serialVersionUID = 4L;
    public static String nameList() {
        String nameList = "";
        for (FormOfEducation formOfEducation : values()) {
            nameList += formOfEducation.name() + ", ";
        }
        return nameList.substring(0, nameList.length()-2);

    }
}
