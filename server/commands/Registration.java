package laba7.server.commands;

import laba7.common.data.User;
import laba7.common.exeptions.ConnectionErrorException;
import laba7.common.exeptions.UserAlreadyExistsException;
import laba7.common.exeptions.WrongAmountOfElementsException;
import laba7.server.modules.CollectionManager;
import laba7.server.modules.DatabaseManager;
import laba7.server.modules.ResponseOutputer;

import java.sql.Connection;
import java.sql.SQLException;

public class Registration extends AbstractCommand {

    public Registration() {
        super("registration", "-", "register new user");
    }


    @Override
    public boolean execute(String stringArgument, Object objectArgument, CollectionManager collectionManager, User user) {
        try {
            if (stringArgument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            try {
                String username = stringArgument.split(":")[0];
                String password = stringArgument.split(":")[1];
                Connection connection = DatabaseManager.getConnection();
                collectionManager.getDatabaseManager().createUser(connection, username, password);
                ResponseOutputer.appendln("You was successfully authorized");
                return true;
            } catch (ArrayIndexOutOfBoundsException e){
                throw new WrongAmountOfElementsException();
            } catch (UserAlreadyExistsException e) {
                ResponseOutputer.appendln(e.getMessage());
            } catch (ConnectionErrorException  e) {
                ResponseOutputer.appendln("Server error, try again later");
            }
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Usage: '" + getName() + " " + getUsage() + "'");
        }
        return false;
    }
}