package com.magicleap.pocstreamer;

import android.content.ContentResolver;
import android.media.Image;

import java.net.Socket;

public interface HostCallback {
    public void logHttpEvent(String httpEvent);
    public void onIpAddressKnown(String ipAddress);
    public void onHttpRequestReceived(Socket socket);
    public ContentResolver getHostContentResolver();
}
