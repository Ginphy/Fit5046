package com.example.ce.ui.home;

public class Reception {
    private String reason;
    private result result;
    private static class result {
        private String date;
        private String week;
        private String statusDesc;
        private String status;
    }
    private int error_code;

    //定义 输出返回数据 的方法
    public void show() {
        System.out.println(reason);
        System.out.println(result.date);
        System.out.println(result.week);
        System.out.println(result.statusDesc);
        System.out.println(result.status);
        System.out.println(error_code);
    }
    public String getStats() {
        return result.statusDesc;
    }
}
