package libriary.commands;

import libriary.internet.Pack;

/**
 *
 * Интерфейс Command
 *
 */

public interface Command {
    String getDescription();
    String getName();
    String getDescriptionArgument();
    String execute(Pack pack);
    String isValidArgument(String argument);
}
