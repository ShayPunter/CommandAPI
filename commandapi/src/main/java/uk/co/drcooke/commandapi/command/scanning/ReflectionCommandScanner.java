/*
 * Copyright 2018 David Cooke
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package uk.co.drcooke.commandapi.command.scanning;

import uk.co.drcooke.commandapi.annotations.command.*;
import uk.co.drcooke.commandapi.argument.parsing.CommandParameter;
import uk.co.drcooke.commandapi.argument.parsing.SimpleCommandParameter;
import uk.co.drcooke.commandapi.command.namespace.CommandNamespace;
import uk.co.drcooke.commandapi.command.namespace.SimpleCommandNamespace;
import uk.co.drcooke.commandapi.execution.ExitCode;
import uk.co.drcooke.commandapi.execution.executable.CommandExecutable;
import uk.co.drcooke.commandapi.execution.executable.SimpleCommandExecutable;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class ReflectionCommandScanner implements CommandScanner{

    @Override
    public CommandNamespace getCommands(Object object) {
        Class clazz = object.getClass();
        if(!clazz.isAnnotationPresent(Command.class)){
            return null;
        }
        CommandNamespace commandNamespace = getEmptyCommandNamespace(clazz);
        for(Method method : clazz.getDeclaredMethods()){
            processMethod(method, commandNamespace, object);
        }
        return commandNamespace;
    }

    private CommandNamespace getEmptyCommandNamespace(Class clazz){
        Command commandAnnotation = (Command)clazz.getAnnotation(Command.class);
        String name = commandAnnotation.value();
        SimpleCommandNamespace commandNamespace = new SimpleCommandNamespace(name);
        Usage usage = (Usage)clazz.getAnnotation(Usage.class);
        if(usage != null){
            commandNamespace.setUsage(usage.usage());
        }
        Description description = (Description)clazz.getAnnotation(Description.class);
        if(description != null){
            commandNamespace.setDescription(description.description());
        }
        return commandNamespace;
    }

    private void processMethod(Method method, CommandNamespace commandNamespace, Object parent){
        if(method.isAnnotationPresent(DefaultHandler.class)){
            List<CommandParameter> commandParameters = new ArrayList<>();
            for(Parameter parameter : method.getParameters()){
                Class<?> type = parameter.getType();
                Annotation[] annotations = parameter.getAnnotations();
                commandParameters.add(new SimpleCommandParameter(type, annotations));
            }
            CommandExecutable commandExecutable = new SimpleCommandExecutable("", commandParameters, argumentManifest -> {
                try {
                    System.out.println(argumentManifest.getArguments().get(0).getClass().getCanonicalName());
                    return (ExitCode)method.invoke(parent, argumentManifest.getArguments().toArray());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                return ExitCode.FAILURE;
            });
            commandNamespace.setDefaultCommand(commandExecutable);
        }else if(method.isAnnotationPresent(Subcommand.class)){
            Subcommand subcommand = method.getAnnotation(Subcommand.class);
            List<CommandParameter> commandParameters = new ArrayList<>();
            for(Parameter parameter : method.getParameters()){
                Class<?> type = parameter.getType();
                Annotation[] annotations = parameter.getAnnotations();
                commandParameters.add(new SimpleCommandParameter(type, annotations));
            }
            CommandExecutable commandExecutable = new SimpleCommandExecutable(subcommand.value(), commandParameters, argumentManifest -> {
                try {
                    return (ExitCode)method.invoke(parent, argumentManifest.getArguments().toArray());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                return ExitCode.FAILURE;
            });
            commandNamespace.addCommand(commandExecutable);
        }
    }

}
