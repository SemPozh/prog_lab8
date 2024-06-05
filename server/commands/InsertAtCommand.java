package laba7.server.commands;


import laba7.common.data.Organization;
import laba7.common.data.User;
import laba7.common.exeptions.ConnectionErrorException;
import laba7.common.exeptions.WrongAmountOfElementsException;
import laba7.server.modules.CollectionManager;
import laba7.server.modules.ResponseOutputer;

import java.sql.SQLException;

/**
 * Command 'add'. Adds a new element to collection.
 */
public class InsertAtCommand extends AbstractCommand {

    public InsertAtCommand() {
        super("insert_at", "<index> {element}", "add a new element to the collection at index index");
    }


    /**
     * Executes the command.
     * @return Command exit status.
     */
    @Override
    public boolean execute(String stringArgument, Object objectArgument, CollectionManager collectionManager, User user) {
        try {
            int index = Integer.parseInt(stringArgument);
            if (stringArgument.isEmpty() || objectArgument == null) throw new WrongAmountOfElementsException();
            Organization organization = (Organization) objectArgument;
            collectionManager.insertAt(index, organization);
            ResponseOutputer.appendln("Organization added successfully!");
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Usage: '" + getName() + " " + getUsage() + "'");
        } catch (ClassCastException exception) {
            ResponseOutputer.appenderror("The object passed by the client is invalid!");
        } catch (NumberFormatException e){
            ResponseOutputer.appenderror("The index must be a number!");
        } catch (IndexOutOfBoundsException e){
            ResponseOutputer.appenderror(e.getMessage());
        }  catch (SQLException | ConnectionErrorException e) {
            ResponseOutputer.appendln("Server error, try again later");
        }
        return false;
    }

}