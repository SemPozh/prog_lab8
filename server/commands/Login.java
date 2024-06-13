package laba8.laba8.server.commands;

import laba8.laba8.common.data.User;
import laba8.laba8.common.exeptions.ConnectionErrorException;
import laba8.laba8.common.exeptions.UserNotFoundException;
import laba8.laba8.common.exeptions.WrongAmountOfElementsException;
import laba8.laba8.server.modules.CollectionManager;
import laba8.laba8.server.modules.DatabaseManager;
import laba8.laba8.server.modules.ResponseOutputer;

import java.sql.Connection;
import java.sql.SQLException;

public class Login extends AbstractCommand {

    public Login() {
        super("login", "-", "authorizes already exists user");
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
            try {
                String username = user.getUsername();
                String password = user.getPassword();
                Connection connection = DatabaseManager.getConnection();
                collectionManager.getDatabaseManager().authorizeUser(connection, username, password);
                connection.close();
                ResponseOutputer.append("AuthorizeSuccess");
            } catch (ArrayIndexOutOfBoundsException e){
                throw new WrongAmountOfElementsException();
            } catch (UserNotFoundException e) {
                ResponseOutputer.append("IncorrectLoginOrPasswordException");
                return false;
            } catch (SQLException | ConnectionErrorException e) {
                ResponseOutputer.append("DatabaseHandlingException");
                if (!e.getMessage().isEmpty()){
                    ResponseOutputer.appendargs(e.getMessage());
                }
            }
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.append("Using");
            ResponseOutputer.appendargs(getName() + " " + getUsage());
        }
        return false;
    }
}