package com.easyranktools.easyranktools.broadcase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.easyranktools.easyranktools.ActivityCall;
import com.easyranktools.easyranktools.service.CallManager;
import com.easyranktools.easyranktools.utils.MyConst;


public class MyCallReceiver extends BroadcastReceiver {
    @Override 
    public void onReceive(Context context, Intent intent) {
        if (context == null || intent == null || intent.getAction() == null) {
            return;
        }
        if (intent.getAction().equals(MyConst.ACCEPT_CALL)) {
            context.startActivity(ActivityCall.makeIntent(context));
            CallManager.getInstance().accept();
            return;
        }
        CallManager.getInstance().reject();
    }
}
