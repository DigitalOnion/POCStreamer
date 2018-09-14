package com.magicleap.pocstreamer;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class HttpPanelFragment extends Fragment {
    private static final String SERVER_ADDRESS = "Server IP: ";
    public TextView infoIp;
    public TextView infoMsg;

    public Button btnTestText;
    public Button btnTestUri;
    public EditText textMessage;

    MainInterface mainView;

    public HttpPanelFragment() {}     // Required empty public constructor

    public void setMainView(MainInterface mainView) {
        this.mainView = mainView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_http_panel, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        infoIp = view.findViewById(R.id.infoip);
        infoMsg = view.findViewById(R.id.msg);

        infoIp.setText(SERVER_ADDRESS + mainView.getIpAddress());

        textMessage = view.findViewById(R.id.text_test);
        btnTestText = view.findViewById(R.id.btn_test_text);
        btnTestText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainView.onClickBtnTestText(view);
            }
        });

        btnTestUri = view.findViewById(R.id.btn_test_uri);
        btnTestUri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainView.onClickBtnTestUri(view);
            }
        });
    }

    public void displayIpAddress(String ipAddress) {
        if(infoIp != null) {
            infoIp.setText(ipAddress);
        }
    }



}
