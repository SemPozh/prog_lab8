package laba7.server;

import laba7.common.exeptions.ConnectionErrorException;
import laba7.server.modules.CollectionManager;
import laba7.server.modules.CommandManager;
import laba7.server.modules.DatabaseManager;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Main server class. Creates all server instances.
 *
 */
public class App {
    public static final int PORT = 9234;
    private static final int MAX_CLIENTS = 1000;
    public static Logger logger = Logger.getLogger("ServerLogger");
    public static String authFile;

    public static void main(String[] args) {
        try {
            FileHandler logFileHandler = new FileHandler("./target/logging.txt");
            logger.addHandler(logFileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            logFileHandler.setFormatter(formatter);
            logger.setUseParentHandlers(false);

        } catch (SecurityException | IOException e) {

            System.out.println("Logger loaded incorrectly");
        }

        if (args.length==1) {
            authFile = args[0];
            DatabaseManager databaseManager = null;
            try {
                databaseManager = new DatabaseManager(authFile);
            } catch (ConnectionErrorException e) {
                System.out.println("Can't connect to database, program can't be executed");
            }
            try {
                CollectionManager collectionManager = new CollectionManager(databaseManager);
                CommandManager commandManager = new CommandManager();
                Server server = new Server(PORT, MAX_CLIENTS, commandManager, collectionManager);
                server.run();
            }catch (ConnectionErrorException | SQLException e) {
                System.out.println("Connection problems. Make sure that auth file is correct");
            }
        } else {
            System.out.println("Give name of the file in command string args!\nUsage: java -jar <jar_file.jar> <db_auth_file>");
        }
    }
}