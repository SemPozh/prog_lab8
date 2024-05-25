package laba6.client.modules;

import laba6.client.App;
import laba6.common.data.Organization;
import laba6.common.data.User;
import laba6.common.exeptions.CommandUsageException;
import laba6.common.exeptions.IncorrectInputInScriptException;
import laba6.common.exeptions.InvalidObjectFieldException;
import laba6.common.exeptions.ScriptRecursionException;
import laba6.common.interaction.Request;
import laba6.common.interaction.ResponseCode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Stack;

public class UserHandler {
    private InputHandler inputHandler;
    private final Stack<File> scriptStack = new Stack<>();
    private final Stack<InputHandler> scannerStack = new Stack<>();
    private static HashMap<String, ClientCommandType> commands;

    public UserHandler(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
        ClientCommands commandsList = new ClientCommands();
        commands = commandsList.getCommands();
    }

    public Request authenticateUser() {
        String userInput;
        System.out.println("You are not authorized!");
        System.out.print("Do you have an account? (y/n): ");
        userInput = inputHandler.readLine();
        if (userInput.equals("y")) {
            System.out.print("Input your username: ");
            String username = inputHandler.readLine();
            System.out.print("Input your password: ");
            String password = inputHandler.readLine();
            return new Request("authorization", username + ":" + password, null, new User(username, password));
        } else if (userInput.equals("n")) {
            System.out.println("Let's create an account!");
            System.out.print("Input your new username: ");
            String username = inputHandler.readLine();
            System.out.print("Input your new password: ");
            String password = inputHandler.readLine();
            return new Request("registration", username + ":" + password, null, new User(username, password));

        } else {
            System.out.println("Incorrect input! Type 'y' on 'n'");
            return new Request();
        }
    }

    /**
     * Receives user input.
     *
     * @param serverResponseCode Last server's response code.
     * @return New request to server.
     */
    public Request handle(ResponseCode serverResponseCode, User user) {
        String userInput;
        String[] userCommand;
        ProcessingCode processingCode;
        int rewriteAttempts = 0;
        try {
            do {
                try {
                    if (fileMode() && (serverResponseCode == ResponseCode.ERROR ||
                            serverResponseCode == ResponseCode.SERVER_EXIT))
                        throw new IncorrectInputInScriptException();
                    while (fileMode() && !inputHandler.scanner.hasNextLine()) {
                        inputHandler.close();
                        inputHandler = scannerStack.pop();
                        System.out.println("Back to script '" + scriptStack.pop().getName() + "'...");
                    }
                    String username = "";
                    String password = "";
                    if (fileMode()) {
                        userInput = inputHandler.readLine();
                        if (!userInput.isEmpty()) {
                            System.out.print(App.SYMBOL1);
                            System.out.println(userInput);
                        }
                    } else {
                        System.out.print(App.SYMBOL1);
                        userInput = inputHandler.readLine();
                        System.out.print("Login: ");
                        username = inputHandler.readLine();
                        System.out.print("Password: ");
                        password = inputHandler.readLine();
                        if (!user.checkUser(username, password)) {
                            System.out.println("Incorrect Login/password. Access denied!");
                            return new Request();
                        }
                    }
                    userCommand = (userInput.trim() + " ").split(" ", 2);
                    userCommand[1] = userCommand[1].trim();
                } catch (NoSuchElementException | IllegalStateException exception) {
                    System.out.println();
                    System.out.println("Error while typing command");
                    userCommand = new String[]{"", ""};
                    rewriteAttempts++;
                    int maxRewriteAttempts = 2;
                    if (rewriteAttempts >= maxRewriteAttempts) {
                        System.out.println("Maximum attempts is accepted!");
                        System.exit(0);
                    }
                }

                processingCode = processCommand(userCommand[0], userCommand[1]);
            } while (processingCode == ProcessingCode.ERROR && !fileMode() || userCommand[0].isEmpty());
            try {
                if (fileMode() && (serverResponseCode == ResponseCode.ERROR || processingCode == ProcessingCode.ERROR))
                    throw new IncorrectInputInScriptException();
                switch (processingCode) {
                    case OBJECT:
                        OrganizationAsker organizationAsker = new OrganizationAsker(inputHandler);
                        Organization organizationObject = organizationAsker.generateOrganizationObject(user);
                        return new Request(userCommand[0], userCommand[1], organizationObject, user);
                    case SCRIPT:
                        File scriptFile = new File(userCommand[1]);
                        if (!scriptFile.exists()) throw new FileNotFoundException();
                        if (!scriptStack.isEmpty() && scriptStack.search(scriptFile) != -1)
                            throw new ScriptRecursionException();
                        scannerStack.push(inputHandler);
                        scriptStack.push(scriptFile);
                        inputHandler = new InputHandler(new Scanner(scriptFile));
                        System.out.println("Execute script '" + scriptFile.getName() + "'...");
                        break;
                }
            } catch (FileNotFoundException exception) {
                System.out.println("File with script not found!");
            } catch (ScriptRecursionException exception) {
                System.out.println("Scripts can't be executed with recursion!");
                throw new IncorrectInputInScriptException();
            } catch (InvalidObjectFieldException e) {
                System.out.println("Invalid data!");
            }
        } catch (IncorrectInputInScriptException exception) {
            System.out.println("Executing stopped!");
            while (!scannerStack.isEmpty()) {
                inputHandler.close();
                inputHandler = scannerStack.pop();
            }
            scriptStack.clear();
            return new Request();
        }
        return new Request(userCommand[0], userCommand[1], null, user);
    }


    public boolean fileMode() {
        return !scriptStack.isEmpty();
    }

    private ProcessingCode processCommand(String command, String commandArgument) {
        try {
            ClientCommandType commandType = commands.get(command);

            if (commandType == null) {
                System.out.println("No such command. Use help to show all commands");
                return ProcessingCode.ERROR;
            }

            int argCount = 0;
            if (!commandArgument.isEmpty()) {
                argCount = 1;
            }
            if (commandType.getArgumentsCount() != argCount) {
                throw new CommandUsageException(commandType.getCommandView());
            }
            return commandType.getProcessingCode();
        } catch (CommandUsageException exception) {
            if (exception.getMessage() != null) {
                System.out.println("Usage: '" + exception.getMessage() + "'");
            }
            return ProcessingCode.ERROR;
        }
    }
}