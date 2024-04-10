package laba6.server;

import laba6.server.commands.*;
import laba6.server.modules.CollectionFileManager;
import laba6.server.modules.CollectionManager;
import laba6.server.modules.CommandManager;
import laba6.server.modules.RequestHandler;

import java.util.logging.Logger;

/**
 * Main server class. Creates all server instances.
 *
 */
public class App {
    public static final int PORT = 1821;
    public static final int CONNECTION_TIMEOUT = 80 * 1000;
//    public static Logger logger = LogManager.getLogger("ServerLogger");
    public static final String ENV_VARIABLE = "LABA6_FILE";
    public static void main(String[] args) {


        CollectionFileManager collectionFileManager = new CollectionFileManager(ENV_VARIABLE);
        CollectionManager collectionManager = new CollectionManager(collectionFileManager);
        CommandManager commandManager = new CommandManager(
                new HelpCommand(),
                new InfoCommand(collectionManager),
                new ShowCommand(collectionManager),
                new AddCommand(collectionManager),
                new UpdateCommand(collectionManager),
                new RemoveByIdCommand(collectionManager),
                new ClearCommand(collectionManager),
                new SaveCommand(collectionManager),
                new ExitCommand(),
                new ExecuteScriptCommand(),
                new AddIfMaxCommand(collectionManager),
                new ReorderCommand(collectionManager),
                new AVGOfAnnualTurnoverCommand(collectionManager),
                new MinByEmployeesCountCommand(collectionManager),
                new PrintAnnualTurnoversCommand(collectionManager),
                new InsertAtCommand(collectionManager),
                new ServerExitCommand()
        );
        RequestHandler requestHandler = new RequestHandler(commandManager);
        Server server = new Server(PORT, CONNECTION_TIMEOUT, requestHandler);
        server.run();
    }
}