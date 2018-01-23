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

package uk.co.drcooke.commandapi.execution.executable;

import uk.co.drcooke.commandapi.argument.parsing.CommandParameter;
import uk.co.drcooke.commandapi.execution.ArgumentManifest;
import uk.co.drcooke.commandapi.execution.ExitCode;

import java.util.List;
import java.util.function.Function;

public class SimpleCommandExecutable implements CommandExecutable {

    private final String name;
    private final List<CommandParameter> commandParameters;
    private final Function<ArgumentManifest, ExitCode> commandExecutionFunction;
    private String usage;
    private String description;

    public SimpleCommandExecutable(String name, List<CommandParameter> commandParameters,
                                   Function<ArgumentManifest, ExitCode> commandExecutionFunction) {
        this.name = name;
        this.commandParameters = commandParameters;
        this.commandExecutionFunction = commandExecutionFunction;
    }

    @Override
    public ExitCode execute(ArgumentManifest argumentManifest) {
        return commandExecutionFunction.apply(argumentManifest);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUsage() {
        return usage;
    }

    @Override
    public void setUsage(String usage) {
        this.usage = usage;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public List<CommandParameter> getCommandParameters() {
        return commandParameters;
    }

}
