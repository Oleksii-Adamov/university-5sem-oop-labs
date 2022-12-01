package org.example;

import org.reflection.ClassInfoPrinter;
import org.reflection.MyClassLoader;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException {
        MyClassLoader myClassLoader = new MyClassLoader();
        //"src/main/java/org/example/ExampleClass"
        ClassInfoPrinter.printClassInfo(myClassLoader.findClass("org.example.ExampleClass"));
    }
}