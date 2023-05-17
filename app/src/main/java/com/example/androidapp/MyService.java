package com.example.androidapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MyService extends Service {
    // private static final long THREE_HOURS = 3 * 60 * 60 * 1000; // 3小时的毫秒数
    private static final long THREE_HOURS =   5 * 1000; // 5秒钟的毫秒数用于测试
    private Timer timer;
    private  boolean notify_signal = false;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 在服务启动时进行计时
        startTimer();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 在服务销毁时移除计时任务
        stopTimer();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startTimer() {
        notify_signal = false;
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    // 检查距离上次关闭的时间是否超过3个小时
                    // 获取上次关闭时间
                    long lastCloseTime = getLastCloseTime();
                    // 检查距离上次关闭的时间是否超过3个小时
                    long timeSinceLastClose = System.currentTimeMillis() - lastCloseTime;;
                    if (timeSinceLastClose >= THREE_HOURS && notify_signal == false) {
                        showNotification();
                        notify_signal = true;
                    }
                }
            }, 0, 1000); // 每隔半小时检查一次
        }
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 创建通知渠道
            NotificationChannel channel = new NotificationChannel(
                    "channel_id",
                    "Channel Name",
                    NotificationManager.IMPORTANCE_DEFAULT);

            // 设置渠道的其他属性
            channel.setDescription("Channel Description");
            // 可以设置其他属性，如灯光、震动等

            // 获取通知管理器并创建渠道
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    private void showNotification() {
        // 创建通知并发送给用户
        // 这里使用NotificationCompat进行示例，您可以根据需求自定义通知内容
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                .setSmallIcon(R.drawable.baseline_query_builder_24)
                .setContentTitle("Reminder")
                .setContentText("You haven't interacted with ChatGPT for 3 hours.")
                .setContentIntent(createPendingIntent())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());
    }
    private PendingIntent createPendingIntent() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("openChatFragment", true); // 通过Intent传递一个标志，表示要打开聊天界面
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE);
        return pendingIntent;
    }

    private long getLastCloseTime() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return prefs.getLong("last_close_time", 0);
    }
}

