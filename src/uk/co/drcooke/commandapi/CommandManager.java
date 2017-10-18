package uk.co.drcooke.commandapi;

import uk.co.drcooke.commandapi.annotations.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommandManager {

    private static Map<String, ArrayList<CommandExecutor>> commandExecutorMap = new HashMap<>();

    public static void registerCommands(Object object){
        Class clazz = object.getClass();
        Command commandAnnotation = (Command)clazz.getAnnotation(Command.class);
        if(commandAnnotation != null){
            String parentCommand = commandAnnotation.command();
            String description = commandAnnotation.description();
            String[] aliases = commandAnnotation.aliases();
            String permission = commandAnnotation.permission();
            String usage = commandAnnotation.usage();
            for(Method method : clazz.getDeclaredMethods()){
                Main mainAnnotation = method.getAnnotation(Main.class);
                if(mainAnnotation != null){
                    CompletionPhrases completionPhrases = method.getAnnotation(CompletionPhrases.class);
                    CommandExecutor commandExecutor = new CommandExecutor("", permission, aliases,
                            completionPhrases.phrases(), method, object);
                    addCommandExecutorToMap(commandExecutor, parentCommand);
                    continue;
                }
                boolean noInherit = method.isAnnotationPresent(NoInherit.class);
            }
        }
    }

    private static void addCommandExecutorToMap(CommandExecutor commandExecutor, String command){
        commandExecutorMap.putIfAbsent(command, new ArrayList<>());
        commandExecutorMap.get(command).add(commandExecutor);
    }

}
