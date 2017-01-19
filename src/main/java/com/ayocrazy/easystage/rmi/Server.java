package com.ayocrazy.easystage.rmi;

import com.ayocrazy.easystage.cover.CoverStage;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

/**
 * Created by ayo on 2017/1/12.
 */

public class Server {
    private int port = 9126;
    private String url;
    private Thread ServerThread;
    private Process process;
    private BufferedReader reader;
    private Stage stage;
    private StageIRemote stageRemote;
    private CoverStage coverStage;

    public Server(Stage stage, CoverStage coverStage) {
        this.stage = stage;
        this.coverStage = coverStage;
        ServerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                startServer();
            }
        });
        ServerThread.start();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        stageRemote.setStage(stage);
    }

    public void reopen() {
        try {
            if (reader != null) reader.close();
            ServerThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    String[] cmds = {"java", "-Dfile.encoding=UTF-8", "-classpath",
                            System.getProperty("java.class.path"),
                            "com.ayocrazy.easystage.view.WindowLauncher", Integer.toString(port)
                    };
                    try {
                        process = Runtime.getRuntime().exec(cmds);
                        InputStream is = process.getErrorStream();
                        reader = new BufferedReader(new InputStreamReader(is));
                        while (true) {
                            String text = reader.readLine();
                            if (text == null) {
                                return;
                            }
                            System.err.println(text);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            ServerThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(0);
            port = serverSocket.getLocalPort();
            serverSocket.close();
            try {
                url = "rmi://localhost:" + port + "/IRemote";
                LocateRegistry.createRegistry(port);
                Naming.rebind(url, stageRemote = new StageIRemote(stage));
                coverStage.showMessage("Server started, listen: " + port);
            } catch (Exception e) {
                coverStage.showMessage("Start server failed");
                e.printStackTrace();
                return;
            }
            reopen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
