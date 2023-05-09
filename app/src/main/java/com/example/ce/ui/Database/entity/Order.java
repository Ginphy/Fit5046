package com.example.ce.ui.Database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.checkerframework.checker.units.qual.C;

@Entity
public class Order {
    @PrimaryKey(autoGenerate = true)
    public int orderid;

    @ColumnInfo(name = "Itemname")
    @NonNull
    public String itemName;

    @ColumnInfo(name = "start_name")
    @NonNull
    public String start_mame;

    @ColumnInfo(name = "terminal_name")
    @NonNull
    public String terminal_mame;

    @ColumnInfo(name = "type")
    @NonNull
    public String type;

    @ColumnInfo(name = "start_date")
    @NonNull
    public String start_date;

    @ColumnInfo(name = "end_date")
    @NonNull
    public String end_date;

    @ColumnInfo(name = "start_jd")
    @NonNull
    public double start_jd;

    @ColumnInfo(name = "start_wd")
    @NonNull
    public double start_wd;

    @ColumnInfo(name = "end_jd")
    @NonNull
    public double end_jd;

    @ColumnInfo(name = "end_wd")
    @NonNull
    public double end_wd;

    @ColumnInfo(name = "status")
    @NonNull
    public boolean status;

    public int price;
    @ColumnInfo(name = "user_id")
    @NonNull
    public String user_id;

    @ColumnInfo(name = "courier_id")
    public String courier_id;

    @ColumnInfo(name = "condition")
    public int conditon;

    public Order( @NonNull int orderid, @NonNull String itemName, @NonNull String start_mame, @NonNull String terminal_mame, @NonNull String type
            ,@NonNull String start_date, @NonNull String end_date, @NonNull double start_jd, @NonNull double start_wd, @NonNull double end_jd,
                  @NonNull double end_wd, @NonNull int price, @NonNull boolean status, @NonNull String user_id, @NonNull String courier_id,
                  int conditon) {
        this.orderid = orderid;
        this.itemName = itemName;
        this.start_date = start_date;
        this.end_date = end_date;
        this.start_mame = start_mame;
        this.terminal_mame = terminal_mame;
        this.type = type;
        this.start_jd = start_jd;
        this.start_wd = start_wd;
        this.end_jd = end_jd;
        this.end_wd = end_wd;
        this.price = price;
        this.status = status;
        this.user_id = user_id;
        this.courier_id = courier_id;
    }
}