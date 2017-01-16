package com.ayocrazy.easystage.rmi;

import com.ayocrazy.easystage.bean.ActorBean;
import com.ayocrazy.easystage.bean.StageBean;
import com.ayocrazy.easystage.view.EasyLog;
import com.ayocrazy.easystage.view.StageWindow;
import com.badlogic.gdx.Gdx;

import java.rmi.Naming;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ayo on 2017/1/12.
 */

public class Client {
    private static Client instance;

    private long retryInterval = 3000;
    private long queryInterval = 100;
    private Timer timer;
    private IRemote remote;
    private StageBean stageBean;
    private ActorBean actorBean;
    private StageWindow stageWindow;
    private boolean userRequest;

    public Client(StageWindow stageWindow) {
        instance = this;
        this.stageWindow = stageWindow;
        timer = new Timer();
        start(0);
    }

    public static Client get() {
        return instance;
    }

    public void stop() {
        timer.cancel();
        log(EasyLog.Tag.warn, "Interupt to server. ");
    }

    public void start(long delay) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    log(EasyLog.Tag.info, "Trying to connect Server, please wait.");
                    remote = (IRemote) Naming.lookup(Server.url);
                    log(EasyLog.Tag.info, "Server connected.");
                    userRequest = true;
                    loop();
                } catch (Exception e) {
                    log(EasyLog.Tag.warn, "connect to rmi failed, retry after " + retryInterval + " seconds");
                    start(retryInterval);
                }
            }
        }, delay);
    }

    public void loop() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    stageBean = remote.getStage();
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            stageWindow.setStageBean(stageBean);
                            loop();
                        }
                    });
                } catch (Exception e) {
                    log(EasyLog.Tag.warn, "connect to rmi failed: " + e);
                    start(1000);
                }
            }
        }, queryInterval);
    }

    public void setValue(final int objId, final String fieldName, final String methodName, final Object value) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    remote.setValue(objId, fieldName, methodName, value);
                } catch (Exception e) {
                    System.err.println("出错：" + e.toString());
                }
            }
        }, 0);
    }

    private void log(final EasyLog.Tag tag, final String msg) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                EasyLog.log(tag, msg);
            }
        });
    }

    public StageBean getStage() {
        return stageBean;
    }
}
