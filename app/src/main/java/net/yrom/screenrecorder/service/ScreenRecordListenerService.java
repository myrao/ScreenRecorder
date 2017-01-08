package net.yrom.screenrecorder.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import net.yrom.screenrecorder.MainActivity;
import net.yrom.screenrecorder.R;
import net.yrom.screenrecorder.view.MyWindowManager;

/**
 * author : raomengyang on 2016/12/29.
 */

public class ScreenRecordListenerService extends Service {
    private static final String TAG = "ScreenRecordListenerService, ";

    public static final int PENDING_REQUEST_CODE = 0x01;
    private static final int NOTIFICATION_ID = 3;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder builder;
    private Handler handler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();
        initNotification();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 当前界面是桌面，且没有悬浮窗显示，则创建悬浮窗。
        if (!MyWindowManager.isWindowShowing()) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    MyWindowManager.createSmallWindow(getApplicationContext());
                }
            });
        }

        return super.onStartCommand(intent, flags, startId);
    }


    private void initNotification() {
        builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText("您正在录制视频内容哦")
                .setOngoing(true)
                .setDefaults(Notification.DEFAULT_VIBRATE);

        Intent backIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, PENDING_REQUEST_CODE, backIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mNotificationManager != null) {
            mNotificationManager.cancel(NOTIFICATION_ID);
        }
        MyWindowManager.removeSmallWindow(getApplicationContext());
    }

}
