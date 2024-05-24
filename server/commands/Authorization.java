package laba7.server.commands;

import laba7.common.data.User;
import laba7.common.exeptions.CollectionIsEmptyException;
import laba7.common.exeptions.WrongAmountOfElementsException;
import laba7.server.modules.CollectionManager;
import laba7.server.modules.DatabaseManager;
import laba7.server.modules.RequestHandler;
import laba7.server.modules.ResponseOutputer;

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
    public boolean execute(String stringArgument, Object objectArgument, CollectionManager collectionManager) {
        try {
            if (stringArgument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            try {
                String username = stringArgument.split(":")[0];
                String password = stringArgument.split(":")[1];
                User user = collectionManager.getDatabaseManager().authorizeUser(username, password);
                if (user == null){
                    ResponseOutputer.appendln("Login/password is incorrect");
                } else {
                    ResponseOutputer.appendln("You was successfully authorized");
                }
                RequestHandler.setUser(user);
            } catch (ArrayIndexOutOfBoundsException e){
                throw new WrongAmountOfElementsException();
            }
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Usage: '" + getName() + " " + getUsage() + "'");
        }
        return false;
    }
}