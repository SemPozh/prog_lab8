package laba7.server.commands;

import laba7.common.data.User;
import laba7.common.exeptions.UserAlreadyExistsException;
import laba7.common.exeptions.WrongAmountOfElementsException;
import laba7.server.modules.CollectionManager;
import laba7.server.modules.RequestHandler;
import laba7.server.modules.ResponseOutputer;

public class Registration extends AbstractCommand {

    public Registration() {
        super("registration", "-", "register new user");
    }


    @Override
    public boolean execute(String stringArgument, Object objectArgument, CollectionManager collectionManager) {
        try {
            if (stringArgument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            try {
                String username = stringArgument.split(":")[0];
                String password = stringArgument.split(":")[1];
                User user = collectionManager.getDatabaseManager().createUser(username, password);
                ResponseOutputer.appendln("You was successfully authorized");
                RequestHandler.setUser(user);
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