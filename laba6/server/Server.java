package laba6.server;

import laba6.common.exeptions.ClosingSocketException;
import laba6.common.exeptions.ConnectionErrorException;
import laba6.common.exeptions.OpeningServerSocketException;
import laba6.common.interaction.Request;
import laba6.common.interaction.Response;
import laba6.common.interaction.ResponseCode;
import laba6.server.modules.RequestHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Server {
    private int port;
    private ServerSocket serverSocket;
    private int soTimeout;
    private RequestHandler requestReaderModule;


    public Server(int port, int soTimeout, RequestHandler requestReaderModule){
        this.port = port;
        this.soTimeout = soTimeout;
        this.requestReaderModule = requestReaderModule;
    }

    public void run() {
        try {
            openServerSocket();
            boolean processingStatus = true;
            while (processingStatus) {
                try (Socket clientSocket = connectToClient()) {
                    processingStatus = processClientRequest(clientSocket);
                } catch (ConnectionErrorException | SocketTimeoutException exception) {
                    break;
                } catch (IOException exception) {
                    System.out.println("An error occurred while trying to terminate the connection to the client!");
                    App.logger.severe("An error occurred while trying to terminate the connection to the client!");
                }
            }
            stop();
        } catch (OpeningServerSocketException exception) {
            System.out.println("The server cannot be started!");
            App.logger.severe("The server cannot be started!");
        }
    }

    private void stop() {
        try {
            System.out.println("Shutting down the server...");
            App.logger.info("Shutting down the server...");
            if (serverSocket == null) throw new ClosingSocketException();
            serverSocket.close();
            System.out.println("The server has completed successfully.");
            App.logger.finest("The server has completed successfully.");
        } catch (ClosingSocketException exception) {
            System.out.println("It is impossible to shut down a server that has not yet started!");
            App.logger.warning("It is impossible to shut down a server that has not yet started!");
        } catch (IOException exception) {
            System.out.println("An error occurred while shutting down the server!");
            App.logger.severe("An error occurred while shutting down the server!");
        }
    }

    private void openServerSocket() throws OpeningServerSocketException {
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(soTimeout);
        } catch (IllegalArgumentException exception) {
            System.out.println("Port '" + port + "' is beyond the limits of possible values!");
            throw new OpeningServerSocketException();
        } catch (IOException exception) {
            System.out.println("An error occurred while trying to use the port '" + port + "'!");
            App.logger.severe("An error occurred while trying to use the port '" + port + "'!");
            throw new OpeningServerSocketException();
        }
    }

    private Socket connectToClient() throws ConnectionErrorException, SocketTimeoutException {
        try {
            System.out.println("Listening to a port...");
            Socket clientSocket = serverSocket.accept();
            System.out.println("The connection to the server has been successfully established!");
            return clientSocket;
        } catch (SocketTimeoutException exception) {
            System.out.println("Connection timed out!");
            throw new SocketTimeoutException();
        } catch (IOException exception) {
            System.out.println("An error occurred while connecting to the client!");
            throw new ConnectionErrorException();
        }
    }

    private boolean processClientRequest(Socket clientSocket) {
        Request userRequest = null;
        Response responseToUser = null;
        try (ObjectInputStream clientReader = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream clientWriter = new ObjectOutputStream(clientSocket.getOutputStream())) {
            do {
                userRequest = (Request) clientReader.readObject();
                responseToUser = requestReaderModule.handle(userRequest);
//                App.logger.info("Запрос '" + userRequest.getCommandName() + "' успешно обработан.");
                clientWriter.writeObject(responseToUser);
                clientWriter.flush();
            } while (responseToUser.getResponseCode() != ResponseCode.SERVER_EXIT);
            return false;
        } catch (ClassNotFoundException exception) {
            System.out.println("An error occurred while reading received data!");
            App.logger.severe("An error occurred while reading received data!");
        } catch (InvalidClassException | NotSerializableException exception) {
            System.out.println("An error occurred while sending data to the client!");
            App.logger.severe("An error occurred while sending data to the client!");
        } catch (IOException exception) {
            if (userRequest == null) {
                System.out.println("Unexpected connection loss with the client!");
                App.logger.info("Unexpected connection loss with the client!");
            } else {
                System.out.println("The client has been successfully disconnected from the server!");
                App.logger.finest("The client has been successfully disconnected from the server!");
            }
        }
        return true;
    }
}
