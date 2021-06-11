package libriary.internet;

import com.sun.istack.internal.NotNull;
import libriary.data.Person;
import libriary.data.StudyGroup;
import libriary.data.StudyGroupCollection;
import libriary.exception.DBExceprion;
import libriary.exception.DuplicateException;
import libriary.internet.User;
import libriary.utilities.StudyGroupBuilder;
import libriary.utilities.StudyGroupParser;

import java.sql.*;
import java.util.Date;


public class DataBase {
    private Connection connection;

    static final String DB_DRIVER = "org.postgresql.Driver";
    static final String DB_CONNECT = "jdbc:postgresql://localhost:5430/studs";

    public DataBase(String login, String password) throws ClassNotFoundException, SQLException {
        Class.forName(DB_DRIVER);

        connection = DriverManager.getConnection(DB_CONNECT, login, password);

    }

    public int insert (User user) throws DBExceprion, SQLException {
        PreparedStatement preparedStatement =
                connection.prepareStatement("insert into users (login, password) values (?, ?) returning id");
        preparedStatement.setString(1, user.getLogin());
        preparedStatement.setString(2, user.getPassword());

        if (preparedStatement.execute()){
            ResultSet resultSet = preparedStatement.getResultSet();
            if (resultSet.next()){
                return  resultSet.getInt("id");
            }
        }
        throw new DBExceprion("Не получилось добавить нового пользователя");

    }

    public int insert(@NotNull Person groupAdmin) throws SQLException, DBExceprion {
        if (groupAdmin == null) throw new NullPointerException("groupAdmin cannot be null");
        PreparedStatement preparedStatement =
                connection.prepareStatement("insert into person (name, weight, passport_id) values (?, ?, ?) returning id");
        preparedStatement.setString(1, groupAdmin.getName());
        preparedStatement.setDouble(2, groupAdmin.getWeight());
        preparedStatement.setString(3, groupAdmin.getPassportID());

        if (preparedStatement.execute()) {
            ResultSet resultSet = preparedStatement.getResultSet();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        }
        throw new DBExceprion("Не получилось добавить нового Админа");

    }

    public int insert(StudyGroup studyGroup, int ownedId, String login) throws SQLException, DBExceprion {
        Integer personId = (studyGroup.getGroupAdmin() == null ? null : insert(studyGroup.getGroupAdmin()));
        PreparedStatement preparedStatement =
                connection.prepareStatement("insert into groups (name, x, y, date, studentcount, form_of_education, sem, id_admin, login_owner, id_owner) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning id");
        preparedStatement.setString(1, studyGroup.getName());
        preparedStatement.setDouble(2, studyGroup.getCoordinates().getX());
        preparedStatement.setFloat(3, studyGroup.getCoordinates().getY());
        preparedStatement.setLong(4, studyGroup.getCreationDate().getTime());
        preparedStatement.setInt(5, studyGroup.getStudentsCount());
        preparedStatement.setString(6, (studyGroup.getFormOfEducation() == null ? null : studyGroup.getFormOfEducation().toString()));
        preparedStatement.setString(7, studyGroup.getSemesterEnum().toString());
        preparedStatement.setInt(8, personId);
        preparedStatement.setString(9, login);
        preparedStatement.setInt(10, ownedId);

        if (preparedStatement.execute()) {
            ResultSet resultSet = preparedStatement.getResultSet();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        }
        throw new DBExceprion("Не получилось добавить новую группу");
    }

