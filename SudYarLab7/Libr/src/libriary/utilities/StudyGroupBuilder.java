package libriary.utilities;

import libriary.data.*;

import java.util.Date;

public class StudyGroupBuilder {
    private StudyGroup studyGroup;
    private Double x;
    private Float y;
    private String nameAdmin;
    private Double weight;
    private String passportId;
    private Integer ownerId;
    private String ownerLogin;

    public StudyGroupBuilder() {

        studyGroup = new StudyGroup();
        studyGroup.setCreationDate(new Date());
    }

    public boolean addName (String line){
        String name = null;
        if ((line != null) && !("".equals(line.trim())) && (line.trim().split(" ").length < 2)) {
            name = StudyGroupParser.parseName(line.trim());
        }
        if (name == null) return false;
        else{
            studyGroup.setName(name);
            return true;
        }
    }
    public boolean addX (String line){
        x = null;
        if ((line != null) && !("".equals(line.trim())) && (line.trim().split(" ").length < 2)) {
            x = StudyGroupParser.parseX(line.trim());
        }
        return x != null;
    }

    public boolean addX (Double dou){
        x = dou;
        return x != null;
    }

    public boolean addY (String line){
        y = null;
        if ((line != null) && !("".equals(line.trim())) && (line.trim().split(" ").length < 2)) {
            y = StudyGroupParser.parseY(line.trim());
        }
        return y != null;
    }
    public boolean addY (Float flo){
        y = null;
        if (flo > Coordinates.yMinValue) y = flo;
        return y != null;
    }

    public boolean addStudentCount (String line){
        Integer studentCount = null;
        if ((line != null) && !("".equals(line.trim())) && (line.trim().split(" ").length < 2)) {
            studentCount = StudyGroupParser.parseStudentsCount(line.trim());
        }
        if (studentCount == null) return false;
        else{
            studyGroup.setStudentsCount(studentCount);
            return true;
        }
    }

    public boolean addStudentCount (Integer value){
        Integer studentCount = null;
        if ((value != null) && (value > 0)) {
            studentCount = value;
        }
        if (studentCount == null) return false;
        else{
            studyGroup.setStudentsCount(studentCount);
            return true;
        }
    }

    public boolean addFormOfEducation (String line){
        FormOfEducation formOfEducation = null;
        if ((line != null) && !("".equals(line.trim())) && (line.trim().split(" ").length < 2)) {
            formOfEducation = StudyGroupParser.parseFormOfEducation(line.trim());
        }
        if (formOfEducation == null) return false;
        else{
            studyGroup.setFormOfEducation(formOfEducation);
            return true;
        }
    }

    public boolean addSemester (String line){
        Semester semester = null;
        if ((line != null) && !("".equals(line.trim())) && (line.trim().split(" ").length < 2)) {
            semester = StudyGroupParser.parseSemesterEnum(line.trim());
        }
        if (semester == null) return false;
        else{
            studyGroup.setSemesterEnum(semester);
            return true;
        }
    }

    public boolean addNameAdmin (String line){
        nameAdmin = null;
        if ((line != null) && !("".equals(line.trim())) && (line.trim().split(" ").length < 2)) {
            nameAdmin = StudyGroupParser.parseNameAdmin(line.trim());
        }
        return nameAdmin != null;
    }

    public boolean addWeight (String line){
        weight = null;
        if ((line != null) && !("".equals(line.trim())) && (line.trim().split(" ").length < 2)) {
            weight = StudyGroupParser.parseWeigh(line.trim());
        }
        return weight != null;
    }
    public boolean addPassportId (String line){
        passportId = null;
        if ((line != null) && !("".equals(line.trim())) && (line.trim().split(" ").length < 2)) {
            passportId = StudyGroupParser.parsePassportId(line.trim());
        }
        return passportId != null;
    }

    public boolean addOwnerId(int id){
        ownerId = id;
        studyGroup.setIdOwner(ownerId);
        return true;
    }

    public boolean addOwnerLogin(String line){
        ownerLogin = null;
        if ((line != null) && !("".equals(line.trim())) && (line.trim().split(" ").length < 2)){
            ownerLogin = line;
            studyGroup.setLoginOwner(ownerLogin);
        }
        return ownerLogin != null;
    }

    public StudyGroup toStudyGroup (){
        studyGroup.setCoordinates(StudyGroupParser.parseCoordinates(x, y));
        studyGroup.setGroupAdmin(StudyGroupParser.parsePerson(nameAdmin, weight, passportId));
        if ((studyGroup.getName() != null) && (studyGroup.getCoordinates() != null) &&
                (studyGroup.getStudentsCount() != null) && (studyGroup.getSemesterEnum() != null) &&
                (studyGroup.getCreationDate() != null))
            return studyGroup;
        else return null;
    }
}
