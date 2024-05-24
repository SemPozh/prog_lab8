package laba6.server.commands;

import laba6.client.validators.IDValidator;
import laba6.common.data.Organization;
import laba6.common.data.User;
import laba6.common.exeptions.*;
import laba6.server.modules.CollectionManager;
import laba6.server.modules.ResponseOutputer;

/**
 * Command 'remove_by_id'. Removes the element by its ID.
 */
public class RemoveByIdCommand extends AbstractCommand {

    public RemoveByIdCommand() {
        super("remove_by_id", "<ID>", "remove an element from a collection by ID");
    }

    /**
     * Executes the command.
     *
     * @return Command exit status.
     */
    @Override
    public boolean execute(String stringArgument, Object objectArgument, CollectionManager collectionManager, User user) {
        try {
            if (stringArgument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            if (collectionManager.collectionSize() == 0) throw new CollectionIsEmptyException();
            IDValidator idValidator = new IDValidator();
            Integer id = idValidator.validate(stringArgument);
            Organization organization = collectionManager.getById(id);
            if (organization == null) throw new OrganizationNotFoundException();
            if (organization.getCreatedBy() != user){
                throw new CollectionAccessException("You are not owner of this Organization and can't remove it!");
            }
            collectionManager.removeFromCollection(organization);
            ResponseOutputer.appendln("Organization deleted successfully");
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Usage: '" + getName() + " " + getUsage() + "'");
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.appenderror("Collection is empty");
        } catch (InvalidObjectFieldException exception) {
            ResponseOutputer.appenderror("ID must be represented by a number!");
        } catch (OrganizationNotFoundException exception) {
            ResponseOutputer.appenderror("There is no organization with this ID in the collection!");
        } catch (CollectionAccessException e) {
            ResponseOutputer.appendln(e.getMessage());
        }
        return false;
    }
}