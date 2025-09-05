package com.callos16.callscreen.colorphone.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.provider.MediaStore;
import androidx.core.app.NotificationCompat;
import com.callos16.callscreen.colorphone.ActivityCall;
import com.callos16.callscreen.colorphone.R;
import com.callos16.callscreen.colorphone.broadcase.MyCallReceiver;
import com.callos16.callscreen.colorphone.utils.MyConst;
import com.callos16.callscreen.colorphone.utils.OtherUtils;
import com.callos16.callscreen.colorphone.utils.ReadContact;


import java.io.IOException;


public class MyNotificationManager {
    private final Context c;
    private final NotificationManager manager;
    private final int CALL_NOTIFICATION_ID = 12325;
    private final int ACCEPT_CALL_CODE = 0;
    private final int DECLINE_CALL_CODE = 1;

    public MyNotificationManager(Context context) {
        this.c = context;
        this.manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void setupNotification(final boolean z) {
        final String phoneCall = CallManager.getInstance().getPhoneCall();
        final Handler handler = new Handler(new Handler.Callback() { 
            @Override 
            public final boolean handleMessage(Message message) {
                return MyNotificationManager.this.m236x28553fb0(z, phoneCall, message);
            }
        });
        new Thread(new Runnable() { 
            @Override 
            public final void run() {
                MyNotificationManager.this.m237x9284c7cf(phoneCall, handler);
            }
        }).start();
    }

    
    
    public  boolean m236x28553fb0(boolean z, String str, Message message) {
        String str2;
        String str3;
        Bitmap bitmap;
        String[] strArr = (String[]) message.obj;
        int state = CallManager.getInstance().getState();
        boolean z2 = ((PowerManager) this.c.getSystemService(Context.POWER_SERVICE)).isInteractive() && state == 2 && z;
        if (z2) {
            str2 = "call_notification_channel_high_priority";
            str3 = "simple_dialer_call_high_priority";
        } else {
            str2 = "call_notification_channel";
            str3 = "simple_dialer_call";
        }
        if (Build.VERSION.SDK_INT >= 26) {
            this.manager.createNotificationChannel(new NotificationChannel(str3, str2, z2 ? NotificationManager.IMPORTANCE_HIGH : NotificationManager.IMPORTANCE_DEFAULT));
        }
        PendingIntent activity = PendingIntent.getActivity(this.c, 0, ActivityCall.makeIntent(this.c), PendingIntent.FLAG_MUTABLE);
        Intent intent = new Intent(this.c, MyCallReceiver.class);
        intent.setAction(MyConst.ACCEPT_CALL);
        PendingIntent broadcast = PendingIntent.getBroadcast(this.c, 0, intent, 301989888);
        Intent intent2 = new Intent(this.c, MyCallReceiver.class);
        intent2.setAction(MyConst.DECLINE_CALL);
        PendingIntent broadcast2 = PendingIntent.getBroadcast(this.c, 1, intent2, 301989888);
        String str4 = strArr[0];
        if (str4.isEmpty()) {
            str4 = str;
        }
        int i = R.string.ongoing_call;
        if (state == 1) {
            i = R.string.dialing;
        } else if (state == 2) {
            i = R.string.is_calling;
        } else if (state == 7) {
            i = R.string.call_ended;
        } else if (state == 10) {
            i = R.string.call_ending;
        }
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.c.getContentResolver(), Uri.parse(strArr[1]));
        } catch (IOException unused) {
            bitmap = null;
        }
        if (bitmap != null) {
            bitmap = OtherUtils.getCroppedBitmap(bitmap);
        }
        int i2 = z2 ? 2 : 0;
        NotificationCompat.Action build = new NotificationCompat.Action.Builder((int) R.drawable.ic_accept_call, this.c.getString(R.string.accept), broadcast).build();
        NotificationCompat.Action build2 = new NotificationCompat.Action.Builder((int) R.drawable.ic_decline_call, this.c.getString(R.string.decline), broadcast2).build();
        NotificationCompat.Builder style = new NotificationCompat.Builder(this.c, str3).setSmallIcon(R.drawable.ic_call_notification).setContentIntent(activity).setContentTitle(str4).setContentText(this.c.getString(i)).setPriority(i2).setCategory(NotificationCompat.CATEGORY_CALL).setOngoing(true).setSound(null).setUsesChronometer(state == 4).setChannelId(str3).setStyle(new NotificationCompat.DecoratedCustomViewStyle());
        if (state == 2) {
            style.addAction(build);
        }
        style.addAction(build2);
        if (bitmap != null) {
            style.setLargeIcon(bitmap);
        }
        if (z2) {
            style.setFullScreenIntent(activity, true);
        }
        this.manager.notify(12325, style.build());
        return true;
    }

    
    
    public  void m237x9284c7cf(String str, Handler handler) {
        String[] namePhoto = ReadContact.getNamePhoto(this.c, str);
        Message message = new Message();
        message.what = 1;
        message.obj = namePhoto;
        handler.sendMessage(message);
    }

    public void cancelNotification() {
        this.manager.cancel(12325);
    }
}
