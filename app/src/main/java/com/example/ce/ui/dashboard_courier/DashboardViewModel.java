package com.example.ce.ui.dashboard_courier;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Date;

public class DashboardViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private String course_name;
    private int course_rating;
    private int orderId;
    private String articleId;
    private String StartPoint;
    private String EndPoint;
    private String datetime;
    private String articleType;
    private String userId;
    private double order_price;



    public DashboardViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }
    public DashboardViewModel(int orderId, String itemname ,String StartName, String EndName,
                              String datetime,String articleType,String userId,double order_price){
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
        this.orderId = orderId;
        this.articleId = itemname;
        this.StartPoint = StartName;
        this.EndPoint = EndName;
        this.datetime = datetime;
        this.articleType = articleType;
        this.userId = userId;
        this.order_price = order_price;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }



    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getStartPoint() {
        return StartPoint;
    }

    public void setStartPoint(String StartPoint) {
        this.StartPoint = StartPoint;
    }

    public String getEndPoint() {
        return EndPoint;
    }

    public void setEndPoint(String EndPoint) {
        this.EndPoint = EndPoint;
    }

    public String getDatetime() {
        return datetime.toString();
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getArticleType() {
        return articleType;
    }

    public void setArticleType(String articleType) {
        this.articleType = articleType;
    }

    public String getuserId() {
        return userId;
    }

    public void setuserId(String userId) {
        this.userId = userId;
    }

    public double getOrder_price() {
        return order_price;
    }

    public void setOrder_price(int order_price) {
        this.order_price = order_price;
    }



    public LiveData<String> getText() {
        return mText;
    }
}
