package libriary.data;

import libriary.exception.DuplicateException;
import libriary.internet.User;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;

public class StudyGroupCollection {
    private static volatile StudyGroupCollection instance;

    private final HashMap<Integer, StudyGroup > collection;
    private final HashMap<String, User> userCollection;
    private final HashSet<String> passportIdSet;
    private int maxId = 0;


    private StudyGroupCollection() {
        collection = new HashMap<>();
        passportIdSet = new HashSet<>();
        userCollection = new HashMap<>();
    }
    public static StudyGroupCollection getInstance() {
        StudyGroupCollection localInstance = instance;
        if (localInstance == null) {
            synchronized (StudyGroupCollection.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new StudyGroupCollection();
                }
            }
        }
        return localInstance;
    }


    public void add(StudyGroup s) throws DuplicateException {
        if (collection.containsKey(s.getId())) throw new DuplicateException("ERROR: Повторение id");
        if ((s.getGroupAdmin() != null) && (passportIdSet.contains(s.getGroupAdmin().getPassportID())))
            throw new DuplicateException("ERROR: Повторение passportId админа");
        collection.put(s.getId(), s);
        if (s.getId() > maxId) maxId = s.getId();
        if (s.getGroupAdmin() != null) passportIdSet.add(s.getGroupAdmin().getPassportID());
    }

    public void update(int id, StudyGroup s) throws DuplicateException{
        s.setId(id);
        if (s.getGroupAdmin() != null) {
            if (passportIdSet.contains(s.getGroupAdmin().getPassportID()) &&
                    ( collection.get(id).getGroupAdmin() == null ||
                    !s.getGroupAdmin().getPassportID().equals(collection.get(id).getGroupAdmin().getPassportID()))) {

                throw new DuplicateException("ERROR: Повторение passportId админа");
            }
        }
        if (collection.get(id).getGroupAdmin() != null) passportIdSet.remove(collection.get(id).getGroupAdmin().getPassportID());
        collection.put(s.getId(), s);
        if (s.getGroupAdmin() != null) passportIdSet.add(s.getGroupAdmin().getPassportID());
    }

    public void insert(StudyGroup s) throws DuplicateException{
        s.setId(++maxId);
        add(s);
    }

    public void remove(int id){

        if (collection.containsKey(id)) {
            if (collection.get(id).getGroupAdmin() != null) passportIdSet.remove(collection.get(id).getGroupAdmin().getPassportID());
            collection.remove(id);
        }
        if (id == maxId) maxId = Collections.max(collection.keySet());
    }

    public void clear(){
        collection.clear();
        passportIdSet.clear();
        maxId = 0;
    }


    public HashMap<Integer, StudyGroup> getCollection() {
        return collection;
    }

    public synchronized String addUser (User user) {
        if ("".trim().equals(user.getLogin())) return "Нельзя добавить пустой логин";
        else if (userCollection.containsKey(user.getLogin())) return "Такой логин уже занят";
        else {
            userCollection.put(user.getLogin(), user);
            return "Пользователь успешно зарегестрирован";
        }
    }

    public User getUser(String login){
        if (login != null) return userCollection.get(login);
        else return  null;
    }

    public String removeUser(String login){
        if ((login != null) && (userCollection.containsKey(login))) {
            userCollection.remove(login);
            return "Пользователь " + login + " удалён";
        }else return "Не найден такой пользователь";
    }

    public StudyGroup getById(int id){
        return collection.get(id);
    }

    public String getInfo(){
        return "HashMap коллекция, размер: " + collection.size();
    }

    public boolean containsPassportId(String passportId) {
        return passportIdSet.contains(passportId);
    }

    public boolean isEmpty(){
        return collection.isEmpty();
    }

    @Override
    public String toString() {
        String result = "";
        for (StudyGroup s: collection.values().stream().sorted().collect(Collectors.toList())) {
            result += s.toString() + "\n";
        }
        if (isEmpty()) return "Коллекция пуста";
        else return result.trim();

    }
}
