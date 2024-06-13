package laba8.laba8.client;

import laba8.laba8.client.controllers.MainPageController;
import laba8.laba8.client.modules.Printer;
import laba8.laba8.client.modules.PrinterUI;
import laba8.laba8.client.modules.ScriptHandler;
import laba8.laba8.client.modules.UserHandler;
import laba8.laba8.common.data.Organization;
import laba8.laba8.common.data.User;
import laba8.laba8.common.exeptions.ConnectionErrorException;
import laba8.laba8.common.exeptions.NotInDeclaredLimitsException;
import laba8.laba8.common.interaction.Request;
import laba8.laba8.common.interaction.Response;
import laba8.laba8.common.interaction.ResponseCode;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Client {
    private final String host;
    private final int port;
    private SocketChannel socketChannel;
    private int reconnectionAttempts;
    private ObjectOutputStream serverWriter;
    private ObjectInputStream serverReader;
    private User user;
    private boolean isConnected;


    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }


    public void run() {
        try {
            connectToServer();
        } catch (NotInDeclaredLimitsException e) {
            Printer.printerror("ClientException");
            System.exit(0);
        } catch (ConnectionErrorException e) {
            System.exit(0);
        }
    }

    /**
     * Connecting to server.
     */
    private void connectToServer() throws ConnectionErrorException, NotInDeclaredLimitsException {
        try {
            Printer.print("ConnectionToServer");
            InetSocketAddress inetSocketAddress = new InetSocketAddress(host, port);
            socketChannel = SocketChannel.open(inetSocketAddress);
            serverWriter = new ObjectOutputStream(socketChannel.socket().getOutputStream());
            serverReader = new ObjectInputStream(socketChannel.socket().getInputStream());
            isConnected = true;
            Printer.println("ConnectionToServerComplete");
        } catch (IllegalArgumentException exception) {
            Printer.printerror("ServerAddressException");
            isConnected = false;
            throw new NotInDeclaredLimitsException();
        } catch (IOException exception) {
            Printer.printerror("ConnectionToServerException");
            isConnected = false;
            throw new ConnectionErrorException();
        }
    }

    /**
     * Server request process.
     */
    public ConcurrentLinkedDeque<Organization> processRequestToServer(String commandName, String commandArguments, Serializable commandObjectArgument) {
        Request requestToServer = null;
        Response serverResponse = null;
        try {
            requestToServer = new Request(commandName, commandArguments, commandObjectArgument, user);
            serverWriter.writeObject(requestToServer);
            serverResponse = (Response) serverReader.readObject();
            if (!serverResponse.getResponseBody().isEmpty()){
                PrinterUI.tryError(serverResponse.getResponseBody(), serverResponse.getResponseBodyArgs());
            }

        } catch (InvalidClassException | NotSerializableException exception) {
            PrinterUI.error("DataSendingException");
        } catch (ClassNotFoundException exception) {
            PrinterUI.error("DataReadingException");
        } catch (IOException exception) {
            if (requestToServer.getCommandName().equals(MainPageController.EXIT_COMMAND_NAME)) return null;
            PrinterUI.error("EndConnectionToServerException");
            try {
                connectToServer();
                PrinterUI.info("ConnectionToServerComplete");
            } catch (ConnectionErrorException | NotInDeclaredLimitsException reconnectionException) {
                PrinterUI.info("TryCommandLater");
            }
        }
        return serverResponse == null ? null : serverResponse.getCollection();
    }

    public boolean authenticateUserProcess(String username, String password, boolean register) {
        Request requestToServer = null;
        Response serverResponse = null;
        String command;
        try {
            command = register ? "register" : "login";
            requestToServer = new Request(command, "", null, new User(username, password));
            if (serverWriter == null) throw new IOException();
            serverWriter.writeObject(requestToServer);
            serverResponse = (Response) serverReader.readObject();
            PrinterUI.tryError(serverResponse.getResponseBody(), serverResponse.getResponseBodyArgs());
        } catch (IOException e) {
            PrinterUI.error("EndConnectionToServerException");
            try {
                connectToServer();
                PrinterUI.info("ConnectionToServerComplete");
            } catch (ConnectionErrorException | NotInDeclaredLimitsException reconnectionException) {
                PrinterUI.info("TryAuthLater");
            }
        } catch (ClassNotFoundException e) {
            PrinterUI.error("DataReadingException");
        }
        if (serverResponse != null && serverResponse.getResponseCode().equals(ResponseCode.OK)) {
            user = requestToServer.getUser();
            return true;
        }
        return false;
    }

    public void stop() {
        try {
            processRequestToServer("exit", "", null);
            socketChannel.close();
        } catch (IOException | NullPointerException exception) {
            Printer.printerror("EndRunningWorkOfClientException");
        }
    }

    public boolean isConnected() {
        return this.isConnected;
    }

    /**
     * Server script process.
     *
     * @param scriptFile Scipt file.
     * @return Is everything OK.
     */
    public boolean processScriptToServer(File scriptFile) {
        Request requestToServer = null;
        Response serverResponse = null;
        ScriptHandler scriptHandler = new ScriptHandler(scriptFile);
        do {
            try {
                requestToServer = serverResponse != null ? scriptHandler.handle(serverResponse.getResponseCode(), user) :
                        scriptHandler.handle(null, user);
                if (requestToServer == null) return false;
                if (requestToServer.isEmpty()) continue;
                serverWriter.writeObject(requestToServer);
                serverResponse = (Response) serverReader.readObject();
                if (!serverResponse.getResponseBody().isEmpty())
                    PrinterUI.tryError(serverResponse.getResponseBody(), serverResponse.getResponseBodyArgs());
            } catch (InvalidClassException | NotSerializableException exception) {
                PrinterUI.error("DataSendingException");
            } catch (ClassNotFoundException exception) {
                PrinterUI.error("DataReadingException");
            } catch (IOException exception) {
                PrinterUI.error("EndConnectionToServerException");
                try {
                    connectToServer();
                    PrinterUI.info("ConnectionToServerComplete");
                } catch (ConnectionErrorException | NotInDeclaredLimitsException reconnectionException) {
                    PrinterUI.info("TryCommandLater");
                }
            }
        } while (!requestToServer.getCommandName().equals("exit"));
        return true;
    }

    public String getUsername() {
        return user == null ? null : user.getUsername();
    }

    public User getUser() {
        return user;
    }
}
