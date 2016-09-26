package com.spbau.bibaev.software.desing.shell.parsing;

import com.spbau.bibaev.software.desing.shell.command.CommandArg;
import com.spbau.bibaev.software.desing.shell.command.Executable;
import com.spbau.bibaev.software.desing.shell.ex.EmptyCommandException;
import com.spbau.bibaev.software.desing.shell.ex.ParseException;
import com.spbau.bibaev.software.desing.shell.parsing.quoting.Quote;
import com.spbau.bibaev.software.desing.shell.pipe.PipeHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Parser user input into one abstract executable task
 *
 * @author Vitaliy.Bibaev
 */
public class InputParser implements Parser<Executable> {

  private final QuoteParser myQuoteParser = new QuoteParser();
  /**
   * Parses user input into one executable
   * @param input The user input
   * @return Something that can be executed
   * @throws EmptyCommandException Thrown if command is empty, {@code "cat | | ls"} - for example second command is empty
   */
  public Executable parse(@NotNull String input) throws ParseException {
    List<List<Quote>> commands = myQuoteParser.parse(input); // split by unquoted '|' character
    List<Executable> executables = new ArrayList<>();
    for(List<Quote> command : commands) {
      if(command.size() == 0) {
        throw new EmptyCommandException("Command must contain one or more characters");
      }

      List<CommandArg> commandArgs = command.stream().map(CommandArg::new)
          .collect(Collectors.toList());
      CommandArg name = commandArgs.get(0);
      List<CommandArg> args = commandArgs.subList(1, commandArgs.size());
      executables.add(new ArgumentSubstitutionWrapper(name, args));
    }

    Executable previous = executables.get(0);
    for(int i = 1; i < executables.size(); i++) {
      previous = new PipeHandler(previous, executables.get(i));
    }

    return previous;
  }

}