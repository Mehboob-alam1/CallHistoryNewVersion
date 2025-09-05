package com.callos16.callscreen.colorphone.service;

import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.telecom.Call;
import android.telecom.InCallService;
import java.util.List;


public class CallManager {
    private static CallManager callManager;
    private Call call;
    private final Call.Callback callback = new Call.Callback() { 
        @Override 
        public void onStateChanged(Call call, int i) {
            super.onStateChanged(call, i);
            if (CallManager.this.listener != null) {
                CallManager.this.listener.onStateChanged(i);
            }
        }

        @Override 
        public void onDetailsChanged(Call call, Call.Details details) {
            super.onDetailsChanged(call, details);
        }

        @Override 
        public void onConferenceableCallsChanged(Call call, List<Call> list) {
            super.onConferenceableCallsChanged(call, list);
        }
    };
    private InCallService inCallService;
    private CallManagerListener listener;
    private AudioManager mAudioManager;
    private MyRecorder myRecorder;

    public static CallManager getInstance() {
        if (callManager == null) {
            callManager = new CallManager();
        }
        return callManager;
    }

    public void onAddCall(Call call, InCallService inCallService) {
        this.inCallService = inCallService;
        this.mAudioManager = (AudioManager) inCallService.getSystemService("audio");
        Call call2 = this.call;
        if (call2 != null) {
            call2.unregisterCallback(this.callback);
            this.call.disconnect();
        }
        this.call = call;
        call.registerCallback(this.callback);
        this.myRecorder = new MyRecorder(inCallService, getPhoneCall(), getState());
    }

    public void onRemoveCall(Call call) {
        this.inCallService = null;
        this.mAudioManager = null;
        Call call2 = this.call;
        if (call2 == call) {
            call2.unregisterCallback(this.callback);
            this.call = null;
        }
        MyRecorder myRecorder = this.myRecorder;
        if (myRecorder != null) {
            myRecorder.stopRecord();
            this.myRecorder = null;
        }
    }

    public void accept() {
        Call call = this.call;
        if (call != null) {
            call.answer(0);
        }
    }

    public void reject() {
        if (this.call != null) {
            if (getState() == 2) {
                this.call.reject(false, null);
            } else {
                this.call.disconnect();
            }
        }
    }

    public boolean hold() {
        if (this.call == null) {
            return false;
        }
        boolean z = getState() == 3;
        if (z) {
            this.call.unhold();
        } else {
            this.call.hold();
        }
        return !z;
    }

    public boolean switchSpeaker() {
        AudioManager audioManager = this.mAudioManager;
        if (audioManager == null || this.inCallService == null) {
            return false;
        }
        boolean isSpeakerphoneOn = audioManager.isSpeakerphoneOn();
        try {
            if (!isSpeakerphoneOn) {
                this.inCallService.setAudioRoute(8);
            } else {
                this.inCallService.setAudioRoute(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return !isSpeakerphoneOn;
    }

    public boolean muteSpeaker() {
        AudioManager audioManager = this.mAudioManager;
        if (audioManager == null || this.inCallService == null) {
            return false;
        }
        audioManager.setMode(2);
        boolean isMicrophoneMute = this.mAudioManager.isMicrophoneMute();
        InCallService inCallService = this.inCallService;
        if (inCallService != null) {
            inCallService.setMuted(!isMicrophoneMute);
        }
        return !isMicrophoneMute;
    }

    public void startRecorder() {
        MyRecorder myRecorder = this.myRecorder;
        if (myRecorder != null) {
            myRecorder.startRecord();
        }
    }

    public void onKeyPad(String str) {
        Call call = this.call;
        if (call == null) {
            return;
        }
        call.playDtmfTone(str.charAt(0));
        this.call.stopDtmfTone();
    }

    public int getState() {
        Call call = this.call;
        if (call == null || call.getDetails() == null) {
            return -1;
        }
        if (Build.VERSION.SDK_INT >= 31) {
            return this.call.getDetails().getState();
        }
        return this.call.getState();
    }

    public String getPhoneCall() {
        String decode;
        try {
            Call call = this.call;
            return (call == null || call.getDetails() == null || (decode = Uri.decode(this.call.getDetails().getHandle().toString())) == null || !decode.startsWith("tel:")) ? "" : decode.substring(decode.indexOf("tel:") + 4);
        } catch (Exception unused) {
            return "";
        }
    }

    public int getTimeCall() {
        Call call = this.call;
        if (call == null || call.getDetails().getConnectTimeMillis() == 0) {
            return 0;
        }
        return (int) ((System.currentTimeMillis() - this.call.getDetails().getConnectTimeMillis()) / 1000);
    }

    public void addListener(CallManagerListener callManagerListener) {
        this.listener = callManagerListener;
    }

    public void removeListener() {
        this.listener = null;
    }

    public static int getState(Call call) {
        if (call == null) {
            return -1;
        }
        if (Build.VERSION.SDK_INT >= 31) {
            return call.getDetails().getState();
        }
        return call.getState();
    }
}
