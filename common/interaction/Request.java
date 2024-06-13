package laba8.laba8.common.interaction;

import laba8.laba8.common.data.User;

import java.io.Serializable;

public class Request implements Serializable {
    private final String commandName;
    private final String commandStringArgument;
    private final Serializable commandObjectArgument;

    private final User user;

    public Request(String commandName, String commandStringArgument, Serializable commandObjectArgument, User user) {
        this.commandName = commandName;
        this.commandStringArgument = commandStringArgument;
        this.commandObjectArgument = commandObjectArgument;
        this.user = user;
    }

    public Request(){
        this("", "", null, null);
    }

    /**
     * @return Command name.
     */
    public String getCommandName() {
        return commandName;
    }

    /**
     * @return Command string argument.
     */
    public String getCommandStringArgument() {
        return commandStringArgument;
    }

    /**
     * @return Command object argument.
     */
    public Object getCommandObjectArgument() {
        return commandObjectArgument;
    }

    public User getUser(){
        return this.user;
    }

    /**
     * @return Is this request empty.
     */
    public boolean isEmpty() {
        return commandName.isEmpty() && commandStringArgument.isEmpty() && commandObjectArgument == null;
    }
}
