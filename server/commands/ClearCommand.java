package laba8.laba8.server.commands;

import laba8.laba8.common.data.User;
import laba8.laba8.common.exeptions.ConnectionErrorException;
import laba8.laba8.common.exeptions.UserNotFoundException;
import laba8.laba8.common.exeptions.WrongAmountOfElementsException;
import laba8.laba8.server.modules.CollectionManager;
import laba8.laba8.server.modules.ResponseOutputer;

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
            ResponseOutputer.append("CollectionCleared");
            collectionManager.loadCollection();
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.append("Using");
            ResponseOutputer.appendargs(getName() + " " + getUsage());
        } catch (SQLException | ConnectionErrorException e){
            ResponseOutputer.append("DatabaseHandlingException");
            if (!e.getMessage().isEmpty()){
                ResponseOutputer.appendargs(e.getMessage());
            }
        } catch (UserNotFoundException e){
            ResponseOutputer.append("UserNotFoundException");

        }
        return false;
    }
}