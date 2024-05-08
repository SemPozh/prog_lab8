package laba6.server.modules;

import laba6.common.interaction.*;
import laba6.server.commands.Command;

import java.util.HashMap;

public class RequestHandler {
    private final CollectionManager collectionManager;

    public RequestHandler(CollectionManager collectionManager){
        this.collectionManager = collectionManager;
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
        return new Response(responseCode, ResponseOutputer.getAndClear());
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
        Commands commandsList = new Commands();
        HashMap<String, CommandType> commands = commandsList.getCommands();

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
