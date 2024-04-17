package laba6.server.commands;

import laba6.client.validators.IDValidator;
import laba6.client.validators.NameValidator;
import laba6.common.data.Address;
import laba6.common.data.Coordinates;
import laba6.common.data.Organization;
import laba6.common.data.OrganizationType;
import laba6.common.exeptions.CollectionIsEmptyException;
import laba6.common.exeptions.InvalidObjectFieldException;
import laba6.common.exeptions.OrganizationNotFoundException;
import laba6.common.exeptions.WrongAmountOfElementsException;
import laba6.server.modules.CollectionManager;
import laba6.server.modules.ResponseOutputer;

/**
 * Command 'update'. Updates the information about selected marine.
 */
public class UpdateCommand extends AbstractCommand {
    private CollectionManager collectionManager;

    public UpdateCommand(CollectionManager collectionManager) {
        super("update", "<ID> {element}", "update the value of a collection element by ID");
        this.collectionManager = collectionManager;
    }

    /**
     * Executes the command.
     *
     * @return Command exit status.
     */
    @Override
    public boolean execute(String stringArgument, Object objectArgument) {
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