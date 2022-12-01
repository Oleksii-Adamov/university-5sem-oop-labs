package org.entities;

import java.io.Serializable;

public class TestEntity implements Serializable {
    private String string;
    private int integer;

    public TestEntity(String string, int integer) {
        this.string = string;
        this.integer = integer;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public int getInteger() {
        return integer;
    }

    public void setInteger(int integer) {
        this.integer = integer;
    }

    @Override
    public String toString() {
        return "TestEntity{" +
                "string='" + string + '\'' +
                ", integer=" + integer +
                '}';
    }
}
