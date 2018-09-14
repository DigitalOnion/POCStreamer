package com.magicleap.pocstreamer;

import android.media.Image;

import java.net.Socket;

public interface ImageProcessCallback {
    public void onImageReady(Socket socket, Image image);
}
