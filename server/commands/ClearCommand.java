package laba6.server.commands;

import laba6.common.data.User;
import laba6.common.exeptions.WrongAmountOfElementsException;
import laba6.server.modules.CollectionManager;
import laba6.server.modules.ResponseOutputer;

import java.sql.SQLException;

/**
 * Command 'clear'. Clears the collection.
 */
public class ClearCommand extends AbstractCommand {

    public ClearCommand() {
        super("clear", "", "clear collection");
    }

    /**
     * Executes the command.
     *
     * @return Command exit status.
     */
    @Override
    public boolean execute(String stringArgument, Object objectArgument, CollectionManager collectionManager, User user) {
        try {
            if (!stringArgument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            collectionManager.clearCollection(user);
            ResponseOutputer.appendln("Collection cleared!");
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Usage: '" + getName() + " " + getUsage() + "'");
        } catch (SQLException e){
            ResponseOutputer.appendln("Database server error, sorry, try again later...");
        }
        return false;
    }
}