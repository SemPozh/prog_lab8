package laba8.laba8.client.modules;

import laba8.laba8.common.data.Organization;
import laba8.laba8.common.data.User;
import laba8.laba8.common.exeptions.CommandUsageException;
import laba8.laba8.common.exeptions.IncorrectInputInScriptException;
import laba8.laba8.common.exeptions.InvalidObjectFieldException;
import laba8.laba8.common.exeptions.ScriptRecursionException;
import laba8.laba8.common.interaction.Request;
import laba8.laba8.common.interaction.ResponseCode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Stack;

public class ScriptHandler {
    private final int maxRewriteAttempts = 1;

    private InputHandler inputHandler;
    private Stack<File> scriptStack = new Stack<>();
    private Stack<InputHandler> scannerStack = new Stack<>();
    private static HashMap<String, ClientCommandType> commands;

    /**
     * Receives user input.
     *
     * @param serverResponseCode Previous response code.
     * @param user               User object.
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
                    if (serverResponseCode == ResponseCode.ERROR || serverResponseCode == ResponseCode.SERVER_EXIT)
                        throw new IncorrectInputInScriptException();
                    while (!scannerStack.isEmpty() && !inputHandler.scanner.hasNextLine()) {
                        inputHandler.close();
                        inputHandler = scannerStack.pop();
                        if (!scannerStack.isEmpty()) scriptStack.pop();
                        else return null;
                    }
                    userInput = inputHandler.scanner.nextLine();
                    if (!userInput.isEmpty()) {
//                        Outputer.print(App.PS1);
//                        Outputer.println(userInput);
                    }
                    userCommand = (userInput.trim() + " ").split(" ", 2);
                    userCommand[1] = userCommand[1].trim();
                } catch (NoSuchElementException | IllegalStateException exception) {
//                    Outputer.println();
//                    Outputer.printerror("CommandErrorException");
                    userCommand = new String[]{"", ""};
                    rewriteAttempts++;
                    if (rewriteAttempts >= maxRewriteAttempts) {
//                        Outputer.printerror("RewriteAttemptsException");
                        System.exit(0);
                    }
                }
                processingCode = processCommand(userCommand[0], userCommand[1]);
            } while (userCommand[0].isEmpty());
            try {
                if (serverResponseCode == ResponseCode.ERROR || processingCode == ProcessingCode.ERROR)
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
//                Outputer.printerror("ScriptFileNotFoundException");
                throw new IncorrectInputInScriptException();
            } catch (ScriptRecursionException exception) {
//                Outputer.printerror("ScriptRecursionException");
                throw new IncorrectInputInScriptException();
            } catch (InvalidObjectFieldException e) {
                throw new RuntimeException(e);
            }
        } catch (IncorrectInputInScriptException exception) {
//            OutputerUI.error("IncorrectInputInScriptException");
            while (!scannerStack.isEmpty()) {
                inputHandler.close();
                inputHandler = scannerStack.pop();
            }
            scriptStack.clear();
            return null;
        }
        return new Request(userCommand[0], userCommand[1], null, user);
    }

    public ScriptHandler(File scriptFile) {
        try {
            inputHandler = new InputHandler(new Scanner(scriptFile));
            scannerStack.add(inputHandler);
            scriptStack.add(scriptFile);
            ClientCommands commandsList = new ClientCommands();
            commands = commandsList.getCommands();
        } catch (Exception exception) { /* ? */ }
    }

    /**
     * Generates marine to add.
     *
     * @return Marine to add.
     * @throws IncorrectInputInScriptException When something went wrong in script.
     */
    private Organization generateOrganizationAdd(User user) throws IncorrectInputInScriptException, InvalidObjectFieldException {
        OrganizationAsker organizationAsker = new OrganizationAsker(inputHandler);
        Organization organization = new Organization(
                organizationAsker.askName(),
                organizationAsker.askCoordinates(),
                organizationAsker.askEmployeesCount(),
                organizationAsker.askOrganizationType(),
                user);
        organization.setAnnualTurnover(organizationAsker.askAnnualTurnover());
        organization.setOfficialAddress(organizationAsker.askAddress());
        return organization;
    }

    /**
     * Processes the entered command.
     *
     * @return Status of code.
     */
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