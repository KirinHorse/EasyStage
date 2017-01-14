package com.ayocrazy.easystage.command;

import com.ayocrazy.easystage.rmi.Client;

/**
 * Created by 26286 on 2017/1/14.
 */

public class SetValueCommand extends Command {
    private int objId;
    private String fieldName, methodName;
    private Object lastValue, value;

    public SetValueCommand(int objId, String fieldName, String methodName, Object lastValue, Object value) {
        this.objId = objId;
        this.fieldName = fieldName;
        this.methodName = methodName;
        this.lastValue = lastValue;
        this.value = value;
    }

    @Override
    void exe() {
        Client.get().setValue(objId, fieldName, methodName, value);
    }

    @Override
    void unexe() {
        Client.get().setValue(objId, fieldName, methodName, lastValue);
    }
}
