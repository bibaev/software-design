package com.spbau.bibaev.software.desing.shell.command.impl;

import com.spbau.bibaev.software.desing.shell.EnvironmentImpl;
import com.spbau.bibaev.software.desing.shell.command.ExecutionResult;
import com.spbau.bibaev.software.desing.shell.command.CommandBase;
import com.spbau.bibaev.software.desing.shell.util.TextUtil;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.List;

/**
 * Update execution environment - add or update global variable
 *
 * usage: name=value
 * name should be a valid identifier
 * value is any string
 * @author Vitaliy.Bibaev
 */
public class AssignCommand extends CommandBase {
  private static final char ASSIGN_CHARACTER = '=';

  public AssignCommand(@NotNull String name, @NotNull List<String> args) {
    super(name, args);
  }

  /** Checks that expression can be interpreted as assignment expression.
   * Rule for matching something as expression:
   * id=string
   *
   * @param expression
   *        The candidate to be an expression
   * @return true is {@code expression} can be interpreted as assignment, false otherwise
   */
  public static boolean isAssignExpression(@NotNull String expression) {
    int eqIndex = expression.indexOf('=');
    return eqIndex != -1 && TextUtil.isIdentifier(expression.substring(0, eqIndex));
  }

  @NotNull
  @Override
  public ExecutionResult perform(@NotNull InputStream in, @NotNull OutputStream out, @NotNull OutputStream err)
      throws IOException {
    String expression = getName();
    int eqIndex = expression.indexOf(ASSIGN_CHARACTER);
    EnvironmentImpl.getInstance().putVariableValue(expression.substring(0, eqIndex), expression.substring(eqIndex + 1));
    return ExecutionResult.CONTINUE;
  }
}
