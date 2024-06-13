package laba8.laba8.server.commands;

import laba8.laba8.common.data.User;
import laba8.laba8.common.exeptions.CollectionIsEmptyException;
import laba8.laba8.common.exeptions.ConnectionErrorException;
import laba8.laba8.common.exeptions.WrongAmountOfElementsException;
import laba8.laba8.server.modules.CollectionManager;
import laba8.laba8.server.modules.ResponseOutputer;

import java.sql.SQLException;

/**
 * Command 'sum_of_health'. Prints the sum of health of all marines.
 */
public class AVGOfAnnualTurnoverCommand extends AbstractCommand {

    public AVGOfAnnualTurnoverCommand() {
        super("average_of_annual_turnover", "", "display the average of the annualTurnover field values for all collection elements");
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
            double avg = collectionManager.getAvgOfAnnualTurnover();
            if (avg == 0) throw new CollectionIsEmptyException();
            ResponseOutputer.append("AverageOfAnnualTurnover");
            ResponseOutputer.appendargs(String.valueOf(avg));
            collectionManager.loadCollection();
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.append("Using");
            ResponseOutputer.appendargs(getName() + " " + getUsage());
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.appenderror("CollectionIsEmptyException");
        } catch (SQLException | ConnectionErrorException e) {
            ResponseOutputer.append("DatabaseHandlingException");
            if (!e.getMessage().isEmpty()){
                ResponseOutputer.appendargs(e.getMessage());
            }
        }
        return false;
    }
}