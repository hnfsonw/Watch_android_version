package com.loybin.baidumap.model;


import java.io.Serializable;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/04/21 上午11:14
 * 描   述: 围栏信息的bean
 */
public class GeoFenceModel implements Serializable {

    //围栏Id
    public int FenceId = 33568;
    //围栏名
    public String FenceName = "家";
    //纬度
    public double Latitude = 0;
    //经度
    public double Longitude = 0;
    //围栏半径
    public int Radius = 200;
    //围栏类型 1 为圆形，2 为矩形， 3 为多边形
    public int FenceType = 1;
    //报警类型 1 进入报警， 2 离开报警， 3 进出报警
    public int AlarmType = 3;
    //是否设备围栏
    public Boolean IsDeviceFence = false;
    //中心地址
    public String Address = "";
    //开始时间,设备围栏的定制参数，平台围栏不需要
    public String StartTime = "";
    //结束时间,设备围栏的定制参数，平台围栏不需要
    public String EndTime = "";
    //是否在使用
    public Boolean InUse = true;
    //设备id
    public int DeviceId = 674964;
    //登录返回的Token
    public String Token = "";
}
