package laba6.server;

import laba6.common.exeptions.IncorrectAuthFileException;
import laba6.server.modules.CollectionManager;
import laba6.server.modules.CommandManager;
import laba6.server.modules.DatabaseManager;
import laba6.server.modules.RequestHandler;

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
    public static final int PORT = 1488;
    private static final int MAX_CLIENTS = 1000;
    public static final int CONNECTION_TIMEOUT = 80 * 100000;
    public static Logger logger = Logger.getLogger("ServerLogger");
    public static String fileName;
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
            String authFileName = args[0];
            DatabaseManager databaseManager = new DatabaseManager("jdbc:postgresql://pg:5432/studs", authFileName);
            try {
                databaseManager.connectToDatabase();
            } catch (IncorrectAuthFileException e) {
                System.out.println(e.getMessage());
                System.out.println("AuthFile: '<login>:<password>'");
            } catch (SQLException e) {
                System.out.println("Can't connect to database, program can't be executed");
            }
            CollectionManager collectionManager = new CollectionManager(databaseManager);
            CommandManager commandManager = new CommandManager();
            Server server = new Server(PORT, MAX_CLIENTS, commandManager, collectionManager);
            server.run();
        } else {
            System.out.println("Give name of the file in command string args!\nUsage: java -jar <jar_file.jar> <db_auth_file>");
        }
    }
}