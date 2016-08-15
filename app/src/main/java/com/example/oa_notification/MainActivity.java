package com.example.oa_notification;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.Queue;

public class MainActivity extends AppCompatActivity {

    private Notification mNotification;
    private .NotificationCompat.Builder mBuilder;
    private NotificationManager mNotificationManager;
    private int chatnews=123;

    //用handler来更新消息通知栏
    private Handler mhandler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case chatnews:

                    TranObject<TextMessage> textObject = (TranObject<TextMessage>) msg
                            .getData().getSerializable("msg");
                    // System.out.println(textObject);
                    if (textObject != null) {
                        int form = textObject.getFromUser();// 消息从哪里来
                        String content = textObject.getObject().getMessage();// 消息内容

                        // 更新通知栏

                        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                        int icon = R.mipmap.ic_launcher;
                        CharSequence tickerText = form + ":" + content;
                        long when = System.currentTimeMillis();

                        mBuilder=new NotificationCompat.Builder(getApplicationContext())
                                .setSmallIcon(icon)
                                .setContentText(tickerText)
                                .setAutoCancel(true)
                                .setWhen(when);

                        mNotification.flags = Notification.FLAG_NO_CLEAR;
                        // 设置默认声音
                        mNotification.defaults |= Notification.DEFAULT_SOUND;

                        mNotificationManager.notify(1, mNotification);// 通知一下才会生效哦
                    }
            break;

            default:
            break;
          }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


            //监听消息，我们监听收消息线程中收到的消息先判断程序是否运行在后台，如果在后台，我们就直接把消息发送给handler，
    // 如果不是，就通过广播发送出去这个消息，所以：我们首先需要在按返回键的进入后台的时候，做一个标记，表示程序进入后台运行，可以保存到应用的全局变量
            in.setMessageListener(new MessageListener() {

            @Override
                public void Message(TranObject msg) {

                //app退出来的时候做一个标记
            if (isrunning==1) {
            // 如果 是在后台运行，就更新通知栏，否则就发送广播
                if (msg.getType() == TranObjectType.MESSAGE) {// 只处理文本消息类型

                // 把消息对象发送到handler去处理
            //在message池中返回一个新的massage对象
                    Message message = mhandler.obtainMessage();
                    message.what =chatnews;
                    message.getData().putSerializable("msg", msg);
                    mhandler.sendMessage(message);
                }
            } else {

            //发送个什么广播，这是随便写的
                        Intent broadCast = new Intent();
                        broadCast.setAction(SyncStateContract.Constants.ACTION);
                        broadCast.putExtra(SyncStateContract.Constants.MSGKEY, msg);
                        sendBroadcast(broadCast);// 把收到的消息已广播的形式发送出去
                    }
             }
            });
        }
}
