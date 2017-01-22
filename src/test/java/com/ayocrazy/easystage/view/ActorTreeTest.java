package com.ayocrazy.easystage.view;

import com.ayocrazy.easystage.bean.ActorBean;
import com.ayocrazy.easystage.bean.StageBean;
import com.ayocrazy.easystage.junit.LibgdxRunner;
import com.ayocrazy.easystage.junit.NeedGL;
import com.ayocrazy.easystage.junit.util.GdxThreadHelper;
import com.ayocrazy.easystage.rmi.ActorGetter;
import com.ayocrazy.easystage.rmi.StageGetter;
import com.ayocrazy.easystage.util.SkinHelper;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import net.mwplay.nativefont.NativeLabel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.number.OrderingComparison.greaterThanOrEqualTo;
import static org.junit.Assert.assertThat;

@RunWith(LibgdxRunner.class)
@NeedGL
public class ActorTreeTest {
    private ActorTree actorTree;
    private Skin skin;

    @Before
    public void setUp() throws Exception {
        skin = SkinHelper.prepareSkin();
        this.actorTree = new ActorTree(skin);
    }

    @Test
    public void shouldInitListener() throws Exception {
        assertThat(actorTree.getListeners().size, greaterThanOrEqualTo(1));
    }

    @Test
    public void shouldAddRootIfParentIsNull() throws Exception {
        ActorBean actorBean = new ActorBean();
        actorBean.setChildren(new int[]{});
        actorBean.setName("actorBean");

        actorTree.addNodes(null, actorBean);

        assertThat(actorTree.getChildren().size, is(1));
    }

    @Test
    public void shouldEnableSetActors() throws Exception {
        Stage stage = new Stage();
        NativeLabel nativeLabel = new NativeLabel("nativeLabel", skin.get(Label.LabelStyle.class));
        VerticalGroup root = new VerticalGroup();
        stage.setRoot(root);
        stage.addActor(nativeLabel);

        StageGetter stageGetter = new StageGetter(stage);
        StageBean stageBean = stageGetter.refreshStageBean();
        ActorBean nativeLabelActorBean = new ActorGetter(nativeLabel, stageGetter).refreshActor();
        ActorBean rootActorBean = new ActorGetter(root, stageGetter).refreshActor();
        actorTree.setActors(stageBean, new ActorBean[]{rootActorBean, nativeLabelActorBean});
        GdxThreadHelper.waitRunnableFinish();

        assertThat(actorTree.getRootNodes().size, is(1));
        assertThat(actorTree.getRootNodes().first().getChildren().size, is(1));
        assertThat(actorTree.getRootNodes().first().getChildren().get(0).getActor(), instanceOf(NativeLabel.class));
    }
}