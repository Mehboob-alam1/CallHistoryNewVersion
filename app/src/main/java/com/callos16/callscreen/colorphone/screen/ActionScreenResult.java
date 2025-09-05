package com.callos16.callscreen.colorphone.screen;


public interface ActionScreenResult {
    void onAccept();

    void onHold();

    void onMute();

    void onOpenContact();

    void onPadClick(String str);

    void onRecorder();

    void onReject();

    void onSpeaker();
}
