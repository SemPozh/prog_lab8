package laba8.laba8.server.commands;

import laba8.laba8.client.validators.IDValidator;
import laba8.laba8.common.data.Organization;
import laba8.laba8.common.data.User;
import laba8.laba8.common.exeptions.*;
import laba8.laba8.server.modules.CollectionManager;
import laba8.laba8.server.modules.ResponseOutputer;

import java.sql.SQLException;

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
            if (!organization.getCreatedBy().equals(user)){
                throw new CollectionAccessException("OrganizationRemoveAccessException");
            }
            System.out.println(1);
            collectionManager.removeFromCollection(organization);
            ResponseOutputer.append("OrganizationWasRemoved");
            collectionManager.loadCollection();
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.append("Using");
            ResponseOutputer.appendargs(getName() + " " + getUsage());
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.appenderror("CollectionIsEmptyException");
        } catch (InvalidObjectFieldException exception) {
            ResponseOutputer.appenderror("IDMustBeInteger");
        } catch (OrganizationNotFoundException exception) {
            ResponseOutputer.appenderror("OrganizationNotFoundException");
        } catch (CollectionAccessException e) {
            ResponseOutputer.append(e.getMessage());
        }  catch (SQLException | ConnectionErrorException e) {
            ResponseOutputer.append("DatabaseHandlingException");
            if (!e.getMessage().isEmpty()){
                ResponseOutputer.appendargs(e.getMessage());
            }
        }
        return false;
    }
}