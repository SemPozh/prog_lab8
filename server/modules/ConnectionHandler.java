package laba6.server.modules;

import laba6.common.interaction.Request;
import laba6.common.interaction.Response;
import laba6.common.interaction.ResponseCode;
import laba6.server.App;
import laba6.server.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.*;

public class ConnectionHandler implements Runnable{
    private Server server;
    private Socket clientSocket;
    private CommandManager commandManager;
    private CollectionManager collectionManager;
    private ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
    private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(1);

    public ConnectionHandler(Server server, Socket clientSocket, CommandManager commandManager, CollectionManager collectionManager){
        this.server = server;
        this.clientSocket = clientSocket;
        this.commandManager = commandManager;
        this.collectionManager = collectionManager;
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
            if (stopFlag) server.stop();
            server.releaseConnection();
        }
    }
}
