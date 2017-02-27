// IMyAidlInterface.aidl
package net.yrom.screenrecorder;

// Declare any non-default types here with import statements
import net.yrom.screenrecorder.model.DanmakuBean;

interface IScreenRecorderAidlInterface {

    void sendDanmaku(in List<DanmakuBean> danmakuBean);

    void startScreenRecord(in Intent bundleData);

}
