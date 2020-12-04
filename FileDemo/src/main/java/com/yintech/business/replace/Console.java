package com.yintech.business.replace;

interface IConsole {
    public void consoleLog(String string);
    public void consoleError(String string);
}

public class Console {
    private static IConsole delegate;
    public static IConsole getDelegate() {
        return delegate;
    }
    public static void setDelegate(IConsole delegate) {
        Console.delegate = delegate;
    }
    public static void log(String string) {
        if (delegate != null) {
            delegate.consoleLog(string);
        } else {
            System.out.print(string);
        }
    }
    public static void error(String string) {
        if (delegate != null) {
            delegate.consoleError(string);
        } else {
            System.out.print(string);
        }
    }
    public static void logLine(String string) {
        log(string + "\n");
    }
    public static void errorLine(String string) {
        error(string + "\n");
    }
}
