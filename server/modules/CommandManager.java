package laba8.laba8.server.modules;

import laba8.laba8.client.modules.ProcessingCode;
import laba8.laba8.server.commands.*;

import java.util.HashMap;

public class CommandManager {
    public HashMap<String, ServerCommandType> commands = new HashMap<>();

    public CommandManager() {
        this.commands.put("help", new ServerCommandType(ProcessingCode.OK, 0, "help", new HelpCommand()));
        this.commands.put("info", new ServerCommandType(ProcessingCode.OK, 0, "info", new InfoCommand()));
        this.commands.put("show", new ServerCommandType(ProcessingCode.OK, 0, "show", new ShowCommand()));
        this.commands.put("clear", new ServerCommandType(ProcessingCode.OK, 0, "clear", new ClearCommand()));
        this.commands.put("exit", new ServerCommandType(ProcessingCode.OK, 0, "exit", new ExitCommand()));
        this.commands.put("reorder", new ServerCommandType(ProcessingCode.OK, 0, "reorder", new ReorderCommand()));
        this.commands.put("average_of_annual_turnover", new ServerCommandType(ProcessingCode.OK, 0, "average_of_annual_turnover", new AVGOfAnnualTurnoverCommand()));
        this.commands.put("min_by_employees_count", new ServerCommandType(ProcessingCode.OK, 0, "min_by_employees_count", new MinByEmployeesCountCommand()));
        this.commands.put("print_field_descending_annual_turnover", new ServerCommandType(ProcessingCode.OK, 0, "print_field_descending_annual_turnover", new PrintAnnualTurnoversCommand()));
        this.commands.put("add", new ServerCommandType(ProcessingCode.OBJECT, 0, "add {element}", new AddCommand()));
        this.commands.put("add_if_max", new ServerCommandType(ProcessingCode.OBJECT, 0, "add_if_max {element}", new AddIfMaxCommand()));
        this.commands.put("update", new ServerCommandType(ProcessingCode.OBJECT, 1, "update <ID> {element}", new UpdateCommand()));
        this.commands.put("remove_by_id", new ServerCommandType(ProcessingCode.OK, 1, "remove_by_id <ID>", new RemoveByIdCommand()));
        this.commands.put("execute_script", new ServerCommandType(ProcessingCode.SCRIPT, 1, "execute_script <file name>", new ExecuteScriptCommand()));
        this.commands.put("insert_at", new ServerCommandType(ProcessingCode.OBJECT, 1, "insert_at <ID> {element}", new InsertAtCommand()));
        this.commands.put("login", new ServerCommandType(ProcessingCode.OK, 0, "authorization", new Login()));
        this.commands.put("register", new ServerCommandType(ProcessingCode.OK, 0, "registration", new Registration()));
        this.commands.put("refresh", new ServerCommandType(ProcessingCode.OK, 0, "refresh", new RefreshCommand()));
    }

    public HashMap<String, ServerCommandType> getCommands() {
        return commands;
    }
}
