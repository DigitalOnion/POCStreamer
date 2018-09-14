package com.magicleap.pocstreamer;

import android.content.ContentResolver;
import android.media.Image;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.net.Socket;

public class MainActivity extends AppCompatActivity implements MainInterface, HostCallback, ImageProcessCallback {

    private HttpPanelFragment panelFragment;
    private Camera2BasicFragment cameraFragment;

    private HttpServerThread httpServerThread;

    private boolean bTestText = false;
    private boolean bTestUri = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        panelFragment = new HttpPanelFragment();
        panelFragment.setMainView(this);
        cameraFragment = new Camera2BasicFragment();
        cameraFragment.setImageCallback(this);

        ViewPager pager = findViewById(R.id.pager);
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            Fragment[] fragmentPages = new Fragment[] {
                    panelFragment,
                    cameraFragment,
            };

            @Override
            public Fragment getItem(int page) {
                return fragmentPages[page];
            }

            @Override
            public int getCount() {
                return fragmentPages.length;
            }
        });

        httpServerThread = new HttpServerThread();
        httpServerThread.setHostCallback(this);
        httpServerThread.start();
    }

    @Override
    public void logHttpEvent(String httpEvent) {

    }

    @Override
    public void onIpAddressKnown(final String ipAddress) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(panelFragment != null && panelFragment.infoIp != null) {
                    panelFragment.infoIp.setText(ipAddress);
                }
            }
        });
    }

    @Override
    public void onHttpRequestReceived(final Socket socket) {
        if(bTestText) {
            String message = panelFragment.textMessage.getText().toString();
            httpServerThread.generateHttpResponse(socket, message);
        } else if(bTestUri) {

        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                cameraFragment.takePicture(socket);
            }
        });
    }

    @Override
    public void onImageReady(final Socket socket, final Image image) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                httpServerThread.generateHttpResponse(socket, image);
            }
        });
    }

    @Override
    public ContentResolver getHostContentResolver() {
        return null;
    }

    @Override
    public String getIpAddress() {
        if(httpServerThread != null) {
            String ip = httpServerThread.getIpAddress();
            if(ip != null) {
                return ip;
            }
        }
        return HttpServerThread.UNKNOWN_ADDRESS;
    }

    @Override
    public void onClickBtnTestText(View view) {
        bTestText = !bTestText;
    }

    @Override
    public void onClickBtnTestUri(View view) {
        bTestUri = !bTestUri;
    }

}
