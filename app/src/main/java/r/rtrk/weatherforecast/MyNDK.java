package r.rtrk.weatherforecast;

public class MyNDK {
    static{
        System.loadLibrary("MyJNI");
    }

    public native int convert(int x, int y);
}
