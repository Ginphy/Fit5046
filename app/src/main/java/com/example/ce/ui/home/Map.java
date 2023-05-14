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
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;

import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.ServiceSettings;
import com.amap.api.services.route.BusRouteResultV2;
import com.amap.api.services.route.DrivePathV2;
import com.amap.api.services.route.DriveRouteResultV2;
import com.amap.api.services.route.RideRouteResultV2;
import com.amap.api.services.route.RouteSearchV2;
import com.amap.api.services.route.WalkRouteResultV2;
import com.example.ce.R;

import com.example.ce.ui.overlay.DrivingRouteOverlay;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;




public class Map extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth;
    private MapView mMapView;
    private AMap aMap = null;
    private ImageView mSaLocation;

    private boolean isFirstLoc = true;//是否是首次定位

    private TextView mJieguo;
    private EditText mStartAddress;
    private EditText mDestinationAddress;
    private RouteSearchV2 mRouteSearch;
    private AutoCompleteTextView mEditAddress;
    private List<String> stringlist = new ArrayList<>();
    private List<String> stringlist2 = new ArrayList<>();
    private Context mContext;

    // Var about StartPosition info & EndPosition info
    public static  double StartLatitude;
    public static  double StartLongitude;
    public static  String StartName;
    public static  double EndLatitude;
    public static  double EndLongitude;
    public static  String EndName;
    // Distance value (per Meters)
    public static int distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        FirebaseUser User = auth.getInstance().getCurrentUser();
        String itemid = intent.getStringExtra("itemid"); //iteminfo
        String starttime = intent.getStringExtra("starttime"); //iteminfo
        String deadline = intent.getStringExtra("deadline"); //iteminfo
        setContentView(R.layout.fragment_home) ;
        Button SearchStart = findViewById(R.id.SearchStart);

        Button SearchEnd = findViewById(R.id.SearchEnd);
        // Button itembutton = findViewById(R.id.Item);
        Button Submit = findViewById(R.id.Submit);
        mContext = this.getApplicationContext();
        try {
            ServiceSettings.updatePrivacyShow(this, true, true);
            ServiceSettings.updatePrivacyAgree(this,true);
            //定义了一个地图view
            mMapView = (MapView) findViewById(R.id.map);
            mMapView.onCreate(savedInstanceState);// 此方法须覆写，虚拟机需要在很多情况下保存地图绘制的当前状态。
            // Define the Address String Fragment
            mStartAddress = findViewById(R.id.startAddr);
            mDestinationAddress = findViewById(R.id.destinationAddr);

            mRouteSearch = new RouteSearchV2(this);
            mRouteSearch.setRouteSearchListener(new RouteSearchV2.OnRouteSearchListener() {
                @Override
                public void onBusRouteSearched(BusRouteResultV2 busRouteResult, int i) {

                }
                @Override
                public void onDriveRouteSearched(DriveRouteResultV2 result, int errorCode) {
                    aMap.clear();// 清理地图上的所有覆盖物
                    if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
                        if (result != null && result.getPaths() != null) {
                            if (result.getPaths().size() > 0) {
                                final DrivePathV2 drivePath = result.getPaths()
                                        .get(0);
                                if (drivePath == null) {
                                    return;
                                }
                                DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
                                        mContext, aMap, drivePath,
                                        result.getStartPos(),
                                        result.getTargetPos(), null);
                                drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
                                drivingRouteOverlay.setIsColorfulline(true);//是否用颜色展示交通拥堵情况，默认true
                                drivingRouteOverlay.removeFromMap();
                                drivingRouteOverlay.addToMap();
                                drivingRouteOverlay.zoomToSpan();

                                distance = (int) drivePath.getDistance();

                            } else if (result != null && result.getPaths() == null) {
                                Toast.makeText(mContext, "No result!",
                                        Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(mContext, "No result!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(mContext, "Unkonw error!",
                                Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onWalkRouteSearched(WalkRouteResultV2 walkRouteResult, int i) {

                }

                @Override
                public void onRideRouteSearched(RideRouteResultV2 rideRouteResult, int i) {

                }
            });

            //初始化地图控制器对象
            if (aMap == null) {
                aMap = mMapView.getMap();
            }
            MyLocationStyle myLocationStyle;
            myLocationStyle = new MyLocationStyle();
            myLocationStyle.interval(2000);
            aMap.setMyLocationStyle(myLocationStyle);
            aMap.getUiSettings().setMyLocationButtonEnabled(true);
            aMap.setMyLocationEnabled(true);
            aMap.moveCamera(CameraUpdateFactory.zoomTo(20));
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

                aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(StartLatitude,StartLongitude)));
                aMap.addMarker(new MarkerOptions().position(new LatLng(StartLatitude,StartLongitude)).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_start)));
                mStartAddress.setText(StartName);
                if(EndName != null) {
                    mDestinationAddress.setText(EndName);
                }
            }
            else if (tag == 1) {
                EndLatitude = intent.getDoubleExtra("Latitude",0);
                EndLongitude = intent.getDoubleExtra("Longitude",0);
                EndName = intent.getStringExtra("Name");

                aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(EndLatitude,EndLongitude)));
                aMap.addMarker(new MarkerOptions().position(new LatLng(EndLatitude,EndLongitude)).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_end)));
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
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StartName == null || EndName == null) {
                    Toast.makeText(Map.this, "Please choose position first!",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    LatLonPoint mStartPoint = new LatLonPoint(StartLatitude, StartLongitude);
                    LatLonPoint mEndPoint = new LatLonPoint(EndLatitude, EndLongitude);


                    RouteSearchV2.FromAndTo fromAndTo = new RouteSearchV2.FromAndTo(mStartPoint, mEndPoint);

                    RouteSearchV2.DriveRouteQuery query = new RouteSearchV2.DriveRouteQuery(fromAndTo, RouteSearchV2.DrivingStrategy.DEFAULT, null,
                            null, "");
                    //不加此行代码，一些数据不会返回
                    query.setShowFields(RouteSearchV2.ShowFields.POLINE | RouteSearchV2.ShowFields.CITIES |
                            RouteSearchV2.ShowFields.COST | RouteSearchV2.ShowFields.NAVI | RouteSearchV2.ShowFields.TMCS);

                    mRouteSearch.calculateDriveRouteAsyn(query);
                }
            }
        });
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

        mMapView.onSaveInstanceState(outState);
    }

}
