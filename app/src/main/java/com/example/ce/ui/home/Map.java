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

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;

import com.example.ce.R;
import com.example.ce.ui.login.LoginActivity;


import java.util.ArrayList;
import java.util.List;

public class Map extends Activity implements View.OnClickListener {

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private ImageView mSaLocation;
    private LocationClient mLocationClient;
    private LatLng latLng;
    private boolean isFirstLoc = true;//是否是首次定位
    public BDAbstractLocationListener myListener = new MyLocationListener();
    private MyLocationData locData;
    private double mLatitude;
    private double mLongitude;
    private TextView mJieguo;
    private AutoCompleteTextView mEditAddress;
    private List<String> stringlist = new ArrayList<>();
    private List<String> stringlist2 = new ArrayList<>();
    private SuggestionSearch mSuggestionSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Initail Map Information
        SDKInitializer.setAgreePrivacy(this.getApplicationContext(),true);
        LocationClient.setAgreePrivacy(true);
        SDKInitializer.initialize(this.getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.home) ;
        setInit();
        // initMap();
    }

    public void setInit() {
        mMapView = findViewById(R.id.mapview);
        mBaiduMap = mMapView.getMap();
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(listener);
        mMapView.showZoomControls(false);
        mSaLocation = findViewById(R.id.sa_location);
        mSaLocation.setOnClickListener(this);
        mEditAddress = findViewById(R.id.editaddress);
        mJieguo = findViewById(R.id.jieguo);
    }

    /**
     * 监听输入框的输入
     */
    public void setEdit(final String city) {
        //点击就自动提示
        mEditAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditAddress.showDropDown();
            }
        });

        mEditAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                        .keyword(mEditAddress.getText().toString())
                        .city(city));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    OnGetSuggestionResultListener listener = new OnGetSuggestionResultListener() {
        public void onGetSuggestionResult(SuggestionResult res) {
            if (res == null || res.getAllSuggestions() == null) {//未找到相关结果
                return;
            } else {//获取在线建议检索结果
                List<SuggestionResult.SuggestionInfo> resl = res.getAllSuggestions();

                stringlist.clear();
                stringlist2.clear();
                for (int i = 0; i < resl.size(); i++) {
                    stringlist.add(resl.get(i).key);
                    stringlist2.add(resl.get(i).city+resl.get(i).district+resl.get(i).key);
                    latLng = resl.get(i).pt;
                }
                mJieguo.setText("" + resl);
                AutoEditTextAdapter adapter = new AutoEditTextAdapter(stringlist,stringlist2, Map.this);
                mEditAddress.setAdapter(adapter);
                adapter.setOnItemClickListener(new AutoEditTextAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(int position) {
                        Toast.makeText(Map.this,""+position,Toast.LENGTH_SHORT).show();
                        mEditAddress.setText(stringlist.get(position));
                        mEditAddress.dismissDropDown();
                    }

                    @Override
                    public void onLongClick(int position) {

                    }
                });
            }
        }
    };

    /**
     * 定位
     */
    private void initMap() throws Exception {
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);//默认显示普通地图
        mBaiduMap.setMyLocationEnabled(true);// 开启定位图层
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        initLocation();//配置定位SDK参数
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        mLocationClient.start();//开启定位
        mLocationClient.requestLocation();//图片点击事件，回到定位点
    }

    /**
     * 配置定位SDK参数
     */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");//可选，coorType - 取值有3个： 返回国测局经纬度坐标系：gcj02 返回百度墨卡托坐标系 ：bd09 返回百度经纬度坐标系 ：bd09ll
        Log.e("获取地址信息设置", option.getAddrType());//获取地址信息设置
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true); // 是否打开gps进行定位
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setScanSpan(1000);//可选，设置的扫描间隔，单位是毫秒，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        Log.e("获取设置的Prod字段值", option.getProdName());
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setNeedDeviceDirect(true);//在网络定位时，是否需要设备方向- true:需要 ; false:不需要。默认为false
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation
        // .getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sa_location:
                //把定位点再次显现出来
                MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
                mBaiduMap.animateMapStatus(mapStatusUpdate);
                break;
        }
    }

    //实现BDLocationListener接口,BDLocationListener为结果监听接口，异步获取定位结果
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(final BDLocation location) {
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            // 构造定位数据
            mLatitude = location.getLatitude();
            mLongitude = location.getLongitude();
            locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    .direction(100)// 此处设置开发者获取到的方向信息，顺时针0-360
                    .latitude(mLatitude)
                    .longitude(mLongitude).build();
            mBaiduMap.setMyLocationData(locData);// 设置定位数据

            setEdit(location.getCity());

            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(latLng).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    Toast.makeText(Map.this, location.getAddrStr(), Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    Toast.makeText(Map.this, location.getAddrStr(), Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    Toast.makeText(Map.this, location.getAddrStr(), Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeServerError) {//服务器错误
                    Toast.makeText(Map.this, "服务器错误，请检查", Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {//网络错误
                    Toast.makeText(Map.this, "网络错误，请检查", Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {//手机模式错误
                    Toast.makeText(Map.this, "手机模式错误，请检查是否飞行", Toast.LENGTH_SHORT).show();
                }
            }
        }
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
