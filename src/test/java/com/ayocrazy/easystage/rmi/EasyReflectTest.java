package com.ayocrazy.easystage.rmi;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class EasyReflectTest {

    @Test
    public void shouldGetValue() throws Exception {
        TestObject testObject = new TestObject();
        testObject.setText("test");
        String text = (String) EasyReflect.getValue("text", testObject);
        assertThat(text, is("test"));
    }

    @Test
    public void shouldSetValue() throws Exception {
        TestObject testObject = new TestObject();
        testObject.setText("test");
        EasyReflect.setValue("text", testObject, "newValue");
        assertThat(testObject.getText(), is("newValue"));
    }


    class TestObject {
        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

}