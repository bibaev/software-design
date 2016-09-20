package com.spbau.bibaev.software.desing.shell.command.impl.todo;

import com.spbau.bibaev.software.desing.shell.ExecutionResult;
import com.spbau.bibaev.software.desing.shell.command.CommandArg;
import com.spbau.bibaev.software.desing.shell.command.CommandBase;
import com.spbau.bibaev.software.desing.shell.util.TextUtil;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.List;

public class AssignCommand extends CommandBase {
  public AssignCommand(@NotNull List<String> args) {
    super(args);
  }

  public static boolean isAssignExpression(@NotNull String name) {
    int eqIndex = name.indexOf('=');
    return eqIndex != -1 && TextUtil.isIdentifier(name.substring(0, eqIndex));
  }

  @NotNull
  @Override
  public ExecutionResult perform(@NotNull InputStream in, @NotNull OutputStream out, @NotNull OutputStream err)
      throws IOException {
    return ExecutionResult.CONTINUE;
  }
}
