package laba6.server.commands;

import laba6.common.data.User;
import laba6.common.exeptions.CollectionIsEmptyException;
import laba6.common.exeptions.UserNotFoundException;
import laba6.common.exeptions.WrongAmountOfElementsException;
import laba6.server.modules.CollectionManager;
import laba6.server.modules.DatabaseManager;
import laba6.server.modules.RequestHandler;
import laba6.server.modules.ResponseOutputer;

import java.sql.SQLException;

public class Authorization extends AbstractCommand {

    public Authorization() {
        super("authorization", "-", "authorizes already exists user");
    }

    /**
     * Executes the command.
     *
     * @return Command exit status.
     */
    @Override
    public boolean execute(String stringArgument, Object objectArgument, CollectionManager collectionManager, User user) {
        try {
            if (stringArgument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            try {
                String username = stringArgument.split(":")[0];
                String password = stringArgument.split(":")[1];
                collectionManager.getDatabaseManager().authorizeUser(username, password);
                ResponseOutputer.appendln("You was successfully authorized");
            } catch (ArrayIndexOutOfBoundsException e){
                throw new WrongAmountOfElementsException();
            } catch (UserNotFoundException e) {
                ResponseOutputer.appendln("Incorrect Login/Password");
                return false;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Usage: '" + getName() + " " + getUsage() + "'");
        }
        return false;
    }
}