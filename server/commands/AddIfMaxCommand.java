package laba8.laba8.server.commands;

import laba8.laba8.common.data.Organization;
import laba8.laba8.common.data.User;
import laba8.laba8.common.exeptions.ConnectionErrorException;
import laba8.laba8.common.exeptions.UserNotFoundException;
import laba8.laba8.common.exeptions.WrongAmountOfElementsException;
import laba8.laba8.server.modules.CollectionManager;
import laba8.laba8.server.modules.ResponseOutputer;

import java.sql.SQLException;

/**
 * Command 'add_if_min'. Adds a new element to collection if it's less than the minimal one.
 */
public class AddIfMaxCommand extends AbstractCommand {

    public AddIfMaxCommand() {
        super("add_if_max", "{element}", "add a new element if its value is less than the smallest one");
    }

    /**
     * Executes the command.
     *
     * @return Command exit status.
     */
    @Override
    public boolean execute(String stringArgument, Object objectArgument, CollectionManager collectionManager, User user) {
        try {
            if (!stringArgument.isEmpty() || objectArgument == null) throw new WrongAmountOfElementsException();
            Organization organization = (Organization) objectArgument;
            if (collectionManager.collectionSize() == 0 || organization.compareTo(collectionManager.getLast()) < 0) {
                collectionManager.addToCollection(organization);
                ResponseOutputer.append("OrganizationWasAdded");
                collectionManager.loadCollection();
                return true;
            } else ResponseOutputer.appenderror("OrganizationSizeException");
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.append("Using");
            ResponseOutputer.appendargs(getName() + " " + getUsage());
        } catch (ClassCastException exception) {
            ResponseOutputer.appenderror("ClientObjectException");
        } catch (SQLException | ConnectionErrorException e) {
            ResponseOutputer.append("DatabaseHandlingException");
            if (!e.getMessage().isEmpty()){
                ResponseOutputer.appendargs(e.getMessage());
            }
        } catch (UserNotFoundException e) {
            ResponseOutputer.appenderror("UserNotFoundException");
        }
        return false;
    }
}