    public Person selectPerson(int id) throws SQLException, DBExceprion {
        PreparedStatement preparedStatement = connection.prepareStatement("select * from persons where id=?");
        preparedStatement.setInt(1, id);
        if (preparedStatement.execute()){
            ResultSet resultSet = preparedStatement.getResultSet();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                Double weight = resultSet.getDouble("weight");
                String passportId = resultSet.getString("passport_id");
                return new Person(name, weight, passportId);
            }
        }
        throw new DBExceprion("Не найден админ группы с id = " + id);
    }

    public User selectUser(int id) throws DBExceprion, SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("select * from users where id=?");
        preparedStatement.setInt(1, id);
        if (preparedStatement.execute()){
            ResultSet resultSet = preparedStatement.getResultSet();
            if (resultSet.next()) {
                String login = resultSet.getString("login");
                String password = resultSet.getString("password");
                User newUser = new User(login, password);
                newUser.setId(id);
                return newUser;
            }
        }
        throw new DBExceprion("Не найден пользователь с id = " + id);
    }

    public User selectUser(String login) throws DBExceprion, SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("select * from users where login=?");
        preparedStatement.setString(1, login);
        if (preparedStatement.execute()){
            ResultSet resultSet = preparedStatement.getResultSet();
            if (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String password = resultSet.getString("password");
                User newUser = new User(login, password);
                newUser.setId(id);
                return newUser;
            }
        }
        throw new DBExceprion("Не найден пользователь с login = " + login);
    }

    public StudyGroup selectStudyGroup (int id) throws SQLException, DBExceprion {
        PreparedStatement preparedStatement = connection.prepareStatement("select * from groups where id=?");
        preparedStatement.setInt(1, id);
        if (preparedStatement.execute()){
            ResultSet resultSet = preparedStatement.getResultSet();
            if (resultSet.next()) {
                StudyGroupBuilder builder = new StudyGroupBuilder();
                builder.addName(resultSet.getString("name"));
                builder.addX(resultSet.getDouble("x"));
                builder.addY (resultSet.getFloat("y"));
                builder.addStudentCount(resultSet.getInt("studentcount"));
                builder.addFormOfEducation(resultSet.getString("form_of_education"));
                builder.addSemester(resultSet.getString("sem"));
                Person groupAdmin = selectPerson(resultSet.getInt("id_admin"));
                builder.addOwnerId(resultSet.getInt("login_owner"));
                builder.addOwnerLogin(resultSet.getString("login_owner"));
                StudyGroup studyGroup = builder.toStudyGroup();
                Date creationDate = StudyGroupParser.parseDate(resultSet.getLong("date"));
                studyGroup.setId(id);
                studyGroup.setCreationDate(creationDate);
                studyGroup.setGroupAdmin(groupAdmin);
                return studyGroup;
            }
        }
        throw new DBExceprion("Не найдена группа с id = " + id);
    }

    public String selectAll(StudyGroupCollection sgc) throws SQLException, DBExceprion {
        String answer = "";
        PreparedStatement preparedStatement = connection.prepareStatement("select * from groups");
        if (preparedStatement.execute()){
            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                StudyGroupBuilder builder = new StudyGroupBuilder();
                Integer id = (resultSet.getInt("id"));
                builder.addName(resultSet.getString("name"));
                builder.addX(resultSet.getDouble("x"));
                builder.addY (resultSet.getFloat("y"));
                builder.addStudentCount(resultSet.getInt("studentcount"));
                builder.addFormOfEducation(resultSet.getString("form_of_education"));
                builder.addSemester(resultSet.getString("sem"));
                Person groupAdmin = selectPerson(resultSet.getInt("id_admin"));
                builder.addOwnerId(resultSet.getInt("login_owner"));
                builder.addOwnerLogin(resultSet.getString("login_owner"));
                StudyGroup studyGroup = builder.toStudyGroup();
                Date creationDate = StudyGroupParser.parseDate(resultSet.getLong("date"));
                studyGroup.setId(id);
                studyGroup.setCreationDate(creationDate);
                studyGroup.setGroupAdmin(groupAdmin);
                try {
                    sgc.add( studyGroup);
                } catch (DuplicateException e) {
                    answer+= e.getMessage() + " у элемента с id = " + id + "\n";
                }
            }
        }
        preparedStatement = connection.prepareStatement("select * from users");
        if (preparedStatement.execute()) {
            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String login = resultSet.getString("login");
                String password = resultSet.getString("password");
                User newUser = new User(login, password);
                newUser.setId(id);
                String line = sgc.addUser(newUser);
                answer += ("Пользователь успешно зарегестрирован".equals(line) ? "" : line + " (у логина с id = " + id + "\n");
            }

            answer += "Копирование данных с базы данных завершено";
        }
        return answer;

    }


}