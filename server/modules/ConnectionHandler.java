package laba7.server.modules;

import laba7.common.interaction.Request;
import laba7.common.interaction.Response;
import laba7.common.interaction.ResponseCode;
import laba7.server.App;
import laba7.server.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.*;

public class ConnectionHandler implements Runnable{
    private final Server server;
    private final Socket clientSocket;
    private final CommandManager commandManager;
    private final CollectionManager collectionManager;
    private final ForkJoinPool forkJoinPool;
    private final ExecutorService fixedThreadPool;

    public ConnectionHandler(Server server, Socket clientSocket, CommandManager commandManager, CollectionManager collectionManager, ForkJoinPool forkJoinPool, ExecutorService fixedThreadPool){
        this.server = server;
        this.clientSocket = clientSocket;
        this.commandManager = commandManager;
        this.collectionManager = collectionManager;
        this.forkJoinPool = forkJoinPool;
        this.fixedThreadPool = fixedThreadPool;
    }

    /**
     * Main handling cycle.
     */
    @Override
    public void run() {
        Request userRequest;
        Response responseToUser;
        boolean stopFlag = false;
        try(ObjectInputStream clientReader = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream clientWriter = new ObjectOutputStream(clientSocket.getOutputStream())){
            do {
                userRequest = (Request) clientReader.readObject();
                responseToUser = forkJoinPool.invoke(new RequestHandler(userRequest, collectionManager, commandManager));
                App.logger.info("Request '" + userRequest.getCommandName() + "' handled.");
                Response finalResponseToUser = responseToUser;
                if (!fixedThreadPool.submit(()->{
                    try {
                       clientWriter.writeObject(finalResponseToUser);
                       clientWriter.flush();
                       return true;
                    } catch (IOException e) {
                        System.out.println("An error occurred while sending data to the client!");
                        App.logger.warning("An error occurred while sending data to the client!");
                    }
                    return false;
                }).get()) break;
            } while (responseToUser.getResponseCode() != ResponseCode.SERVER_EXIT && responseToUser.getResponseCode() != ResponseCode.CLIENT_EXIT);

            if (responseToUser.getResponseCode() == ResponseCode.SERVER_EXIT){
                stopFlag = true;
            }
        } catch (ClassNotFoundException e){
            System.out.println("An error occurred while reading received data!");
            App.logger.warning("An error occurred while reading received data!");
        } catch (CancellationException | ExecutionException | InterruptedException exception) {
            System.out.println("A multithreading error occurred while processing the request!");
            App.logger.warning("При обработке запроса произошла ошибка многопоточности!");
        } catch (IOException exception) {
            System.out.println("Unexpected connection loss with the client!");
            App.logger.warning("Unexpected connection loss with the client!");
        } finally {
            try {
                fixedThreadPool.shutdown();
                clientSocket.close();
                System.out.println("The client has been disconnected from the server.");
                App.logger.info("The client has been disconnected from the server.");
            } catch (IOException exception) {
                System.out.println("An error occurred while trying to terminate the connection to the client!");
                App.logger.warning("An error occurred while trying to terminate the connection to the client!");
            }
            if (stopFlag){
                try {
                    collectionManager.getDatabaseManager().closeConnection();
                } catch (SQLException e) {
                    System.out.println("Error while closing connection");
                }
                server.stop();
            }
            server.releaseConnection();
        }
    }
}
