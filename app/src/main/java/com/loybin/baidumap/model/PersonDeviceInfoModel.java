package com.loybin.baidumap.model;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/04/21 上午11:14
 * 描   述: 单个设备详情bean
 */
public class PersonDeviceInfoModel {

    /*设备Id*/
    public int Id = 674964;

    /*设备 imei号*/
    public String SerialNumber = "1506425180";

    /*昵称  软件方面取的名称*/
    public String NickName = "pt518";

    /*设备名物理层面不可改的*/
    public String Name = "PT518-25180";

    /*设备状态 1和2是在线其他为离线*/
    public int Status = 3;

    /*头像的url*/
    public String Avatar = "http://iwimg.gpscar.cn/UserAvatar/avatar_d_674964.jpg?t=20170406170413249";

    /*纬度 国内纠偏后的*/
    public String Latitude = "22.5485519766928";

    /*经度 国内纠偏后的*/
    public String Longitude = "113.946724770127";

    /*原始纬度*/
    public String OLat = "22.545925";

    /*原始经度*/
    public String OLng = "113.935304";

    /*设备定位时间*/
    public String DeviceUtcTime = "2017-04-07 03:48:20";

    /*最后定位时间*/
    public String LastCommunication = "";

    /*设备型号值 设备下发指令需要这个参数*/
    public String TypeValue = "1266";

    /*是否分享设备 true 为分享 false 为主控号*/
    public boolean IsShared = false;

    /*卫星*/
    public String Satellite = "0";

    /*定位方式 1. GPS 定位 2. LBS 定位 3. WIFI 定位*/
    public int PositionType = 3;

    /*信号强度*/
    public String Signal = "33";

}
