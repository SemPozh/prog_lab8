package laba6.server.commands;

import laba6.server.modules.CollectionManager;

/**
 * Interface for all commands.
 */
public interface Command {
    String getName();

    String getUsage();

    String getDescription();

    boolean execute(String commandStringArgument, Object commandObjectArgument, CollectionManager collectionManager);
}