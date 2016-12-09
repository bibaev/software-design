package com.spbau.bibaev.software.design.messenger.app;

import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;

/**
 * @author Vitaliy.Bibaev
 */
public interface User {
  @NotNull
  InetAddress getAddress();

  int getPort();
}