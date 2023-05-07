package com.example.ce.ui.home;


import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;

import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.ServiceSettings;
import com.example.ce.R;
import com.example.ce.ui.login.InfoActivity;
import com.example.ce.ui.login.LoginActivity;
import com.example.ce.ui.login.StartActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class Map extends Activity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth;
    private MapView mMapView;
    private AMap aMap = null;
    private ImageView mSaLocation;

    private boolean isFirstLoc = true;//是否是首次定位

    private TextView mJieguo;
    private EditText mStartAddress;
    private EditText mDestinationAddress;
    private AutoCompleteTextView mEditAddress;
    private List<String> stringlist = new ArrayList<>();
    private List<String> stringlist2 = new ArrayList<>();

    // Var about StartPosition info & EndPosition info
    public static  double StartLatitude;
    public static  double StartLongitude;
    public static  String StartName;
    public static  double EndLatitude;
    public static  double EndLongitude;
    public static  String EndName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Initail BaiduMap Information
//        SDKInitializer.setAgreePrivacy(this.getApplicationContext(),true);
//        LocationClient.setAgreePrivacy(true);
//        SDKInitializer.initialize(this.getApplicationContext());

        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        FirebaseUser User = auth.getInstance().getCurrentUser();
        String itemid = intent.getStringExtra("itemid"); //iteminfo
        String starttime = intent.getStringExtra("starttime"); //iteminfo
        String deadline = intent.getStringExtra("deadline"); //iteminfo
        setContentView(R.layout.fragment_home) ;
        Button SearchStart = findViewById(R.id.SearchStart);
        Button Send = findViewById(R.id.Send);
        Button SearchEnd = findViewById(R.id.SearchEnd);
        Button itembutton = findViewById(R.id.Item);
        try {
            ServiceSettings.updatePrivacyShow(this, true, true);
            ServiceSettings.updatePrivacyAgree(this,true);
            //定义了一个地图view
            mMapView = (MapView) findViewById(R.id.map);
            mMapView.onCreate(savedInstanceState);// 此方法须覆写，虚拟机需要在很多情况下保存地图绘制的当前状态。
            // Define the Address String Fragment
            mStartAddress = findViewById(R.id.startAddr);
            mDestinationAddress = findViewById(R.id.destinationAddr);

            //初始化地图控制器对象
            if (aMap == null) {
                aMap = mMapView.getMap();
            }
            MyLocationStyle myLocationStyle;
            myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
            myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
            aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
            aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
            aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false
            aMap.moveCamera(CameraUpdateFactory.zoomTo(20));//地图显示级别
        } catch (Exception e) {
            System.out.print("e=" + e);
        }

        // After SearchPosition, Here will get the feedback postion info

        if (intent != null) {
            // Unbox bundle
            int tag = intent.getIntExtra("Tag",-1);
            if (tag == 0) {
                StartLatitude = intent.getDoubleExtra("Latitude",0);
                StartLongitude = intent.getDoubleExtra("Longitude",0);
                StartName = intent.getStringExtra("Name");
                //根据获取的经纬度，将地图移动到定位位置
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(StartLatitude,StartLongitude)));
                // aMap.addMarker(new MarkerOptions().position(new LatLng(StartLatitude,StartLongitude)).icon(pos_icon));
                mStartAddress.setText(StartName);
                if(EndName != null) {
                    mDestinationAddress.setText(EndName);
                }
            }
            else if (tag == 1) {
                EndLatitude = intent.getDoubleExtra("Latitude",0);
                EndLongitude = intent.getDoubleExtra("Longitude",0);
                EndName = intent.getStringExtra("Name");
                //根据获取的经纬度，将地图移动到定位位置
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(EndLatitude,EndLongitude)));
                mDestinationAddress.setText(EndName);
                if(StartName != null) {
                    mStartAddress.setText(StartName);
                }
            } else {
                System.out.print("No valid bundle now");
            }
            if(StartName != null && EndName != null) {
                // Show Path
                ;
            }

        } else {
            System.out.print("Intent Error.");
        }
        Random random = new Random();
        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                java.util.Map<String, Object> order = new HashMap<>();
                order.put("uid", User.getUid());
                order.put("RecAddress", mDestinationAddress.getText().toString());
                order.put("SntAddress", mStartAddress.getText().toString());
                order.put("Price", random.nextInt(202));
                order.put("RevUser", "zjj");
                order.put("itemid", itemid);
                order.put("StartTime", FieldValue.serverTimestamp());
                order.put("Deadline",deadline);
                order.put("status", 0);
                db.collection("Orders")
                        .add(order)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });
            }
        });
        itembutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Map.this, ItemActivity.class));
                finish();
            }
        });
        SearchStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Map.this, SearchStartpointActivity.class));
            }
        });
        SearchEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Map.this, SearchDestinationActivity.class));
            }
        });
        // setInit();
        // initMap();
    }
    public Timestamp timestampchange(String string){

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp timestamp = null;
        try {
            Date date = format.parse(string);
            timestamp = new Timestamp(date);
            Log.d(TAG, "Timestamp: " + timestamp);
            return timestamp;
        } catch (ParseException e) {
            Log.e(TAG, "Error parsing date", e);
        }
        return timestamp;
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
        //在activity executes onDestroy() mMapView.onDestroy(),Destroy Map
        mMapView.onDestroy();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

}
