package com.ayocrazy.easystage.handler;

import com.ayocrazy.easystage.junit.LibgdxRunner;
import com.ayocrazy.easystage.junit.NeedGL;
import com.badlogic.gdx.scenes.scene2d.Stage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

@RunWith(LibgdxRunner.class)
public class StageHandlerTest {
    private StageHandler stageHandler;

    @Before
    public void setUp() throws Exception {
        this.stageHandler = new StageHandler();
    }

    @Test
    @NeedGL
    public void shouldSupportCreateStageWithoutArgs() throws Exception {
        Stage stage = stageHandler.newStage(TestStage1.class);

        assertThat(stage, not(nullValue()));
    }

    @Test
    @NeedGL
    public void shouldSupportCreateStageWithArgs() throws Exception {
        TestStage2 stage = (TestStage2) stageHandler.newStage(TestStage2.class, "testValue");

        assertThat(stage, not(nullValue()));
        assertThat(stage.getValuePassFromArgs(), is("testValue"));
    }

}

class TestStage1 extends Stage {
}

class TestStage2 extends Stage {
    private String valuePassFromArgs;

    public String getValuePassFromArgs() {
        return valuePassFromArgs;
    }

    public TestStage2(String valuePassFromArgs) {
        this.valuePassFromArgs = valuePassFromArgs;
    }
}
