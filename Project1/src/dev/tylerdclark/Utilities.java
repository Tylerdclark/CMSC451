package dev.tylerdclark;

/**
 * Using the manual JVM warmup method mentioned in the article:
 * https://www.baeldung.com/java-jvm-warmup
 */
public class JVMWarmUp {

    protected static void load(){
        for (int i = 0; i < 100000; i++) {
            ManualClass manualClass = new ManualClass();
            manualClass.method();
        }
    }

    public static class ManualClass {
        public void method(){}
    }
}
