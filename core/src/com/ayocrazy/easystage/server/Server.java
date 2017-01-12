package com.ayocrazy.easystage.server;

import com.badlogic.gdx.scenes.scene2d.Stage;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

/**
 * Created by ayo on 2017/1/12.
 */

public class Server implements Runnable {
    private Thread ServerThread;
    private boolean flag = true;
    private Process process;
    private Stage stage;

    public Server(Stage stage) {
        ServerThread = new Thread(this);
        ServerThread.start();
    }

    @Override
    public void run() {
        try {
            String[] cmds2 = {"java", "-Dfile.encoding=UTF-8", "-classpath",
                    System.getProperty("java.class.path"),
                    "com.ayocrazy.easystage.desktop.WindowLauncher"
            };
            LocateRegistry.createRegistry(9126);
            try {
                Naming.bind("rmi://localhost:9126/IRemote", new StageIRemote(stage));
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
}
