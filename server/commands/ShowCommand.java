package laba8.laba8.server.commands;

import laba8.laba8.common.data.User;
import laba8.laba8.common.exeptions.ConnectionErrorException;
import laba8.laba8.common.exeptions.WrongAmountOfElementsException;
import laba8.laba8.server.modules.CollectionManager;
import laba8.laba8.server.modules.ResponseOutputer;

import java.sql.SQLException;

/**
 * Command 'show'. Shows information about all elements of the collection.
 */
public class ShowCommand extends AbstractCommand {

    public ShowCommand() {
        super("show", "", "display all elements of a collection");
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
            ResponseOutputer.append(collectionManager.showCollection());
            collectionManager.loadCollection();
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.append("Usage: '" + getName() + " " + getUsage() + "'");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ConnectionErrorException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
