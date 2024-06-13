package laba8.laba8.server.commands;

import laba8.laba8.client.validators.IDValidator;
import laba8.laba8.common.data.Organization;
import laba8.laba8.common.data.User;
import laba8.laba8.common.exeptions.*;
import laba8.laba8.server.modules.CollectionManager;
import laba8.laba8.server.modules.DatabaseManager;
import laba8.laba8.server.modules.ResponseOutputer;

import java.sql.Connection;
import java.sql.SQLException;

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
    public boolean execute(String stringArgument, Object objectArgument, CollectionManager collectionManager, User user) {
        try {
            if (stringArgument.isEmpty() || objectArgument == null) throw new WrongAmountOfElementsException();
            if (collectionManager.collectionSize() == 0) throw new CollectionIsEmptyException();
            IDValidator idValidator = new IDValidator();
            Integer id = idValidator.validate(stringArgument);
            if (id <= 0) throw new NumberFormatException();
            Organization oldOrganization = collectionManager.getById(id);
            if (oldOrganization == null) throw new OrganizationNotFoundException();
            if (!oldOrganization.getCreatedBy().equals(user)){
                throw new CollectionAccessException("You are not owner of this organization and not allowed to update it!");
            }
            Organization organization = (Organization) objectArgument;
            Connection connection = DatabaseManager.getConnection();
            collectionManager.getDatabaseManager().updateOrganization(connection, oldOrganization, organization);
            connection.close();
            oldOrganization.setName(organization.getName());
            oldOrganization.setCoordinates(organization.getCoordinates());
            oldOrganization.setAnnualTurnover(organization.getAnnualTurnover());
            oldOrganization.setEmployeesCount(organization.getEmployeesCount());
            oldOrganization.setType(organization.getType());
            oldOrganization.setOfficialAddress(organization.getOfficialAddress());

            ResponseOutputer.append("OrganizationWasUpdated");
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
        } catch (ClassCastException exception) {
            ResponseOutputer.appenderror("ClientObjectException");
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