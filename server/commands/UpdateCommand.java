package laba7.server.commands;

import laba7.client.validators.IDValidator;
import laba7.common.data.Organization;
import laba7.common.exeptions.CollectionIsEmptyException;
import laba7.common.exeptions.InvalidObjectFieldException;
import laba7.common.exeptions.OrganizationNotFoundException;
import laba7.common.exeptions.WrongAmountOfElementsException;
import laba7.server.modules.CollectionManager;
import laba7.server.modules.ResponseOutputer;

/**
 * Command 'update'. Updates the information about selected marine.
 */
public class UpdateCommand extends AbstractCommand {

    public UpdateCommand() {
        super("update", "<ID> {element}", "update the value of a collection element by ID");
    }

    /**
     * Executes the command.
     *
     * @return Command exit status.
     */
    @Override
    public boolean execute(String stringArgument, Object objectArgument, CollectionManager collectionManager) {
        try {
            if (stringArgument.isEmpty() || objectArgument == null) throw new WrongAmountOfElementsException();
            if (collectionManager.collectionSize() == 0) throw new CollectionIsEmptyException();

            IDValidator idValidator = new IDValidator();
            Integer id = idValidator.validate(stringArgument);
            if (id <= 0) throw new NumberFormatException();
            Organization oldOrganization = collectionManager.getById(id);
            if (oldOrganization == null) throw new OrganizationNotFoundException();

            Organization organization = (Organization) objectArgument;
            oldOrganization.setName(organization.getName());
            oldOrganization.setCoordinates(organization.getCoordinates());
            oldOrganization.setAnnualTurnover(organization.getAnnualTurnover());
            oldOrganization.setEmployeesCount(organization.getEmployeesCount());
            oldOrganization.setType(organization.getType());
            oldOrganization.setOfficialAddress(organization.getOfficialAddress());

            ResponseOutputer.appendln("The organization has been successfully changed!");
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Usage: '" + getName() + " " + getUsage() + "'");
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.appenderror("The collection is empty!");
        } catch (InvalidObjectFieldException exception) {
            ResponseOutputer.appenderror("ID must be represented as a positive number!");
        } catch (OrganizationNotFoundException exception) {
            ResponseOutputer.appenderror("There is no soldier with this ID in the collection!");
        } catch (ClassCastException exception) {
            ResponseOutputer.appenderror("The object passed by the client is invalid!");
        }
        return false;
    }
}