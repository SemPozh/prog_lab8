package laba6.server.commands;

import laba6.common.data.User;
import laba6.common.exeptions.WrongAmountOfElementsException;
import laba6.server.modules.CollectionManager;
import laba6.server.modules.ResponseOutputer;

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
            ResponseOutputer.appendln("help - show information about commands");
            ResponseOutputer.appendln("info - show information about collection");
            ResponseOutputer.appendln("show - show objects in collection");
            ResponseOutputer.appendln("add - add element in the collection");
            ResponseOutputer.appendln("update id - update element with id");
            ResponseOutputer.appendln("clear - clear collection");
            ResponseOutputer.appendln("execute_script file_name - execute script from file");
            ResponseOutputer.appendln("exit - close program, saves collection");
            ResponseOutputer.appendln("insert_at index - insert object in collection on index=index");
            ResponseOutputer.appendln("add_if_max - add element if it max");
            ResponseOutputer.appendln("reorder - reverse order of the collection");
            ResponseOutputer.appendln("average_of_annual_turnover - show avg of all annual turnovers");
            ResponseOutputer.appendln("min_by_employees_count - object with minimal employees count");
            ResponseOutputer.appendln("print_field_descending_annual_turnover - show all annual turnovers");
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Usage: '" + getName() + " " + getUsage() + "'");
        }
        return false;
    }
}