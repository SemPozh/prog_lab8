package laba7.server.modules;

import laba7.client.modules.ClientCommandType;
import laba7.common.data.User;
import laba7.common.interaction.*;
import laba7.server.commands.Command;

import java.util.HashMap;

public class RequestHandler {
    private final CollectionManager collectionManager;
    private final CommandManager commandManager;

    private static User user;

    public RequestHandler(CollectionManager collectionManager, CommandManager commandManager){
        this.collectionManager = collectionManager;
        this.commandManager = commandManager;
    }

    public static void setUser(User user) {
        RequestHandler.user = user;
    }

    public CollectionManager getCollectionManager() {
        return collectionManager;
    }

    /**
     * Handles requests.
     *
     * @param request Request to be processed.
     * @return Response to request.
     */
    public Response handle(Request request) {
        ResponseCode responseCode = executeCommand(request.getCommandName(), request.getCommandStringArgument(),
                request.getCommandObjectArgument());
        return new Response(responseCode, ResponseOutputer.getAndClear(), user);
    }

    /**
     * Executes a command from a request.
     *
     * @param commandName               Name of command.
     * @param commandStringArgument String argument for command.
     * @param commandObjectArgument Object argument for command.
     * @return Command execute status.
     */
    private ResponseCode executeCommand(String commandName, String commandStringArgument,
                                        Object commandObjectArgument) {

        HashMap<String, ServerCommandType> commands = commandManager.getCommands();

        Command command = commands.get(commandName).getCommand();
        if (command==null){
            ResponseOutputer.appendln("Command '" + commandName + "' not found. Type 'help' for help.");
            return ResponseCode.ERROR;
        }
        if (command.execute(commandStringArgument, commandObjectArgument, collectionManager)){
            return ResponseCode.OK;
        } else {
            return ResponseCode.ERROR;
        }

    }
}
