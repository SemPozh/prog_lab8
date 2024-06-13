package laba8.laba8.client.modules;


import java.util.HashMap;

public class ClientCommands {
    public HashMap<String, ClientCommandType> commands = new HashMap<>();

    public ClientCommands() {
        this.commands.put("help", new ClientCommandType(ProcessingCode.OK, 0, "help"));
        this.commands.put("info", new ClientCommandType(ProcessingCode.OK, 0, "info"));
        this.commands.put("show", new ClientCommandType(ProcessingCode.OK, 0, "show"));
        this.commands.put("clear", new ClientCommandType(ProcessingCode.OK, 0, "clear"));
        this.commands.put("exit", new ClientCommandType(ProcessingCode.OK, 0, "exit"));
        this.commands.put("reorder", new ClientCommandType(ProcessingCode.OK, 0, "reorder"));
        this.commands.put("average_of_annual_turnover", new ClientCommandType(ProcessingCode.OK, 0, "average_of_annual_turnover"));
        this.commands.put("min_by_employees_count", new ClientCommandType(ProcessingCode.OK, 0, "min_by_employees_count"));
        this.commands.put("print_field_descending_annual_turnover", new ClientCommandType(ProcessingCode.OK, 0, "print_field_descending_annual_turnover"));
        this.commands.put("add", new ClientCommandType(ProcessingCode.OBJECT, 0, "add {element}"));
        this.commands.put("add_if_max", new ClientCommandType(ProcessingCode.OBJECT, 0, "add_if_max {element}"));
        this.commands.put("update", new ClientCommandType(ProcessingCode.OBJECT, 1, "update <ID> {element}"));
        this.commands.put("remove_by_id", new ClientCommandType(ProcessingCode.OK, 1, "remove_by_id <ID>"));
        this.commands.put("execute_script", new ClientCommandType(ProcessingCode.SCRIPT, 1, "execute_script <file name>"));
        this.commands.put("insert_at", new ClientCommandType(ProcessingCode.OBJECT, 1, "insert_at <ID> {element}"));
    }

    public HashMap<String, ClientCommandType> getCommands() {
        return commands;
    }
}
