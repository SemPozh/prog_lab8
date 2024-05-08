package laba6.server.commands;


import laba6.common.data.Organization;
import laba6.common.exeptions.WrongAmountOfElementsException;
import laba6.server.modules.CollectionManager;
import laba6.server.modules.ResponseOutputer;

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
    public boolean execute(String stringArgument, Object objectArgument, CollectionManager collectionManager) {
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
        }
        return false;
    }

}