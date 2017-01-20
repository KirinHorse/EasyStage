package com.ayocrazy.easystage.handler;

import com.ayocrazy.easystage.cover.CoverStage;
import com.badlogic.gdx.scenes.scene2d.Stage;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created by ayo on 2017/1/10.
 */

public class StageHandler implements MethodInterceptor {
    private Stage stage;
    private CoverStage coverStage;
    private Enhancer enhancer = new Enhancer();

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
        coverStage = new CoverStage(stage);
        return stage;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        Object result = null;
        if (method.getName().equals("act") && args.length == 1) {
            coverStage.invokeAct(proxy, obj, args);
        } else if (method.getName().equals("draw")) {
            coverStage.invokeDraw(proxy, obj, args);
        } else {
            result = proxy.invokeSuper(obj, args);
        }
        return result;
    }
}
