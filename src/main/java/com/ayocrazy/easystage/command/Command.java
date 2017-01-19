package com.ayocrazy.easystage.command;

import java.util.Stack;

/**
 * Created by 26286 on 2017/1/14.
 */

public abstract class Command {
    private static int maxCmds = 50;
    protected static final Stack<Command> cmds = new Stack<>();
    private static int index = -1;

    public static void doCmd(Command cmd) {
        while (cmds.size() - 1 > index) {
            cmds.pop();
        }
        cmd.exe();
        cmds.push(cmd);
        if (index >= maxCmds) {
            cmds.remove(cmds.firstElement());
        } else index++;
    }

    public static void redo() {
        if (index < cmds.size() - 1)
            cmds.get(++index).exe();
    }

    public static void undo() {
        if (index >= 0)
            cmds.get(index--).unexe();
    }

    abstract void exe();

    abstract void unexe();
}
