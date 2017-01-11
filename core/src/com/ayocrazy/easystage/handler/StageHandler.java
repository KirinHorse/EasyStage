package com.ayocrazy.easystage.handler;

import com.ayocrazy.easystage.server.StageServer;
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
    private static Process process;
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

    boolean flag = true;

    void openWindow() {
        if (process != null) return;
        try {
            String[] cmds2 = {"java", "-Dfile.encoding=UTF-8", "-classpath",
                    System.getProperty("java.class.path"),
                    "com.ayocrazy.easystage.desktop.WindowLauncher"
            };
            LocateRegistry.createRegistry(9126);
            try {
                Naming.bind("rmi://localhost:9126/Server", new StageServer(stage));
                System.out.println("Server服务器启动成功");
            } catch (Exception e) {
                e.printStackTrace();
            }
            process = Runtime.getRuntime().exec(cmds2);
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    InputStream is = process.getErrorStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    while (flag) {
                        try {
                            String text = reader.readLine();
                            if (text == null) {
                                return;
                            }
                            System.out.println(text);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            t.start();
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        flag = false;
                        process.getOutputStream().write("\n".getBytes());
                        process.getOutputStream().flush();
                    } catch (Exception e) {
                    }
                }
            }));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
//        long startTime = System.currentTimeMillis();
        if (method.getName().equals("act") && args.length == 0) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.F6)) {
                System.out.println("F6 pressed ");
                openWindow();
            }
        }
        Object result = proxy.invokeSuper(obj, args);
//        actTime += System.currentTimeMillis() - startTime;
        return result;
    }
}
