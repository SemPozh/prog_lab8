package laba7.server.commands;

import laba7.common.exeptions.CollectionIsEmptyException;
import laba7.common.exeptions.WrongAmountOfElementsException;
import laba7.server.modules.CollectionManager;
import laba7.server.modules.ResponseOutputer;

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
    public boolean execute(String stringArgument, Object objectArgument, CollectionManager collectionManager) {
        try {
            if (!stringArgument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            double avg = collectionManager.getAvgOfAnnualTurnover();
            if (avg == 0) throw new CollectionIsEmptyException();
            ResponseOutputer.appendln("Average annual revenue for the collection: " + avg);
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Usage: '" + getName() + " " + getUsage() + "'");
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.appenderror("Collection is empty!");
        }
        return false;
    }
}