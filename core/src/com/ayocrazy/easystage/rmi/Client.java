package com.ayocrazy.easystage.rmi;

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
    private long retryInterval = 3000;
    private long queryInterval = 100;
    private Timer timer;
    private IRemote remote;
    private StageBean bean;
    private StageWindow stageWindow;

    public Client(StageWindow stageWindow) {
        this.stageWindow = stageWindow;
        timer = new Timer();
        start();
    }

    public void start() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    log(EasyLog.Tag.info, "Trying to connect Server, please wait.");
                    remote = (IRemote) Naming.lookup(Server.url);
                    log(EasyLog.Tag.info, "Server connected.");
                    loop();
                } catch (Exception e) {
                    log(EasyLog.Tag.warn, "connect to rmi failed, retry after " + retryInterval + " seconds");
                    timer.schedule(this, retryInterval);
                }
            }
        }, 0);
    }

    public void loop() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    bean = remote.getStage();
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            stageWindow.setStageBean(bean);
                        }
                    });
                } catch (Exception e) {
                    log(EasyLog.Tag.warn, "connect to rmi failed");
                    start();
                }
            }
        }, 500, queryInterval);
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
        return bean;
    }
}
