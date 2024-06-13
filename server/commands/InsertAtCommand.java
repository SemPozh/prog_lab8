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
            ResponseOutputer.append("OrganizationWasAdded");
            collectionManager.loadCollection();
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.append("Using");
            ResponseOutputer.appendargs(getName() + " " + getUsage());
        } catch (ClassCastException exception) {
            ResponseOutputer.appenderror("ClientObjectException");
        } catch (NumberFormatException e){
            ResponseOutputer.appenderror("IndexTypeException");
        } catch (IndexOutOfBoundsException e){
            ResponseOutputer.appenderror("IndexOutOfBoundsException");
        }  catch (SQLException | ConnectionErrorException e) {
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