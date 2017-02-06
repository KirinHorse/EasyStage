package com.ayocrazy.easystage.rmi;

import com.ayocrazy.easystage.EasyConfig;
import com.ayocrazy.easystage.bean.ActorBean;
import com.ayocrazy.easystage.bean.StageBean;
import com.ayocrazy.easystage.view.ActorTree;
import com.ayocrazy.easystage.view.ActorWindow;
import com.ayocrazy.easystage.view.EasyLog;
import com.ayocrazy.easystage.view.StageWindow;
import com.badlogic.gdx.Gdx;

import java.io.PrintStream;
import java.rmi.Naming;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ayo on 2017/1/12.
 */

public class Client {
    private static Client instance;
    private int port;
    private long retryInterval = 3000;
    private long queryInterval = 100;
    private Timer timer;
    private IRemote remote;
    private StageBean stageBean;
    private ActorBean[] actorBeans;
    private ActorBean currentActor;
    private StageWindow stageWindow;
    private ActorWindow actorWindow;
    private ActorTree actorTree;

    public Client(StageWindow stageWindow, ActorTree actorTree, ActorWindow actorWindow) {
        instance = this;
        this.stageWindow = stageWindow;
        this.actorTree = actorTree;
        this.actorWindow = actorWindow;
        timer = new Timer();
        if (EasyConfig.port != 0) {
            port = EasyConfig.port;
        }
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
                    remote = (IRemote) Naming.lookup("rmi://localhost:" + port + "/IRemote");
                    log(EasyLog.Tag.info, "Server connected.");
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
                    StageBean stage = remote.getStage();
                    if (childrenChanged(stage.getChildren())) {
                        setCurrentActor(null);
                        actorBeans = remote.getActors();
                        actorTree.setActors(stage, actorBeans);
                    }
                    if (currentActor != null) {
                        ActorBean actor = remote.getActor(currentActor.getId());
                        currentActor = actor;
                    }
                    stageBean = stage;
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            stageWindow.setStageBean(stageBean);
                            actorWindow.setBean(currentActor);
                            loop();
                        }
                    });
                } catch (Exception e) {
                    log(EasyLog.Tag.warn, "connect to rmi failed: " + e);
                    e.printStackTrace(new PrintStream(System.err));
                    start(1000);
                }
            }
        }, queryInterval);
    }

    private boolean childrenChanged(int[] actors) {
        if (stageBean == null) return true;
        int[] last = stageBean.getChildren();
        if (last.length != actors.length) return true;
        for (int i = 0; i < last.length; i++) {
            if (last[i] != actors[i]) return true;
        }
        return false;
    }

    public void setValue(final int objId, final String fieldName, final String methodName, final Object value) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    remote.setValue(objId, fieldName, methodName, value);
                } catch (Exception e) {
                    System.err.println("error：" + e.toString());
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

    public void setCurrentActor(ActorBean currentActor) {
        if (this.currentActor != null)
            setValue(this.currentActor.getId(), "debug", "default", false);
        if (currentActor != null)
            setValue(currentActor.getId(), "debug", "default", true);
        this.currentActor = currentActor;
    }

    public StageBean getStage() {
        return stageBean;
    }
}
