package com.example.ce.ui.home;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
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
import com.example.ce.MainActivity;
import com.example.ce.R;
import com.example.ce.databinding.FragmentHomeBinding;
import com.example.ce.ui.login.StartActivity;
import com.example.ce.ui.login.LoginActivity;
import com.example.ce.ui.overlay.DrivingRouteOverlay;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.KeyPairGenerator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;



public class HomeFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance() ;
    private FragmentHomeBinding binding;
    private WebView mWebView;

    private MapView mMapView;
    private AMap aMap;

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
    public static double price;

//    private static Retrofit retrofit = null;

//    public static  Retrofit getClient(){ //creating object
//
//        if (retrofit == null){
//
//            retrofit = new Retrofit.Builder() //Retrofit.Builder class uses the Builder API to allow defining the URL end point for the HTTP operations and finally build a new Retrofit instance.
//                    //http://api.openweathermap.org/data/2.5/weather?q=London&APPID=76a35a17f3e1bce771a09f3555b614a8
//                    .baseUrl("https://api.openweathermap.org/data/2.5/")
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//        }
//        return retrofit;
//    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        //Initialize a ViewModel
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();




        mMapView = (MapView) root.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mMapView.getMap();
        }

        Intent intent = getActivity().getIntent();

        FirebaseUser User = auth.getInstance().getCurrentUser();
        String itemid = intent.getStringExtra("itemid"); //iteminfo
        String starttime = intent.getStringExtra("starttime"); //iteminfo
        String deadline = intent.getStringExtra("deadline"); //iteminfo

        Button SearchStart = root.findViewById(R.id.SearchStart);

        Button SearchEnd = root.findViewById(R.id.SearchEnd);
        // Button itembutton = root.findViewById(R.id.Item);
        Button Submit = root.findViewById(R.id.Submit);
//        Button btnBack = root.findViewById(R.id.BackButton);
//        btnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), StartActivity.class);
//                startActivity(intent);
//            }
//        });


        mContext = getActivity().getApplicationContext();

        try {
            ServiceSettings.updatePrivacyShow(mContext, true, true);
            ServiceSettings.updatePrivacyAgree(mContext,true);
            //定义了一个地图view
            mMapView = (MapView) root.findViewById(R.id.map);
            mMapView.onCreate(savedInstanceState);// 此方法须覆写，虚拟机需要在很多情况下保存地图绘制的当前状态。
            // Define the Address String Fragment
            mStartAddress = root.findViewById(R.id.startAddr);
            mDestinationAddress = root.findViewById(R.id.destinationAddr);

            mRouteSearch = new RouteSearchV2(mContext);
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
//                    mBottomLayout.setVisibility(View.VISIBLE);
                                distance = (int) drivePath.getDistance();
//                    int dur = (int) drivePath.getDuration();
//                    String des = AMapUtil.getFriendlyTime(dur)+"("+AMapUtil.getFriendlyLength(dis)+")";
//                    mRotueTimeDes.setText(des);
//                    mRouteDetailDes.setVisibility(View.VISIBLE);
//                    int taxiCost = (int) mDriveRouteResult.getTaxiCost();
//                    mRouteDetailDes.setText("打车约"+taxiCost+"元");
//                    mBottomLayout.setOnClickListener(new OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(mContext,
//                                    DriveRouteDetailActivity.class);
//                            intent.putExtra("drive_path", drivePath);
//                            intent.putExtra("drive_result",
//                                    mDriveRouteResult);
//                            startActivity(intent);
//                        }
//                    });

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
                //根据获取的经纬度，将地图移动到定位位置
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

        } else {
            System.out.print("Intent Error.");
        }


        Random random = new Random();

//        Send.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                java.util.Map<String, Object> order = new HashMap<>();
//                order.put("uid", User.getUid());
//                order.put("RecAddress", mDestinationAddress.getText().toString());
//                order.put("SntAddress", mStartAddress.getText().toString());
//                order.put("Price", random.nextInt(202));
//                order.put("RevUser", "zjj");
//                order.put("itemid", itemid);
//                order.put("StartTime", FieldValue.serverTimestamp());
//                order.put("Deadline",deadline);
//                order.put("status", 0);
//                db.collection("Orders")
//                        .add(order)
//                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                            @Override
//                            public void onSuccess(DocumentReference documentReference) {
//                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.w(TAG, "Error adding document", e);
//                            }
//                        });
//            }
//        });

        SearchStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, SearchStartpointActivity.class));
            }
        });

        SearchEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, SearchDestinationActivity.class));
            }
        });

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StartName == null || EndName == null) {
                    Toast.makeText(mContext, "Please choose position first!",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    // Get price
                    if (distance <= 3000) {
                        price = 10;
                    }
                    else {
                        price = ((distance - 3000)/1000)*3 + 10;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString("StartName",StartName);
                    bundle.putString("EndName",EndName);
                    bundle.putDouble("StartLatitude",StartLatitude);
                    bundle.putDouble("StartLongitude",StartLongitude);
                    bundle.putDouble("EndLatitude",StartLatitude);
                    bundle.putDouble("EndLongitude",EndLongitude);
                    bundle.putInt("distance",distance);
                    bundle.putDouble("price",price);
                    Intent SubmitIntent = new Intent(mContext, ItemActivity.class);
                    SubmitIntent.putExtras(bundle);
                    StartName = null;
                    EndName = null;
                    startActivity(SubmitIntent);
                }
            }
        });
        return root;
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
    public void onSaveInstanceState(Bundle outState) {
        Log.i("sys", "mf onSaveInstanceState");
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        Log.i("sys", "mf onResume");
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        Log.i("sys", "mf onPause");
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mMapView.onDestroy();
        binding = null;
    }
}
