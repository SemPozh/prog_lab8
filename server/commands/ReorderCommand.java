package laba8.laba8.server.commands;

import laba8.laba8.common.data.User;
import laba8.laba8.common.exeptions.ConnectionErrorException;
import laba8.laba8.common.exeptions.WrongAmountOfElementsException;
import laba8.laba8.server.modules.CollectionManager;
import laba8.laba8.server.modules.ResponseOutputer;

import java.sql.SQLException;

public class ReorderCommand extends AbstractCommand {

    public ReorderCommand() {
        super("reorder", "", "change the order of the collection");
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
            collectionManager.reorder();
            ResponseOutputer.append("The collection is upside down");
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