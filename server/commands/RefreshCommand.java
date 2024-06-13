package laba8.laba8.server.commands;

import laba8.laba8.common.data.User;
import laba8.laba8.common.exeptions.WrongAmountOfElementsException;
import laba8.laba8.server.modules.CollectionManager;
import laba8.laba8.server.modules.ResponseOutputer;

public class RefreshCommand extends AbstractCommand{
    public RefreshCommand() {
        super("refresh", "", "~internal command~");
    }

    /**
     * Executes the command.
     *
     * @return Command exit status.
     */

    @Override
    public boolean execute(String commandStringArgument, Object commandObjectArgument, CollectionManager collectionManager, User user) {
        try {
            if (!commandStringArgument.isEmpty() || commandObjectArgument != null) throw new WrongAmountOfElementsException();
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.append("Using");
            ResponseOutputer.appendargs(getName() + " " + getUsage());
        }
        return false;
    }
}
