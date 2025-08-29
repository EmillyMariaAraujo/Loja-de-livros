package com.ppoo.tads.controller.cmd;

import java.util.ArrayDeque;
import java.util.Deque;

public class CommandManager {
    private final Deque<Command> undo = new ArrayDeque<>();
    private final Deque<Command> redo = new ArrayDeque<>();

    public void run(Command c){ c.execute(); undo.push(c); redo.clear(); }
    public boolean canUndo(){ return !undo.isEmpty(); }
    public boolean canRedo(){ return !redo.isEmpty(); }
    public void undo(){ if(canUndo()){ Command c = undo.pop(); c.undo(); redo.push(c);} }
    public void redo(){ if(canRedo()){ Command c = redo.pop(); c.execute(); undo.push(c);} }
}
