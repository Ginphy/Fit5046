package com.example.ce.ui.home;


import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.services.core.ServiceSettings;


import com.amap.api.maps.MapView;
import com.amap.api.services.core.ServiceSettings;
import com.example.ce.R;
import com.example.ce.ui.login.LoginActivity;


import java.util.ArrayList;
import java.util.List;


public class Map extends Activity {

    private MapView mMapView;
    private AMap aMap = null;
    private ImageView mSaLocation;

    private boolean isFirstLoc = true;//是否是首次定位

    private double mLatitude;
    private double mLongitude;
    private TextView mJieguo;
    private AutoCompleteTextView mEditAddress;
    private List<String> stringlist = new ArrayList<>();
    private List<String> stringlist2 = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Initail BaiduMap Information
//        SDKInitializer.setAgreePrivacy(this.getApplicationContext(),true);
//        LocationClient.setAgreePrivacy(true);
//        SDKInitializer.initialize(this.getApplicationContext());

        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_home) ;
        try {
            ServiceSettings.updatePrivacyShow(this, true, true);
            ServiceSettings.updatePrivacyAgree(this,true);
            //定义了一个地图view
            mMapView = (MapView) findViewById(R.id.map);
            mMapView.onCreate(savedInstanceState);// 此方法须覆写，虚拟机需要在很多情况下保存地图绘制的当前状态。
            //初始化地图控制器对象
            if (aMap == null) {
                aMap = mMapView.getMap();
            }
        } catch (Exception e) {
            System.out.print("e=" + e);
        }





        // setInit();
        // initMap();
    }




    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
}
