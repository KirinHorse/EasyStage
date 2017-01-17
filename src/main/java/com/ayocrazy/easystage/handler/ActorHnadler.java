package com.ayocrazy.easystage.handler;

import com.badlogic.gdx.scenes.scene2d.Actor;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created by ayo on 2017/1/10.
 */

public class ActorHnadler implements MethodInterceptor {
    private Actor actor;

    public Actor newActor(Class<? extends Actor> claz, Object[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(claz);
        enhancer.setCallback(this);
        if (args == null || args.length == 0) {
            return (Actor) enhancer.create();
        }
        Class argTypes[] = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            argTypes[i] = args[i].getClass();
        }
        actor = (Actor) enhancer.create(argTypes, args);
        return actor;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        return proxy.invokeSuper(obj, args);
    }
}
