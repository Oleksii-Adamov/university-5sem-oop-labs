package org.example;

public class ExampleClass implements ExampleInterface {

    private String field1;

    protected int field2;

    public int field3;

    @Override
    public void exampleInterfaceMethod() {

    }

    public ExampleClass(String field1, int field2, int field3) {
        this.field1 = field1;
        this.field2 = field2;
        this.field3 = field3;
    }

    public ExampleClass() {
        field1 = "";
        field2 = 0;
        field3 = 0;
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public int getField2() {
        return field2;
    }

    public void setField2(int field2) {
        this.field2 = field2;
    }

    public void examplePublicMethod() {

    }

    private void examplePrivateMethod() {

    }
}
