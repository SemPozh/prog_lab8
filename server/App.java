package laba6.server;

import laba6.server.modules.CollectionFileManager;
import laba6.server.modules.CollectionManager;
import laba6.server.modules.RequestHandler;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Main server class. Creates all server instances.
 *
 */
public class App {
    public static final int PORT = 1488;
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
            fileName = args[0];
            CollectionFileManager collectionFileManager = new CollectionFileManager(fileName);
            CollectionManager collectionManager = new CollectionManager(collectionFileManager);
            RequestHandler requestHandler = new RequestHandler(collectionManager);
            Server server = new Server(PORT, CONNECTION_TIMEOUT, requestHandler);
            server.run();
        } else {
            System.out.println("Give name of the file in command string args!");
        }
    }
}