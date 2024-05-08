package laba6.common.interaction;

import laba6.client.modules.ProcessingCode;
import laba6.server.commands.*;

import java.util.HashMap;

public class Commands {
    public HashMap<String, CommandType> commands = new HashMap<>();

    public Commands(){
        this.commands.put("help", new CommandType(ProcessingCode.OK, 0, "help", new HelpCommand()));
        this.commands.put("info", new CommandType(ProcessingCode.OK, 0, "info", new InfoCommand()));
        this.commands.put("show", new CommandType(ProcessingCode.OK, 0, "show", new ShowCommand()));
        this.commands.put("clear", new CommandType(ProcessingCode.OK, 0, "clear", new ClearCommand()));
        this.commands.put("exit", new CommandType(ProcessingCode.OK, 0, "exit", new ExitCommand()));
        this.commands.put("reorder", new CommandType(ProcessingCode.OK, 0, "reorder", new ReorderCommand()));
        this.commands.put("average_of_annual_turnover", new CommandType(ProcessingCode.OK, 0, "average_of_annual_turnover", new AVGOfAnnualTurnoverCommand()));
        this.commands.put("min_by_employees_count", new CommandType(ProcessingCode.OK, 0, "min_by_employees_count", new MinByEmployeesCountCommand()));
        this.commands.put("print_field_descending_annual_turnover", new CommandType(ProcessingCode.OK, 0, "print_field_descending_annual_turnover", new PrintAnnualTurnoversCommand()));
        this.commands.put("add", new CommandType(ProcessingCode.OBJECT, 0, "add {element}", new AddCommand()));
        this.commands.put("add_if_max", new CommandType(ProcessingCode.OBJECT, 0, "add_if_max {element}", new AddIfMaxCommand()));
        this.commands.put("update", new CommandType(ProcessingCode.OBJECT, 1, "update <ID> {element}", new UpdateCommand()));
        this.commands.put("remove_by_id", new CommandType(ProcessingCode.OK, 1, "remove_by_id <ID>", new RemoveByIdCommand()));
        this.commands.put("execute_script", new CommandType(ProcessingCode.SCRIPT, 1, "execute_script <file name>", new ExecuteScriptCommand()));
        this.commands.put("insert_at", new CommandType(ProcessingCode.OBJECT, 1, "insert_at <ID> {element}", new InsertAtCommand()));
    }

    public HashMap<String, CommandType> getCommands(){
        return commands;
    }
}
