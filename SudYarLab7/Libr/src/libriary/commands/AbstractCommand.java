package libriary.commands;

import libriary.internet.Pack;

import java.io.Serializable;
import java.util.Objects;

/**
 * Класс AbstractCommand содержит основные методы, имя и описание команды
 */

public abstract class AbstractCommand implements Command, Serializable {

    private static final long serialVersionUID = 104L;

    private String name;
    private String descriptionArgument;
    private String description;

    public AbstractCommand(String name, String description){
        this.description = description;
        this.name = name;
    }
    public AbstractCommand(String name,String descriptionArgument, String description){
        this.description = description;
        this.descriptionArgument = descriptionArgument;
        this.name = name;
    }

    public String isValidArgument(String argument) {
        return "VALID";
    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public String getDescriptionArgument() {
        return descriptionArgument;
    }

    public String execute(Pack pack){
        return "";
    }

    @Override
    public String toString() {
        return "AbstractCommand{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }
}
