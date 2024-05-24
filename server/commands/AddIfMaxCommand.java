package laba6.server.commands;

import laba6.common.data.Organization;
import laba6.common.data.User;
import laba6.common.exeptions.WrongAmountOfElementsException;
import laba6.server.modules.CollectionManager;
import laba6.server.modules.ResponseOutputer;

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
                ResponseOutputer.appendln("Organization added successfully!");
                return true;
            } else ResponseOutputer.appenderror("The value of the organization is less than the value of the largest organization!");
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Usage: '" + getName() + " " + getUsage() + "'");
        } catch (ClassCastException exception) {
            ResponseOutputer.appenderror("The object passed by the client is invalid!");
        }
        return false;
    }
}