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

package uk.co.drcooke.commandapi.bukkit;

import uk.co.drcooke.commandapi.argument.lexing.CommandArgumentTokeniser;
import uk.co.drcooke.commandapi.argument.parsing.ArgumentParserLookupService;
import uk.co.drcooke.commandapi.argument.parsing.CommandArgumentConverterService;
import uk.co.drcooke.commandapi.argument.parsing.SimpleArgumentParserLookupService;
import uk.co.drcooke.commandapi.argument.parsing.SimpleCommandArgumentConverterService;
import uk.co.drcooke.commandapi.bukkit.security.CommandSenderTypeCheckingCommandExcecutorDecorator;
import uk.co.drcooke.commandapi.bukkit.security.PermissionCheckingCommandExecutorDecorator;
import uk.co.drcooke.commandapi.command.CommandShell;
import uk.co.drcooke.commandapi.command.SimpleCommandShell;
import uk.co.drcooke.commandapi.command.lookup.CommandLookup;
import uk.co.drcooke.commandapi.command.lookup.SimpleCommandLookupService;
import uk.co.drcooke.commandapi.command.registry.CommandNamespaceRegistry;
import uk.co.drcooke.commandapi.command.registry.SimpleCommandNamespaceRegistry;
import uk.co.drcooke.commandapi.command.scanning.ReflectionCommandScanner;
import uk.co.drcooke.commandapi.execution.ExitCode;
import uk.co.drcooke.commandapi.execution.executable.CommandExecutable;
import uk.co.drcooke.commandapi.execution.executor.CommandExecutor;
import uk.co.drcooke.commandapi.execution.executor.SimpleCommandExecutor;
import uk.co.drcooke.commandapi.security.User;

import java.util.Deque;

public class BukkitCommandShell implements CommandShell {

    private final CommandNamespaceRegistry commandNamespaceRegistry;
    private final CommandArgumentTokeniser commandArgumentTokeniser;
    private final CommandArgumentConverterService commandArgumentConverterService;
    private final CommandLookup commandLookup;
    private final CommandExecutor commandExecutor;

    public BukkitCommandShell(CommandNamespaceRegistry commandNamespaceRegistry,
                              CommandArgumentTokeniser commandArgumentTokeniser,
                              CommandArgumentConverterService commandArgumentConverterService,
                              CommandLookup commandLookup, CommandExecutor commandExecutor) {
        this.commandNamespaceRegistry = commandNamespaceRegistry;
        this.commandArgumentTokeniser = commandArgumentTokeniser;
        this.commandArgumentConverterService = commandArgumentConverterService;
        this.commandLookup = commandLookup;
        this.commandExecutor = commandExecutor;
    }

    @Override
    public ExitCode execute(String input, User user) {
        Deque<String> tokens = commandArgumentTokeniser.parseCommand(input);
        CommandExecutable commandExecutable = commandLookup.getCommand(tokens);
        return commandExecutor.execute(commandExecutable, tokens, user);
    }

    public CommandNamespaceRegistry getCommandNamespaceRegistry() {
        return commandNamespaceRegistry;
    }

    public CommandArgumentTokeniser getCommandArgumentTokeniser() {
        return commandArgumentTokeniser;
    }

    public CommandArgumentConverterService getCommandArgumentConverterService() {
        return commandArgumentConverterService;
    }

    public CommandLookup getCommandLookup() {
        return commandLookup;
    }

    public CommandExecutor getCommandExecutor() {
        return commandExecutor;
    }

    @Override
    public void register(Object o) {
        commandNamespaceRegistry.register(o);
    }

    @Override
    public void unregister(Object o) {
        commandNamespaceRegistry.unregister(o);
    }

    public static CommandShell create(){
        CommandNamespaceRegistry commandNamespaceRegistry = new SimpleCommandNamespaceRegistry(new ReflectionCommandScanner());
        CommandArgumentConverterService commandArgumentConverterService = new SimpleCommandArgumentConverterService(
                new SimpleArgumentParserLookupService(ArgumentParserLookupService.getBuiltinArgumentParsers()));
        return new SimpleCommandShell(commandNamespaceRegistry, CommandArgumentTokeniser.simple(),
                commandArgumentConverterService, new SimpleCommandLookupService(commandNamespaceRegistry),
                new CommandSenderTypeCheckingCommandExcecutorDecorator(
                        new PermissionCheckingCommandExecutorDecorator(
                                new SimpleCommandExecutor(commandArgumentConverterService))));
    }

}
