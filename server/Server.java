package laba8.laba8.server;

import laba8.laba8.common.exeptions.ClosingSocketException;
import laba8.laba8.common.exeptions.ConnectionErrorException;
import laba8.laba8.common.exeptions.OpeningServerSocketException;
import laba8.laba8.server.modules.CollectionManager;
import laba8.laba8.server.modules.CommandManager;
import laba8.laba8.server.modules.ConnectionHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class Server {
    private final int port;
    private ServerSocket serverSocket;
    private final CollectionManager collectionManager;
    private final CommandManager commandManager;
    private final ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
    private final Semaphore semaphore;
    private boolean isStopped;

    public Server(int port, int maxClients, CommandManager commandManager, CollectionManager collectionManager){
        this.port = port;
        this.semaphore = new Semaphore(maxClients);
        this.commandManager = commandManager;
        this.collectionManager = collectionManager;
    }

    public void run() {
        try {
            openServerSocket();
            ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
            ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
            while (!isStopped) {
                try {
                    acquireConnection();
                    if (isStopped()) throw new ConnectionErrorException();
                    Socket clientSocket = acceptClientSocket();
                    cachedThreadPool.submit(new ConnectionHandler(this, clientSocket, commandManager, collectionManager, forkJoinPool, fixedThreadPool));
                } catch (ConnectionErrorException e) {
                    if (!isStopped()) {
                        System.out.println("An error occurred while connecting with client!");
                        App.logger.warning("An error occurred while connecting to the client!");
                    } else break;
                }
            }
            cachedThreadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            System.out.println("The server has completed its operation.");
        } catch (OpeningServerSocketException e){
            System.out.println("The server cannot be started!");
            App.logger.warning("The server cannot be started!");
        } catch (InterruptedException e){
            System.out.println("An error occurred while terminating work with already connected clients!");
        }
    }

    /**
     * Finishes server operation.
     */
    public synchronized void stop() {
        try {
            App.logger.info("Shutting down the server...");
            if (serverSocket == null) throw new ClosingSocketException();
            isStopped = true;
            cachedThreadPool.shutdown();
            serverSocket.close();
            System.out.println("Finishing work with already connected clients...");
            App.logger.info("The server has completed its operation.");
        } catch (ClosingSocketException exception) {
            System.out.println("It is impossible to shut down a server that has not yet started!");
            App.logger.warning("It is impossible to shut down a server that has not yet started!");
        } catch (IOException exception) {
            System.out.println("An error occurred while shutting down the server!");
            System.out.println("Finishing work with already connected clients...");
            App.logger.warning("An error occurred while shutting down the server!");
        }
    }

    /**
     * Release connection.
     */
    public void releaseConnection() {
        semaphore.release();
        App.logger.info("A connection failure has been registered.");
    }

    /**
     * Checked stops of server.
     *
     * @return Status of server stop.
     */
    private synchronized boolean isStopped() {
        return isStopped;
    }

    private void openServerSocket() throws OpeningServerSocketException {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IllegalArgumentException exception) {
            System.out.println("Port '" + port + "' is beyond the limits of possible values!");
            throw new OpeningServerSocketException();
        } catch (IOException exception) {
            System.out.println("An error occurred while trying to use the port '" + port + "'!");
            App.logger.severe("An error occurred while trying to use the port '" + port + "'!");
            throw new OpeningServerSocketException();
        }
    }

    private Socket acceptClientSocket() throws ConnectionErrorException {
        try {
            System.out.println("Прослушивание порта '" + port + "'...");
            App.logger.info("Прослушивание порта '" + port + "'...");
            Socket clientSocket = serverSocket.accept();
            System.out.println("Соединение с клиентом установлено.");
            App.logger.info("Соединение с клиентом установлено.");
            return clientSocket;
        } catch (IOException exception) {
            throw new ConnectionErrorException();
        }
    }

    public void acquireConnection(){
        try {
            semaphore.acquire();
            App.logger.info("Permission for a new connection has been received.");
        } catch (InterruptedException e){
            System.out.println("An error occurred while obtaining permission for a new connection!");
            App.logger.info("An error occurred while obtaining permission for a new connection!");
        }
    }
}
