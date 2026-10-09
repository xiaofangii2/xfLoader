package com.xf.lg;

public class Log {
    public static boolean DEBUG = false;

    public static void i(String msg) {
        System.out.println("[xiaofang/" + Thread.currentThread().getName() + "] " + msg);
    }
    public static void ok(String msg)  { i("[OK] "   + msg); }
    public static void fail(String msg){ i("[FAIL] " + msg); }
    public static void step(String msg){ i(">>> "    + msg); }
    public static void d(String msg)   { if (DEBUG) i("[DBG] " + msg); }
}
