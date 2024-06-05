package laba7.server.commands;

import laba7.common.data.User;
import laba7.common.exeptions.ConnectionErrorException;
import laba7.common.exeptions.UserNotFoundException;
import laba7.common.exeptions.WrongAmountOfElementsException;
import laba7.server.modules.CollectionManager;
import laba7.server.modules.DatabaseManager;
import laba7.server.modules.ResponseOutputer;

import java.sql.Connection;
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
                Connection connection = DatabaseManager.getConnection();
                collectionManager.getDatabaseManager().authorizeUser(connection, username, password);
                connection.close();
                ResponseOutputer.appendln("You was successfully authorized");
            } catch (ArrayIndexOutOfBoundsException e){
                throw new WrongAmountOfElementsException();
            } catch (UserNotFoundException e) {
                ResponseOutputer.appendln("Incorrect Login/Password");
                return false;
            } catch (SQLException | ConnectionErrorException e) {
                ResponseOutputer.appendln("Server error, try again");
            }
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Usage: '" + getName() + " " + getUsage() + "'");
        }
        return false;
    }
}