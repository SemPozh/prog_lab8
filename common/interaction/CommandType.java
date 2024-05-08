package laba6.common.interaction;

import laba6.client.modules.ProcessingCode;
import laba6.server.commands.Command;

public class CommandType {
    ProcessingCode processingCode;
    int argumentsCount;
    String commandView;
    Command command;
    public CommandType(ProcessingCode processingCode, int argumentsCount, String commandView, Command command){
        this.processingCode = processingCode;
        this.argumentsCount = argumentsCount;
        this.commandView = commandView;
        this.command = command;
    }

    public int getArgumentsCount(){
        return this.argumentsCount;
    }

    public String getCommandView(){
        return this.commandView;
    }
    public ProcessingCode getProcessingCode(){
        return this.processingCode;
    }
    public Command getCommand(){
        return this.command;
    }
}
