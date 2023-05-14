package com.example.ce.ui.home.fragment;

import static android.content.ContentValues.TAG;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.google.firebase.firestore.*;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.room.Query;

import android.widget.DatePicker;
import android.widget.Toast;

import com.example.ce.R;
import com.example.ce.databinding.HomeFragmentBinding;
import com.example.ce.ui.Database.DAO.OrderDAO;
import com.example.ce.ui.Database.viewmodel.OrderViewModel;
import com.example.ce.ui.dashboard_courier.DashboardViewModel;
import com.example.ce.ui.home.viewmodel.SharedViewModel;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;


public class HomeFragment extends Fragment {

    private SharedViewModel model;
    private HomeFragmentBinding addBinding;
    private OrderDAO orderDAO;
    public HomeFragment(){}


    public String timestampFrom;
    public Long timestampLongFrom;

    public String timestampTo;
    public Long timestampLongTo;

    PieChart OrderTypeChart;

    Date date1,date2;
    int Food=10, Digital=20, Liquid=15, Wearing=15, Book=20, File=10, Others=10;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        addBinding = HomeFragmentBinding.inflate(inflater, container, false);
        View view = addBinding.getRoot();
        Button btnConfirm = view.findViewById(R.id.btnconfirm);
        DatePicker fromPicker = view.findViewById(R.id.startdate);
        DatePicker toPicker = view.findViewById(R.id.enddate);
        OrderTypeChart = addBinding.orderTypeChart;
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (timestampFrom.compareTo(timestampTo) > 0) {
                    String msg = "Dates are not properly set.";
                    toastMsg(msg);
                } else {
                    db.collection("Orders")
                            .whereGreaterThan("StartDate", timestampFrom)
                            .whereEqualTo("Type","Food")
                                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful())
                                        Food = task.getResult().size();
                                    else
                                        Log.d(TAG, "Error getting type counts: ", task.getException());
                                }
                            });
                    db.collection("Orders")
                            .whereGreaterThan("StartDate", timestampFrom)
                            .whereEqualTo("Type","Digital")
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful())
                                        Digital = task.getResult().size();
                                    else
                                        Log.d(TAG, "Error getting type counts: ", task.getException());
                                }
                            });
                    db.collection("Orders")
                            .whereGreaterThan("StartDate", timestampFrom.toString())
                            .whereEqualTo("Type","Liquid")
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful())
                                        Liquid = task.getResult().size();
                                    else
                                        Log.d(TAG, "Error getting type counts: ", task.getException());
                                }
                            });
                    db.collection("Orders")
                            .whereGreaterThan("StartDate", timestampFrom.toString())
                            .whereEqualTo("Type","Wearing")
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful())
                                        Wearing = task.getResult().size();
                                    else
                                        Log.d(TAG, "Error getting type counts: ", task.getException());
                                }
                            });
                    db.collection("Orders")
                            .whereGreaterThan("StartDate", timestampFrom.toString())
                            .whereEqualTo("Type","Book")
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful())
                                        Book = task.getResult().size();
                                    else
                                        Log.d(TAG, "Error getting type counts: ", task.getException());
                                }
                            });
                    db.collection("Orders")
                            .whereGreaterThan("StartDate", timestampFrom.toString())
                            .whereEqualTo("Type","File")
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful())
                                        File = task.getResult().size();
                                    else
                                        Log.d(TAG, "Error getting type counts: ", task.getException());
                                }
                            });
                    db.collection("Orders")
                            .whereGreaterThan("StartDate", timestampFrom.toString())
                            .whereEqualTo("Type","Others")
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful())
                                        Others = task.getResult().size();
                                    else
                                        Log.d(TAG, "Error getting type counts: ", task.getException());
                                }
                            });
                    TypeChartBuild();
                }
            }
        });

        fromPicker.init(2023, 04, 02, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                Date date = calendar.getTime();
                // Do something with the date
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                timestampFrom = sdf.format(date);
                String StringDate = timestampFrom + " 12:00:00";
                SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    date1 = format.parse(StringDate);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                timestampLongFrom =date1.getTime();

            }
        });

        toPicker.init(2023, 05, 12, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                Date date = calendar.getTime();
                // Do something with the date
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                timestampTo = sdf.format(date);
                String StringDate = timestampTo + " 12:00:00";
                SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    date2 = format.parse(StringDate);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                timestampLongTo=date2.getTime();
            }
        });




        TypeChartBuild();
        return view;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        addBinding = null;
    }

    private void TypeChartBuild() {
        OrderTypeChart.setUsePercentValues(true); //设置为显示百分比
        OrderTypeChart.setDescription(" ");//设置描述
        OrderTypeChart.setDescriptionTextSize(20f);
        // pieChart1.setExtraOffsets(5, 5, 5, 5);//设置饼状图距离上下左右的偏移量
        OrderTypeChart.setDrawCenterText(true); //设置可以绘制中间的文字
        OrderTypeChart.setCenterTextColor(Color.BLACK); //中间的文本颜色
        OrderTypeChart.setCenterTextSize(18);  //设置中间文本文字的大小
        OrderTypeChart.setDrawHoleEnabled(true); //绘制中间的圆形
        OrderTypeChart.setHoleColor(Color.WHITE);//饼状图中间的圆的绘制颜色
        OrderTypeChart.setHoleRadius(40f);//饼状图中间的圆的半径大小
        OrderTypeChart.setTransparentCircleColor(Color.BLACK);//设置圆环的颜色
        OrderTypeChart.setTransparentCircleAlpha(100);//设置圆环的透明度[0,255]
        OrderTypeChart.setTransparentCircleRadius(40f);//设置圆环的半径值
        OrderTypeChart.setRotationEnabled(false);//设置饼状图是否可以旋转(默认为true)
        OrderTypeChart.setRotationAngle(10);//设置饼状图旋转的角度

        Legend l = OrderTypeChart.getLegend(); //设置比例图
        l.setMaxSizePercent(100);
        l.setTextSize(12);
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_CENTER);//设置每个tab的显示位置（这个位置是指下图右边小方框部分的位置 ）
        l.setXEntrySpace(10f);
        l.setYEntrySpace(5f);//设置tab之间Y轴方向上的空白间距值
        l.setYOffset(0f);

        //饼状图上字体的设置
        OrderTypeChart.setDrawEntryLabels(false);//设置是否绘制Label
        // pieChart1.setEntryLabelColor(Color.BLACK);//设置绘制Label的颜色
        OrderTypeChart.setEntryLabelTextSize(23f);//设置绘制Label的字体大小

        // pieChart1.animateY(100, Easing.EasingOption.EaseInQuad);//设置Y轴上的绘制动画

        //设置数据百分比和描述
        ArrayList<PieEntry> pieEntries = new ArrayList<PieEntry>();
        pieEntries.add(new PieEntry(Food, "Food"));
        pieEntries.add(new PieEntry(Digital, "Digital"));
        pieEntries.add(new PieEntry(Liquid, "Liquid"));
        pieEntries.add(new PieEntry(Wearing, "Wearing"));
        pieEntries.add(new PieEntry(Book, "Book"));
        pieEntries.add(new PieEntry(File, "File"));
        pieEntries.add(new PieEntry(Others, "Others"));

        String centerText = "Item Type Ratio";
        OrderTypeChart.setCenterText(centerText);//设置圆环中间的文字
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        ArrayList<Integer> colors = new ArrayList<>();

        // 饼图颜色
        colors.add(Color.rgb(254, 67, 101));
        colors.add(Color.rgb(252, 157, 154));
        colors.add(Color.rgb(249, 205, 173));
        colors.add(Color.rgb(200, 200, 169));
        colors.add(Color.rgb(131, 175, 155));
        colors.add(Color.rgb(230, 155, 3));
        colors.add(Color.rgb(175, 215, 237));
        pieDataSet.setColors(colors);

        pieDataSet.setSliceSpace(0f);//设置选中的Tab离两边的距离
        pieDataSet.setSelectionShift(5f);//设置选中的tab的多出来的
        PieData pieData = new PieData();
        pieData.setDataSet(pieDataSet);

        //各个饼状图所占比例数字的设置
        pieData.setValueFormatter(new PercentFormatter());//设置%
        pieData.setValueTextSize(18f);
        pieData.setValueTextColor(Color.BLUE);

        OrderTypeChart.setData(pieData);
        OrderTypeChart.highlightValues(null);
        OrderTypeChart.invalidate();
    }
    public void toastMsg(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}