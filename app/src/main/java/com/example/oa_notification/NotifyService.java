package com.example.oa_notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Created by 胡钰 on 2016/8/15.
 */
public class NotifyService extends Service{

    private NotificationCompat.Builder mBulider;
    private Notification mNotification;
    private NotificationManager mNotificationManager;
    private BroadcastReceiver backReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
           /*
    * 这里要在朋友列表（friendactivity）中在返回键的时候发送一个广播
    * 代表程序已经进入了后台*/

        new Thread(new Runnable() {
            @Override
            public void run() {

                backReceiver=new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        Toast.makeText(context,"oa进入后台", Toast.LENGTH_SHORT).show();
                        setNotification();

                    }
                };
            }
        });

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //初始化通知栏view的方法
    private void setNotification(){

         //create a intent for activity
        Intent notifyIntent=new Intent(Intent.makeMainActivity(new ComponentName(this,FriendActivity.class)));

        //set the activity to start in a new,empty task
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);

        //create  the pendingIntent
         PendingIntent resultPendingIntent=PendingIntent.getActivity(this,0,notifyIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        mBulider=new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("oa")
                .setContentText("OA在后台运行")
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true);

        //gets an instance of the notificationmanager service
        mNotificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //builds the notification and issues it
        mNotificationManager.notify(1,mBulider.build());

    }
}
