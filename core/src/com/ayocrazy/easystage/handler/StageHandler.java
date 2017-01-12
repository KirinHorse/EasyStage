package com.ayocrazy.easystage.handler;

import com.ayocrazy.easystage.server.Server;
import com.ayocrazy.easystage.server.StageIRemote;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

/**
 * Created by ayo on 2017/1/10.
 */

public class StageHandler implements MethodInterceptor {
    private static Server server;
    private Stage stage;
    private Enhancer enhancer = new Enhancer();
    private long actTime, drawTime;

    public Stage newStage(Class<? extends Stage> clazz, Object... args) {
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(this);
        if (args == null || args.length == 0) {
            stage = (Stage) enhancer.create();
        } else {
            Class argTypes[] = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                argTypes[i] = args[i].getClass();
            }
            stage = (Stage) enhancer.create(argTypes, args);
        }
        return stage;
    }


    void openWindow() {
        if (server == null) {
            server = new Server(stage);
        } else {
            server.setStage(stage);
        }
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
//        long startTime = System.currentTimeMillis();
        if (method.getName().equals("act") && args.length == 0) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.F6)) {
                System.out.println("Serive is starting. ");
                openWindow();
            }
        }
        Object result = proxy.invokeSuper(obj, args);
//        actTime += System.currentTimeMillis() - startTime;
        return result;
    }
}
