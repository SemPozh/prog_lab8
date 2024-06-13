package laba8.laba8.server.commands;

import laba8.laba8.common.data.User;
import laba8.laba8.common.exeptions.WrongAmountOfElementsException;
import laba8.laba8.server.modules.CollectionManager;
import laba8.laba8.server.modules.ResponseOutputer;

/**
 * Command 'help'. It's here just for logical structure.
 */
public class HelpCommand extends AbstractCommand {

    public HelpCommand() {
        super("help", "", "display help on available commands");
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
            ResponseOutputer.append("help - show information about commands");
            ResponseOutputer.append("info - show information about collection");
            ResponseOutputer.append("show - show objects in collection");
            ResponseOutputer.append("add - add element in the collection");
            ResponseOutputer.append("update id - update element with id");
            ResponseOutputer.append("clear - clear collection");
            ResponseOutputer.append("execute_script file_name - execute script from file");
            ResponseOutputer.append("exit - close program, saves collection");
            ResponseOutputer.append("insert_at index - insert object in collection on index=index");
            ResponseOutputer.append("add_if_max - add element if it max");
            ResponseOutputer.append("reorder - reverse order of the collection");
            ResponseOutputer.append("average_of_annual_turnover - show avg of all annual turnovers");
            ResponseOutputer.append("min_by_employees_count - object with minimal employees count");
            ResponseOutputer.append("print_field_descending_annual_turnover - show all annual turnovers");
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.append("Using");
            ResponseOutputer.appendargs(getName() + " " + getUsage());
        }
        return false;
    }
}