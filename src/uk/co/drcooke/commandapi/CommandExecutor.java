package uk.co.drcooke.commandapi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CommandExecutor {

    private String command;
    private String permission;
    private String[] aliases;
    private String[] completionPhrases;
    private Method method;
    private Object parent;

    public CommandExecutor(String command, String permission, String[] aliases, String[] completionPhrases, Method method, Object parent) {
        this.command = command;
        this.permission = permission;
        this.aliases = aliases;
        this.completionPhrases = completionPhrases;
        this.method = method;
        this.parent = parent;
    }

    public String getCommand() {
        return command;
    }

    public String getPermission() {
        return permission;
    }

    public String[] getAliases() {
        return aliases;
    }

    public Method getMethod() {
        return method;
    }

    public Object getParent() {
        return parent;
    }

    public String[] getCompletionPhrases() {
        return completionPhrases;
    }

    public void run(Object... args){
        try {
            method.invoke(parent, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
