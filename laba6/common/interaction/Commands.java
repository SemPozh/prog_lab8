package laba6.common.interaction;

import laba6.client.modules.ProcessingCode;
import laba6.server.commands.HelpCommand;
import laba6.server.commands.InfoCommand;
import laba6.server.commands.ShowCommand;

import java.util.HashMap;

public class Commands {
    public HashMap<String, CommandType> commands = new HashMap<>();

    public Commands(){
        this.commands.put("help", new CommandType(ProcessingCode.OK, 0, "help", "HelpCommand"));
        this.commands.put("info", new CommandType(ProcessingCode.OK, 0, "info", "InfoCommand"));
        this.commands.put("show", new CommandType(ProcessingCode.OK, 0, "show", "ShowCommand"));
        this.commands.put("clear", new CommandType(ProcessingCode.OK, 0, "clear", "ClearCommand"));
        this.commands.put("exit", new CommandType(ProcessingCode.OK, 0, "exit", "ExitCommand"));
        this.commands.put("reorder", new CommandType(ProcessingCode.OK, 0, "reorder", "ReorderCommand"));
        this.commands.put("average_of_annual_turnover", new CommandType(ProcessingCode.OK, 0, "average_of_annual_turnover", "AVGOfAnnualTurnoverCommand"));
        this.commands.put("min_by_employees_count", new CommandType(ProcessingCode.OK, 0, "min_by_employees_count", "MinByEmployeesCountCommand"));
        this.commands.put("add", new CommandType(ProcessingCode.OBJECT, 0, "add {element}", "AddCommand"));
        this.commands.put("add_if_max", new CommandType(ProcessingCode.OBJECT, 0, "add_if_max {element}", "AddIfMaxCommand"));
        this.commands.put("update", new CommandType(ProcessingCode.OBJECT, 0, "update <ID> {element}", "UpdateCommand"));
        this.commands.put("remove_by_id", new CommandType(ProcessingCode.OK, 0, "remove_by_id <ID>", "RemoveByIdCommand"));
        this.commands.put("execute_script", new CommandType(ProcessingCode.SCRIPT, 0, "execute_script <file name>", "ExecuteScriptCommand"));
        this.commands.put("insert_at", new CommandType(ProcessingCode.OBJECT, 0, "insert_at <ID> {element}", "InsertAtCommand"));
    }

    public HashMap<String, CommandType> getCommands(){
        return commands;
    }
}
