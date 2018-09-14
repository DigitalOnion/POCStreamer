package com.magicleap.pocstreamer;

import android.media.Image;
import android.net.Uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

public class HttpServerThread extends Thread {
    public static final String UNKNOWN_ADDRESS = "Unknown IP Address";
    public static final int HttpServerPORT = 8888;

    private ServerSocket httpServerSocket;
    private Uri uri = null;
    private String message = null;

    private HostCallback hostCallback = null;

    public void setHostCallback(HostCallback hostCallback) {
        this.hostCallback = hostCallback;
    }

    public void setUri(Uri uri) {
        this.message = null;
        this.uri = uri;
    }

    public void setMessage(String message) {
        this.message = message;
        this.uri = null;
    }

    public void close() {
        if (httpServerSocket != null) {
            try {
                httpServerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getIpAddress() {
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces
                    = NetworkInterface.getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces.nextElement();
                Enumeration<InetAddress> enumInetAddress
                        = networkInterface.getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        return inetAddress.getHostAddress().toUpperCase() + ":" + HttpServerPORT;
                    }
                }
            }
        } catch (SocketException e) {
            return UNKNOWN_ADDRESS;
        }
        return UNKNOWN_ADDRESS;
    }

    @Override
    public void run() {
        Socket socket = null;
        try {
            httpServerSocket = new ServerSocket(HttpServerPORT);
            hostCallback.onIpAddressKnown(getIpAddress());

            while(true){
                socket = httpServerSocket.accept();

                BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String httpRequest = is.readLine();

                String msgLog = "Request of " + httpRequest
                        + " from " + socket.getInetAddress().toString();
                hostCallback.logHttpEvent(msgLog);

                hostCallback.onHttpRequestReceived(socket);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void generateHttpResponse(Socket socket, Image image) {
        HttpResponseThread httpResponseThread = null;

        if(socket!=null && image != null) {
            httpResponseThread =
                    new HttpResponseThread(
                            hostCallback,
                            socket);
            httpResponseThread.setImage(image);
            httpResponseThread.start();
        }
    }

    public void generateHttpResponse(Socket socket, String message) {
        HttpResponseThread httpResponseThread = null;

        if(socket!=null && message != null) {
            httpResponseThread =
                    new HttpResponseThread(
                            hostCallback,
                            socket);
            httpResponseThread.setMessage(message);
            httpResponseThread.start();
        }
    }

    public void generateHttpResponse(Socket socket, Uri uri) {
        HttpResponseThread httpResponseThread = null;

        if(socket!=null && uri != null) {
            httpResponseThread =
                    new HttpResponseThread(
                            hostCallback,
                            socket);
            httpResponseThread.setUri(uri);
            httpResponseThread.start();
        }
    }

}
