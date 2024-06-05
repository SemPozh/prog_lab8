package laba7.server.modules;

import laba7.client.modules.ProcessingCode;
import laba7.server.commands.Command;

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
