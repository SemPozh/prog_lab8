package laba8.laba8.client.modules;

public class ClientCommandType {
    ProcessingCode processingCode;
    int argumentsCount;
    String commandView;
    public ClientCommandType(ProcessingCode processingCode, int argumentsCount, String commandView){
        this.processingCode = processingCode;
        this.argumentsCount = argumentsCount;
        this.commandView = commandView;
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
}
