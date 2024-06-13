package laba8.laba8.server.commands;

import laba8.laba8.common.data.User;
import laba8.laba8.common.exeptions.CollectionIsEmptyException;
import laba8.laba8.common.exeptions.ConnectionErrorException;
import laba8.laba8.common.exeptions.WrongAmountOfElementsException;
import laba8.laba8.server.modules.CollectionManager;
import laba8.laba8.server.modules.ResponseOutputer;

import java.sql.SQLException;

/**
 * Command 'max_by_melee_weapon'. Prints the element of the collection with maximum melee weapon.
 */
public class MinByEmployeesCountCommand extends AbstractCommand {

    public MinByEmployeesCountCommand() {
        super("max_by_melee_weapon", "", "display the element whose employees count field value is minimal");
    }

    /**
     * Executes the command.
     *
     * @return Command exit status.
     */
    @Override
    public boolean execute(String stringArgument, Object objectArgument, CollectionManager collectionManager, User user) {
        try {
            if (!stringArgument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            collectionManager.loadCollection();
            ResponseOutputer.append("MinByEmployeesCount");
            ResponseOutputer.appendargs(String.valueOf(collectionManager.minByEmployeesCount()));

            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.append("Using");
            ResponseOutputer.appendargs(getName() + " " + getUsage());
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.appenderror("CollectionIsEmptyException");
        } catch (SQLException | ConnectionErrorException e) {
            ResponseOutputer.appenderror("DatabaseHandlingException");
            if (!e.getMessage().isEmpty()){
                ResponseOutputer.appendargs(e.getMessage());
            }
        }
        return true;
    }
}