package com.example.ce.ui.home;

import static android.content.ContentValues.TAG;


import android.content.Context;

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

import android.widget.Toast;

import androidx.annotation.NonNull;

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

import com.example.ce.R;
import com.example.ce.databinding.FragmentHomeBinding;

import com.example.ce.ui.overlay.DrivingRouteOverlay;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.FirebaseFirestore;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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



        mContext = getActivity().getApplicationContext();

        try {
            ServiceSettings.updatePrivacyShow(mContext, true, true);
            ServiceSettings.updatePrivacyAgree(mContext,true);

            mMapView = (MapView) root.findViewById(R.id.map);
            mMapView.onCreate(savedInstanceState);
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
                    aMap.clear();
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
                                drivingRouteOverlay.setNodeIconVisibility(false);
                                drivingRouteOverlay.setIsColorfulline(true);
                                drivingRouteOverlay.removeFromMap();
                                drivingRouteOverlay.addToMap();
                                drivingRouteOverlay.zoomToSpan();

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
                    LatLonPoint mStartPoint = new LatLonPoint(StartLatitude, StartLongitude);
                    LatLonPoint mEndPoint = new LatLonPoint(EndLatitude, EndLongitude);
                    RouteSearchV2.FromAndTo fromAndTo = new RouteSearchV2.FromAndTo(mStartPoint, mEndPoint);
                    RouteSearchV2.DriveRouteQuery query = new RouteSearchV2.DriveRouteQuery(fromAndTo, RouteSearchV2.DrivingStrategy.DEFAULT, null,
                            null, "");

                    query.setShowFields(RouteSearchV2.ShowFields.POLINE | RouteSearchV2.ShowFields.CITIES |
                            RouteSearchV2.ShowFields.COST | RouteSearchV2.ShowFields.NAVI | RouteSearchV2.ShowFields.TMCS);
                    mRouteSearch.calculateDriveRouteAsyn(query);
            }

        } else {
            System.out.print("Intent Error.");
        }


        Random random = new Random();



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
