package com.ayocrazy.easystage.command;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;

import java.util.Stack;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CommandTest {
    private Command mockCommand;

    @Before
    public void setUp() throws Exception {
        mockCommand = mock(Command.class);
    }

    @Test
    public void shouldEnableUndo() throws Exception {
        Command.doCmd(mockCommand);
        verify(mockCommand).exe();
        Stack<Command> cmds = (Stack<Command>) Whitebox.getInternalState(mockCommand, "cmds");
        assertThat(cmds, hasSize(1));

        Command.undo();
        verify(mockCommand).unexe();
    }
}