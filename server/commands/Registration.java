package laba6.server.commands;

import laba6.common.data.User;
import laba6.common.exeptions.UserAlreadyExistsException;
import laba6.common.exeptions.WrongAmountOfElementsException;
import laba6.server.modules.CollectionManager;
import laba6.server.modules.RequestHandler;
import laba6.server.modules.ResponseOutputer;

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
                User newUser = collectionManager.getDatabaseManager().createUser(username, password);
                ResponseOutputer.appendln("You was successfully authorized");
                RequestHandler.setUser(newUser);
            } catch (ArrayIndexOutOfBoundsException e){
                throw new WrongAmountOfElementsException();
            } catch (UserAlreadyExistsException e) {
                ResponseOutputer.appendln(e.getMessage());
            }
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Usage: '" + getName() + " " + getUsage() + "'");
        }
        return false;
    }
}