package com.example.ce.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.example.ce.MainActivity;
import com.example.ce.R;

import java.util.ArrayList;
import java.util.List;

public class SearchDestinationActivity extends AppCompatActivity implements Inputtips.InputtipsListener, TextWatcher, RvAdapter.OnItemClickListener {
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


        inputTips = new Inputtips(this, (InputtipsQuery) null);

        inputTips.setInputtipsListener(this);


    }


    @Override
    public void onGetInputtips(List<Tip> list, int i) {
        rvAdapter.setData(list);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        InputtipsQuery inputquery = new InputtipsQuery(String.valueOf(s), null);
        inputquery.setCityLimit(true);
        inputTips.setQuery(inputquery);

        inputTips.requestInputtipsAsyn();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
    public void onItemClick(RecyclerView parent, View view, int position, Tip data) {

        LatLonPoint point = data.getPoint();

        Poi poi = new Poi(data.getName(), new LatLng(point.getLatitude(), point.getLongitude()), data.getPoiID());
        // Set Intent Class with Position data
        Intent intent = new Intent(SearchDestinationActivity.this, MainActivity.class);
        Bundle bundle = new Bundle();
        // Use 0 stand for Start Info, 1 for End Info
        bundle.putInt("Tag",1);
        bundle.putString("Name",poi.getName());
        bundle.putDouble("Latitude",point.getLatitude());
        bundle.putDouble("Longitude",point.getLongitude());
        intent.putExtras(bundle);
        startActivity(intent);

    }
}
