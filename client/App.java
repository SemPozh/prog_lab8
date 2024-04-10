package laba6.client;

import laba6.client.modules.InputHandler;
import laba6.client.modules.UserHandler;
import laba6.common.exeptions.NotInDeclaredLimitsException;
import laba6.common.exeptions.WrongAmountOfElementsException;

import java.util.Scanner;

public class App {
    public static final String SYMBOL1 = "$ ";
    public static final String SYMBOL2 = "> ";

    private static final int RECONNECTION_TIMEOUT = 5 * 1000;
    private static final int MAX_RECONNECTION_ATTEMPTS = 5;

    private static String host;
    private static int port;

    private static boolean initializeConnectionAddress(String[] hostAndPortArgs) {
        try {
            if (hostAndPortArgs.length != 2) throw new WrongAmountOfElementsException();
            host = hostAndPortArgs[0];
            port = Integer.parseInt(hostAndPortArgs[1]);
            if (port < 0) throw new NotInDeclaredLimitsException();
            return true;
        } catch (WrongAmountOfElementsException exception) {
            String jarName = new java.io.File(App.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .getPath())
                    .getName();
            System.out.println("Usage: 'java -jar " + jarName + " <host> <port>'");
        } catch (NumberFormatException exception) {
            System.out.println("The port must be represented by a number!");
        } catch (NotInDeclaredLimitsException exception) {
            System.out.println("Port cannot be negative!");
        }
        return false;
    }

    public static void main(String[] args) {
        if (!initializeConnectionAddress(args)) return;
        InputHandler inputHandler = new InputHandler(new Scanner(System.in));
        UserHandler userHandler = new UserHandler(inputHandler);
        Client client = new Client(host, port, RECONNECTION_TIMEOUT, MAX_RECONNECTION_ATTEMPTS, userHandler);
        client.run();
        inputHandler.close();
    }
}
