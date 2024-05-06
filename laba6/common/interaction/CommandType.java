package laba6.common.interaction;

import laba6.client.modules.ProcessingCode;
import laba6.server.commands.Command;

public class CommandType {
    ProcessingCode processingCode;
    int argumentsCount;
    String commandView;
    String commandClassName;

    Command command;
    public CommandType(ProcessingCode processingCode, int argumentsCount, String commandView, String commandClassName){
        this.processingCode = processingCode;
        this.argumentsCount = argumentsCount;
        this.commandView = commandView;
        this.commandClassName = commandClassName;
    }

    public int getArgumentsCount(){
        return this.argumentsCount;
    }

    public String getCommandView(){
        return this.commandView;
    }

    public String getCommandClassName() {
        return commandClassName;
    }

    public ProcessingCode getProcessingCode(){
        return this.processingCode;
    }
}
