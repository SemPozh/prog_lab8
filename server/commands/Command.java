package laba8.laba8.server.commands;

import laba8.laba8.common.data.User;
import laba8.laba8.server.modules.CollectionManager;

/**
 * Interface for all commands.
 */
public interface Command {
    String getName();

    String getUsage();

    String getDescription();

    boolean execute(String commandStringArgument, Object commandObjectArgument, CollectionManager collectionManager, User user);
}