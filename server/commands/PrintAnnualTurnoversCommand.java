package laba8.laba8.server.commands;

import laba8.laba8.common.data.User;
import laba8.laba8.common.exeptions.ConnectionErrorException;
import laba8.laba8.common.exeptions.WrongAmountOfElementsException;
import laba8.laba8.server.modules.CollectionManager;
import laba8.laba8.server.modules.ResponseOutputer;

import java.sql.SQLException;

/**
 * Command 'help'. It's here just for logical structure.
 */
public class PrintAnnualTurnoversCommand extends AbstractCommand {
    public PrintAnnualTurnoversCommand() {
        super("print_field_descending_annual_turnover", "", "display all annual turnover");
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
            ResponseOutputer.append("PrintFieldsDescendingAnnualTurnovers");
            ResponseOutputer.appendargs(collectionManager.getAllAnnualTurnovers());
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.append("Using");
            ResponseOutputer.appendargs(getName() + " " + getUsage());
        } catch (SQLException | ConnectionErrorException e) {
            ResponseOutputer.appenderror("DatabaseHandlingException");
            if (!e.getMessage().isEmpty()){
                ResponseOutputer.appendargs(e.getMessage());
            }
        }
        return false;
    }
}