package com.ayocrazy.easystage.rmi;

import com.ayocrazy.easystage.bean.ActorBean;
import com.ayocrazy.easystage.bean.BaseBean;
import com.ayocrazy.easystage.bean.StageBean;
import com.ayocrazy.easystage.bean.UserBean;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by ayo on 2017/1/10.
 */

public interface IRemote extends Remote {
    StageBean getStage() throws RemoteException;

    ActorBean getActor(int id) throws RemoteException;

    ActorBean[] getActors() throws RemoteException;

    boolean setValue(int id, String fieldName, String methodName, Object value) throws RemoteException;

}
