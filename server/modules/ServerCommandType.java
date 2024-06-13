package laba8.laba8.server.modules;

import laba8.laba8.client.modules.ProcessingCode;
import laba8.laba8.server.commands.Command;

public class ServerCommandType {
    ProcessingCode processingCode;
    int argumentsCount;
    String commandView;
    Command command;
    public ServerCommandType(ProcessingCode processingCode, int argumentsCount, String commandView, Command command){
        this.processingCode = processingCode;
        this.argumentsCount = argumentsCount;
        this.commandView = commandView;
        this.command = command;
    }


    public Command getCommand() {
        return command;
    }
}
