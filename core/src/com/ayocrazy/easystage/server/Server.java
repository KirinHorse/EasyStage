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
    static final int port = 9126;
    static final String url = "rmi://localhost:" + port + "/IRemote";
    private Thread ServerThread;
    private Process process;
    private Stage stage;
    private StageIRemote stageRemote;
    private boolean started;


    public Server(Stage stage) {
        this.stage = stage;
        ServerThread = new Thread(this);
        ServerThread.start();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        if (!ServerThread.isAlive()) {
            started = false;
            ServerThread = new Thread(this);
            ServerThread.start();
        } else {
            stageRemote.setStage(stage);
        }
    }

    @Override
    public void run() {
        try {
            String[] cmds = {"java", "-Dfile.encoding=UTF-8", "-classpath",
                    System.getProperty("java.class.path"),
                    "com.ayocrazy.easystage.desktop.WindowLauncher"
            };
            LocateRegistry.createRegistry(port);
            try {
                Naming.rebind(url, stageRemote = new StageIRemote(stage));
                System.out.println("Service started, listen: " + port);
            } catch (Exception e) {
                System.out.println("Service failed");
                e.printStackTrace();
                return;
            }
            started = true;
            process = Runtime.getRuntime().exec(cmds);
            InputStream is = process.getErrorStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            while (started) {
                try {
                    String text = reader.readLine();
                    if (text == null) {
                        started = false;
                        return;
                    }
                    System.out.println(text);
                } catch (Exception e) {
                    started = false;
                    e.printStackTrace();
                }
            }
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        started = false;
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
