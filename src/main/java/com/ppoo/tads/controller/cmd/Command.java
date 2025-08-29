package com.ppoo.tads.controller.cmd;

public interface Command {
    void execute();
    void undo();
    String name();
}
