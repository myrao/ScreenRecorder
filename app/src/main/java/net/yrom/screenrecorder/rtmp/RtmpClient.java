package net.yrom.screenrecorder.rtmp;

/**
 * Created by lake on 16-3-30.
 * Modified by raomengyang 17-3-12
 */
public class RtmpClient {

    /**
     * @param url
     * @param isPublishMode
     * @return rtmpPointer ,pointer to native rtmp struct
     */
    public static native long open(String url, boolean isPublishMode);

    public static native int read(long rtmpPointer, byte[] data, int offset, int size);

    public static native int write(long rtmpPointer, byte[] data, int size, int type, int ts);

    public static native int close(long rtmpPointer);

    public static native String getIpAddr(long rtmpPointer);

}
