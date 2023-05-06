package com.example.ce.ui.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
//import com.amap.api.navi.AMapNavi;
//import com.amap.api.navi.AmapNaviPage;
//import com.amap.api.navi.AmapNaviParams;
//import com.amap.api.navi.AmapNaviType;
//import com.amap.api.navi.AmapPageType;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.example.ce.R;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements Inputtips.InputtipsListener, TextWatcher, RvAdapter.OnItemClickListener {
    private RvAdapter rvAdapter;
    private Inputtips inputTips;
//    private AMapNavi mAMapNavi;
    private EditText editText;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchpoi);
        EditText editText = findViewById(R.id.edit_query);
        editText.addTextChangedListener(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        rvAdapter = new RvAdapter(this, recyclerView, new ArrayList<>());
        rvAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(rvAdapter);

        //构造 Inputtips 对象，并设置监听
        inputTips = new Inputtips(this, (InputtipsQuery) null);
        //指定setInputtipsListener得到onGetInputtips结果的回调
        inputTips.setInputtipsListener(this);


    }

    //通过适配器把数据展示到recyclerView中去
    @Override
    public void onGetInputtips(List<Tip> list, int i) {
        rvAdapter.setData(list);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // 构造InputtipsQuery 对象，通过 InputtipsQuery(第一个参数为输入的数据，第二个参数城市) 设置搜索条件，null为全国范围。
        InputtipsQuery inputquery = new InputtipsQuery(String.valueOf(s), null);
        inputquery.setCityLimit(true);//限制在当前城市
        inputTips.setQuery(inputquery);
        //异步提示请求
        inputTips.requestInputtipsAsyn();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
    public void onItemClick(RecyclerView parent, View view, int position, Tip data) {
        //得到点击的坐标
        LatLonPoint point = data.getPoint();
        //得到经纬度
        Poi poi = new Poi(data.getName(), new LatLng(point.getLatitude(), point.getLongitude()), data.getPoiID());
//        //导航参数对象（起点，途径，终点，导航方式）DRIVER是导航方式（驾驶，步行...当前为驾驶）ROUTE会计算路程选择
//        AmapNaviParams params = new AmapNaviParams(null, null, poi, AmapNaviType.DRIVER, AmapPageType.ROUTE);
//        //传递上下文和导航参数
//        AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), params, null);
    }
}
