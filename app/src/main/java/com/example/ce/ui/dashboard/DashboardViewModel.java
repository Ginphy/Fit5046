package com.example.ce.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Date;

public class DashboardViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private String course_name;
    private int course_rating;
    private String orderId;
    private String articleId;
    private String StartPoint;
    private String EndPoint;
    private Date datetime;
    private String articleType;
    private String CourierId;
    private int order_price;



    public DashboardViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }
     public DashboardViewModel(String orderId, String articleId ,String StartPoint,String EndPoint,
                               Date datetime,String articleType,String CourierId,int order_price){
         mText = new MutableLiveData<>();
         mText.setValue("This is dashboard fragment");
         this.orderId = orderId;
         this.articleId = articleId;
         this.StartPoint = StartPoint;
         this.EndPoint = EndPoint;
         this.datetime = datetime;
         this.articleType = articleType;
         this.CourierId = CourierId;
         this.order_price = order_price;
     }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
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

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public String getArticleType() {
        return articleType;
    }

    public void setArticleType(String articleType) {
        this.articleType = articleType;
    }

    public String getCourierId() {
        return CourierId;
    }

    public void setCourierId(String CourierId) {
        this.CourierId = CourierId;
    }

    public int getOrder_price() {
        return order_price;
    }

    public void setOrder_price(int order_price) {
        this.order_price = order_price;
    }



    public LiveData<String> getText() {
        return mText;
    }
}