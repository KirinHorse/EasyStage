package com.ayocrazy.easystage.rmi;

import com.badlogic.gdx.scenes.scene2d.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by ayo on 2017/1/12.
 */

public class Server implements Runnable {
    private int port = 9126;
    private Registry registry;
    private String url;
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
        stageRemote.setStage(stage);
    }

    @Override
    public void run() {
        try {
            while (true) {
                try {
                    Socket socket = new Socket("localhost", port);
                    socket.close();
                    port++;
                } catch (IOException e) {
                    break;
                }
            }
            try {
                url = "rmi://localhost:" + port + "/IRemote";
                registry = LocateRegistry.createRegistry(port);
                Naming.rebind(url, stageRemote = new StageIRemote(stage));
                System.out.println("Server started, listen: " + port);
            } catch (Exception e) {
                System.out.println("Server failed");
                e.printStackTrace();
                return;
            }
            started = true;
            String[] cmds = {"java", "-Dfile.encoding=UTF-8", "-classpath",
                    System.getProperty("java.class.path"),
                    "com.ayocrazy.easystage.view.WindowLauncher", Integer.toString(port)
            };
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
                    System.err.println(text);
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
