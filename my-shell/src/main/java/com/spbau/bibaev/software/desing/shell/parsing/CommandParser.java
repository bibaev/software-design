package com.spbau.bibaev.software.desing.shell.parsing;

import com.spbau.bibaev.software.desing.shell.ExecutionResult;
import com.spbau.bibaev.software.desing.shell.command.CommandArg;
import com.spbau.bibaev.software.desing.shell.command.CommandFactory;
import com.spbau.bibaev.software.desing.shell.command.Executable;
import com.spbau.bibaev.software.desing.shell.ex.CommandCreationException;
import com.spbau.bibaev.software.desing.shell.ex.EmptyCommandException;
import com.spbau.bibaev.software.desing.shell.parsing.quoting.Quote;
import com.spbau.bibaev.software.desing.shell.pipe.PipeHandler;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class CommandParser {
  public static Executable parse(@NotNull String input) throws EmptyCommandException, CommandCreationException {
    List<List<Quote>> commands = QuoteParser.parse(input); // split by unquoted '|' character
    List<Executable> executables = new ArrayList<>();
    for(List<Quote> command : commands) {
      if(command.size() == 0) {
        throw new EmptyCommandException("Command must contain one or more characters");
      }

      List<CommandArg> allQuotes = command.stream().map(CommandArg::new)
          .collect(Collectors.toList());
      CommandArg name = allQuotes.get(0);
      List<CommandArg> args = allQuotes.subList(1, allQuotes.size());
      executables.add(new MyExecutableWrapper(name, args));
    }

    Executable previous = executables.get(0);
    for(int i = 1; i < executables.size(); i++) {
      previous = new PipeHandler(previous, executables.get(i));
    }

    return previous;
  }

  private static class MyExecutableWrapper implements Executable {
    private final CommandArg myName;
    private final List<CommandArg> myArgs;

    MyExecutableWrapper(@NotNull CommandArg name, @NotNull List<CommandArg> args) {
      myName = name;
      myArgs = args;
    }

    @Override
    public @NotNull ExecutionResult perform(@NotNull InputStream in, @NotNull OutputStream out,
                                            @NotNull OutputStream err) throws IOException {
      String name = myName.getValue();
      List<String> args = myArgs.stream().map(CommandArg::getValue).collect(Collectors.toList());
      try {
        Executable command = CommandFactory.getInstance().createCommand(name, args);
        return command.perform(in, out, err);
      } catch (CommandCreationException e) {
        return ExecutionResult.SHUTDOWN;
      }
    }
  }
}
