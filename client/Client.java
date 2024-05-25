package laba6.client;

import laba6.client.modules.UserHandler;
import laba6.common.data.User;
import laba6.common.exeptions.ConnectionErrorException;
import laba6.common.exeptions.NotInDeclaredLimitsException;
import laba6.common.interaction.Request;
import laba6.common.interaction.Response;
import laba6.common.interaction.ResponseCode;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class Client {
    private final String host;
    private final int port;
    private final int reconnectionTimeout;
    private int reconnectionAttempts;
    private final int maxReconnectionAttempts;
    private final UserHandler userHandler;
    private SocketChannel socketChannel;
    private ObjectOutputStream serverWriter;
    private ObjectInputStream serverReader;
    private User user;

    public Client(String host, int port, int reconnectionTimeout, int maxReconnectionAttempts, UserHandler userHandler) {
        this.host = host;
        this.port = port;
        this.reconnectionTimeout = reconnectionTimeout;
        this.maxReconnectionAttempts = maxReconnectionAttempts;
        this.userHandler = userHandler;
    }

    public void run() {
        try {
            boolean processingStatus = true;
            while (processingStatus) {
                try {
                    connectToServer();
                    authenticateUserProcess();
                    processingStatus = processRequestToServer();
                } catch (ConnectionErrorException exception) {
                    if (reconnectionAttempts >= maxReconnectionAttempts) {
                        System.out.println("The number of connection attempts has been exceeded!");
                        break;
                    }
                    try {
                        Thread.sleep(reconnectionTimeout);
                    } catch (IllegalArgumentException timeoutException) {
                        System.out.println("Connection timeout '" + reconnectionTimeout +
                                "' is beyond the limits of possible values!");
                        System.out.println("reconnection will be made immediately.");
                    } catch (Exception timeoutException) {
                        System.out.println("An error occurred while trying to wait for a connection!");
                        System.out.println("Reconnection will be made immediately.");
                    }
                }
                reconnectionAttempts++;
            }
            if (socketChannel != null) socketChannel.close();
            System.out.println("The client's work has completed successfully.");
        } catch (NotInDeclaredLimitsException exception) {
            System.out.println("The client cannot be started!");
        } catch (IOException exception) {
            System.out.println("An error occurred while trying to complete the connection to the server!");
        }
    }

    /**
     * Connecting to server.
     */
    private void connectToServer() throws ConnectionErrorException, NotInDeclaredLimitsException {
        try {
            if (reconnectionAttempts >= 1) System.out.println("Reconnecting to the server...");
            InetSocketAddress inetSocketAddress = new InetSocketAddress(host, port);
            socketChannel = SocketChannel.open(inetSocketAddress);
            System.out.println("The connection to the server was successfully established.");
            System.out.println("Waiting for permission to share data...");
            serverWriter = new ObjectOutputStream(socketChannel.socket().getOutputStream());
            serverReader = new ObjectInputStream(socketChannel.socket().getInputStream());
            System.out.println("Permission to share data has been received.");
        } catch (IllegalArgumentException exception) {
            System.out.println("The server address was entered incorrectly!");
            throw new NotInDeclaredLimitsException();
        } catch (IOException exception) {
            System.out.println("An error occurred while connecting to the server!");
            throw new ConnectionErrorException();
        }
    }

    /**
     * Server request process.
     */
    private boolean processRequestToServer() {
        Request requestToServer = null;
        Response serverResponse = null;
        do {
            try {
                requestToServer = serverResponse != null ? userHandler.handle(serverResponse.getResponseCode(), user) :
                        userHandler.handle(null, user);
                if (requestToServer.isEmpty()) continue;
                serverWriter.writeObject(requestToServer);
                serverResponse = (Response) serverReader.readObject();
                System.out.println(serverResponse.getResponseBody());
            } catch (InvalidClassException | NotSerializableException exception) {
                System.out.println("An error occurred while sending data to the server!");
            } catch (ClassNotFoundException exception) {
                System.out.println("An error occurred while reading received data!");
            } catch (IOException exception) {
                System.out.println("The connection to the server has been lost!");
                try {
                    reconnectionAttempts++;
                    connectToServer();
                } catch (ConnectionErrorException | NotInDeclaredLimitsException reconnectionException) {
                    if (requestToServer.getCommandName().equals("exit"))
                        System.out.println("The command will not be registered on the server.");
                    else System.out.println("Try the command again later.");
                }
            }
        } while (!requestToServer.getCommandName().equals("exit"));
        return false;
    }

    private void authenticateUserProcess(){
        Request requestToServer = null;
        Response serverResponse = null;
        do {
            try{
                requestToServer = userHandler.authenticateUser();
                if (requestToServer.isEmpty()) continue;
                serverWriter.writeObject(requestToServer);
                serverResponse = (Response) serverReader.readObject();
                System.out.println(serverResponse.getResponseBody());
            } catch (IOException e) {
                System.out.println("The connection to the server has been lost!");
                try {
                    connectToServer();
                } catch (ConnectionErrorException | NotInDeclaredLimitsException reconnectionException) {
                    System.out.println("Try logging in again later.");
                }
            } catch (ClassNotFoundException e) {
                System.out.println("An error occurred while reading received data!");
            }
        } while (serverResponse == null || !serverResponse.getResponseCode().equals(ResponseCode.OK));
        user = requestToServer.getUser();
    }
}
