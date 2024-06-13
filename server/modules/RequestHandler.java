package laba8.laba8.server.modules;


import laba8.laba8.common.data.User;
import laba8.laba8.common.interaction.Request;
import laba8.laba8.common.interaction.Response;
import laba8.laba8.common.interaction.ResponseCode;
import laba8.laba8.server.commands.Command;

import java.util.HashMap;
import java.util.concurrent.RecursiveTask;

public class RequestHandler extends RecursiveTask<Response> {
    private final CollectionManager collectionManager;
    private final CommandManager commandManager;
    private final Request request;


    public RequestHandler(Request request, CollectionManager collectionManager, CommandManager commandManager){
        this.request = request;
        this.collectionManager = collectionManager;
        this.commandManager = commandManager;
    }


    public CollectionManager getCollectionManager() {
        return collectionManager;
    }

    @Override
    public Response compute() {
        User hashedUser = request.getUser();
        ResponseCode responseCode = executeCommand(request.getCommandName(), request.getCommandStringArgument(),
                request.getCommandObjectArgument(), hashedUser);
        return new Response(responseCode, ResponseOutputer.getAndClear(), collectionManager.getOrganizationCollection(), ResponseOutputer.getArgsAndClear());
    }
    /**
     * Executes a command from a request.
     *
     * @param commandName               Name of command.
     * @param commandStringArgument String argument for command.
     * @param commandObjectArgument Object argument for command.
     * @return Command execute status.
     */
    private synchronized ResponseCode executeCommand(String commandName, String commandStringArgument,
                                        Object commandObjectArgument, User user) {
        HashMap<String, ServerCommandType> commands = commandManager.getCommands();

        Command command = commands.get(commandName).getCommand();

        if (command==null){
            ResponseOutputer.append("CommandNotFoundException");
            return ResponseCode.ERROR;
        }
        if (command.execute(commandStringArgument, commandObjectArgument, collectionManager, user)){
            return ResponseCode.OK;
        } else {
            return ResponseCode.ERROR;
        }

    }
}
