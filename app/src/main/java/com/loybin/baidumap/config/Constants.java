package com.loybin.baidumap.config;

import android.Manifest;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/04/21 上午11:14
 * 描   述: 常量配置
 */
public class Constants {

    //测试服务器请求地址
    public static final String TEXTHOST = "https://kidwatch01.hojy.com/hgts/api/";

    //正式服务器请求地址
    public static final String HOST = "https://kidwatch.hojy.com/hgts/api/";

    //移动招标版本请求地址
    public static final String CMCCHOST = "https://kidwatch01.hojy.com/hgts-cmcc/api/";


    public static final String TEXT = " https://kidwatch-manager.hojy.com/hgts/api/";


    // 网络请求
    /**
     * 1.01 注册（register）
     **/
    public static final String REGISTER = "registerAcount";

    /**
     * 1.02 账号密码登录（login）
     **/
    public static final String ACOUNTLOGIN = "acountLogin";

    /**
     * 1.03 短信验证码sendCode
     **/
    public static final String SEND_CODE = "sendCheckCode";

    /**
     * 1.04 忘记密码resetPassword
     **/
    public static final String RESETPASSWORD = "editPassword";

    /**
     * 1.05 获取绑定设备列表
     **/
    public static final String GETACOUNTDEIVCELIST = "getAcountDeivceList";

    /**
     * 1.06 APP扫描二维码，流程状态查询
     **/
    public static final String CHECKDEVICEBINDSTATUS = "checkDeviceBindStatus";

    /**
     * 1.07 首次绑定设备
     **/
    public static final String ACOUNTBINDIMEIFIRST = "acountBindImeiFirst";

    /**
     * 1.08 上传附件
     **/
    public static final String UPLOAD = "upload";

    /**
     * 1.09 其他用户绑定设备
     **/
    public static final String ACOUNTBINDIMEI = "acountBindImei";

    /**
     * 1.10 根据设备id查询设备的电子围栏信息
     **/
    public static final String QUERYFENCEINFOBYDEVICEID = "queryFenceInfoByDeviceId";

    /**
     * 1.11 新增|修改电子围栏
     **/
    public static final String INSERTORUPDATEFENCE = "insertOrUpdateFence";

    /**
     * 1.12 删除电子围栏
     **/
    public static final String DELETEFENCEBYID = "deleteFenceById";

    /**
     * 1.13 获取设备通信群组成员
     **/
    public static final String GETGROUPMEMBERLIST = "getGroupMemberList";

    /**
     * 1.14 解除绑定单个用户【非管理员】
     **/
    public static final String DISBANDONEACOUNT = "disBandOneAcount";

    /**
     * 1.15 管理员解除自己，需要转移管理员权限给其他绑定设备的账户】
     **/
    public static final String DISBANDACOUNTANDCHANGEADMIN = "disBandAcountAndChangeAdmin";

    /**
     * 1.16 解除所有人
     **/
    public static final String DISBANDALLACOUNT = "disBandAllAcount";

    /**
     * 1.17 验证手机号码有没有注册
     **/
    public static final String CHECKACOUNT = "checkAcount";

    /**
     * 1.18 修改账户信息
     **/
    public static final String UPDATEACOUNTINFO = "updateAcountInfo";

    /**
     * 1.19 获取历史轨迹
     **/
    public static final String GETHISTORYLOCATIONS = "getHistoryLocations";

    /**
     * 1.20 发送透传指令
     **/
    public static final String APPSENDCMD = "appSendCMD";

    /**
     * 1.21 管理员修改手表绑定的账户信息，并通知手表
     **/
    public static final String UPDATEACOUNTINFOANDSENDCMDTOIMEI = "updateAcountInfoAndSendCMDToImei";

    /**
     * 1.22 检查版本是否为最新版本
     **/
    public static final String CHECKVERSION = "checkVersion";

    /**
     * 1.23 根据设备id查询设备版本信息
     **/
    public static final String QUERYDEVICEBYDEVICEID = "queryDeviceByDeviceId";

    /**
     * 1.24 获取设备通讯群组成员信息,包括手表
     **/
    public static final String GETGROUPMEMBERLISTALL = "getGroupMemberListAll";

    /**
     * 1.25 设置/更新设备定时开关机接口
     **/
    public static final String UPDATEDEVICEPOWER = "updateDevicePower";

    /**
     * 1.26 通过设备id查询备定时开关机接口
     **/
    public static final String QUERYDEVICEPOWERBYDEVICEID = "queryDevicePowerByDeviceId";

    /**
     * 1.27 通过设备id查询备的电子围栏开启状态、拒接陌生来电开启状态、设置定时开关机开启状态、上课禁用开启状态
     **/
    public static final String QUERYDEVICESTATEBYDEVICEID = "queryDeviceStateByDeviceId";

    /**
     * 1.28 新增/更新设备开关接口
     **/
    public static final String INSERTORUPDATEDEVICEATTR = "insertOrUpdateDeviceAttr";

    /**
     * 1.29 通过设备id查询设备上课禁用时间接口
     **/
    public static final String QUERYFORBIDDENTIMEBYDEVICEID = "queryForbiddenTimeByDeviceId";

    /**
     * 1.30 设置/更新设备上课禁用时间接口
     **/
    public static final String INSERTORUPDATEFORBIDDENTIME = "insertOrUpdateForbiddenTime";

    /**
     * 1.31 写入/更新意见反馈
     **/
    public static final String INSERTORUPDATEOPINIONS = "insertOrUpdateOpinions";

    /**
     * 1.32 更新设备定时开关机开启关闭状态接口
     **/
    public static final String UPDATEDEVICEPOWERSTATE = "updateDevicePowerState";

    /**
     * 1.34 申请绑定设备
     **/
    public static final String APPLYBINDDEVICE = "applyBindDevice";

    /**
     * 1.35 同意|拒绝绑定申请
     **/
    public static final String REPLAYAPPLYBINDDEVICE = "replayApplyBindDevice";

    /**
     * 1.36 获取设备联系人列表【APP】
     **/
    public static final String QUERYDEVICECONTRACTSLISTFORAPP = "queryDeviceContractsListForApp";

    /**
     * 1.37 添加联系人
     **/
    public static final String ADDDEVICECONTRACTS = "addDeviceContracts";

    /**
     * 1.38 删除联系人
     **/
    public static final String DELDEVICECONTRACTS = "delDeviceContracts";

    /**
     * 1.39 编辑联系人
     **/
    public static final String EDITDEVICECONTRACTS = "editDeviceContracts";

    /**
     * 1.40 【普通联系人】主动解除绑定
     **/
    public static final String DISBANDDEVICECONTRACTS = "disBandDeviceContracts";

    /**
     * 1.41 管理员转移权限并解除绑定
     **/
    public static final String CHANGEADMINBANDDEVICECONTRACTS = "changeAdminBandDeviceContracts";

    /**
     * 1.42 管理员解除绑定所有联系人
     **/
    public static final String DISBANDALLDEVICECONTRACTS = "disBandAllDeviceContracts";

    /**
     * 1.43 获取设备短信指令模板
     **/
    public static final String GETSMSCMD = "getSmsCmd";

    /**
     * 1.44 通过上课禁用id更新上课禁用的总状态
     **/
    public static final String UPDATEFORBIDDENTIMESTATEBYID = "updateForbiddenTimeStateById";

    /**
     * 1.45 根据上课禁用id删除上课禁用接口
     **/
    public static final String DELETEFORBIDDENTIMEBYID = "deleteForbiddenTimeById";

    /**
     * 1.46 APP发送喜马拉雅讲故事的信息到手表端
     **/
    public static final String APPSENDSTORYINFOTOIMEI = "appSendStoryInfoToImei";

    /**
     * 1.47 app查询手表步数排行
     **/
    public static final String QUERYDEVICESTEPSRANKING = "queryDeviceStepsRanking";

    /**
     * 1.48 根据设备id新增/修改设备链接的wifi
     **/
    public static final String INSERTORUPDATEDEVICEWIFI = "insertOrUpdateDeviceWifi";

    /**
     * 1.49 根据设备id查询设备链接的wifi
     **/
    public static final String QUERYDEVICEWIFIBYDEVICEID = "queryDeviceWifiByDeviceId";

    /**
     * 1.49 设备上报链接WiFi状态
     **/
    public static final String UPDATEDEVICELINKWIFIBYDEVICEID = "updateDeviceLinkWifiByDeviceId";

    /**
     * 1.50 根据设备imei查询该设备已订阅的喜马拉雅故事信息
     **/
    public static final String QUERYHIMALAYANSTORYBYIMEI = "queryHimalayanStoryByImei";

    /**
     * 1.51 根据设备imei和喜马拉雅故事id 删除该设备已订阅喜马拉雅故事
     **/
    public static final String DELETEBYIMEIANDSTORYID = "deleteByImeiAndStoryId";

    /**
     * 1.52 获取历史消息分类和最新一条消息
     **/
    public static final String GETTYPESANDLASTMESSAGE = "getTypesAndLastMessage";

    /**
     * 1.53 删除通知中心消息
     **/
    public static final String DELETEBATCHMESSAGES = "deleteBatchMessages";

    /**
     * 1.54 获取某一分类的所有历史消息 通知中心
     **/
    public static final String GETMESSAGESBYTYPE = "getMessagesByType";

    /**
     * 1.55 消息状态修改
     **/
    public static final String UPDATEMESSAGESTOREAD = "updateMessagesToRead";

    /**
     * 1.56 根据消息主类型删除消息
     **/
    public static final String DELETEMESSAGESBYMSGTYPE = "deleteMessagesByMsgType";

    /**
     * 1.57 根据设备imei查询设备的好友列表信息接口
     **/
    public static final String QUERYFRIENDSBYIMEI = "queryFriendsByImei";

    /**
     * 1.58 根据设备账户名和好友账户名删除设备好友
     **/
    public static final String DELDEVICEFRIENDBYACOUNTNAME = "delDeviceFriendByAcountName";

    /**
     * 1.59 被邀请人，回复接收/拒绝
     **/
    public static final String REPLYBANDDEVICEREQUEST = "replyBandDeviceRequest";

    /**
     * 1.60 邀请好友绑定设备
     **/
    public static final String BANDDEVICEREQUEST = "bandDeviceRequest";

    /**
     * 1.61 获取设备闹钟信息列表
     **/
    public static final String QUERYALARMCLOCKBYDEVICEID = "queryAlarmClockByDeviceId";

    /**
     * 1.62 删除闹钟
     **/
    public static final String DELETEALARMCLOCK = "deleteAlarmClock";

    /**
     * 1.63 添加闹钟|修改闹钟
     **/
    public static final String INSERTORUPDATEALARMCLOCK = "insertOrUpdateAlarmClock";

    /**
     * 1.64 设备IOT信息查询
     **/
    public static final String QUERYIOTDEVICE = "queryIotDevice";

    /**
     * 1.65 验证通信令牌是否有效
     **/
    public static final String VERIFICATIONTOKEN = "verificationToken";


    public static final Integer LoginType_User = 0;//用户登录
    public static final Integer LoginType_Device = 1;//设备登录

    public static final int State80000 = 80000;//成功
    public static final int State80001 = 80001;//成功
    public static final int COMMAND10010 = 10010;//管理员移交管理员权限，并解绑自己，通知新管理员指令
    public static final int COMMAND10009 = 10009;//管理员解绑设备绑定成员通知被解绑人指令
    public static final int COMMAND10011 = 10011;//管理员给设备添加绑定成员通知
    public static final int COMMAND10012 = 10012;//单向聆听指令
    public static final int COMMAND10006 = 10006;//立即定位指令
    public static final int COMMAND10002 = 10002;//强制手表关机
    public static final int COMMAND100023 = 10023;//强制手表关机
    public static final int COMMAND10022 = 10022;//查询话费指令
    public static final int COMMAND10027 = 10027;//app接收设备上报的wifi数据指令;
    public static final int COMMAND10021 = 10021;//app接收话费查询指令
    public static final int COMMAND10036 = 10036;//设备环信已注册成功指令
    public static final int COMMAND10037 = 10037;//重新登录指令
    public static final int COMMAND10039 = 10039;//设备信息更新指令,客户端重新拉取数据

    public static final String COMMANDCXLL = "CXLL"; //查询流量

    //Error 码
    public static final int ERROR92001 = 92001;//号码短信发送次数已达最大上限

    public static final String message = "单向聆听";//单向聆听
    public static final String message10002 = "关机设备";//关机设备


    public static String MAPTYPE = "baidu";
    //商用帮助文档
    public static final String HELPURL = "https://kidwatch.hojy.com/hgts/userHelp.html";

    public static final String REGISTERURL = "https://kidwatch.hojy.com/hgts/serviceAgreement.html";


    public static final String HELPURLTEXT = "https://kidwatch01.hojy.com/hgts/userHelp.html";
    public static final String REGISTERURLTEXT = "https://kidwatch01.hojy.com/hgts/serviceAgreement.html";

    public static final String TEXT_KEY = "1115170410178158#kidwatchtest";
    public static final String HUANXIN_KEY = "1115170410178158#kidwatch";

    public static final String LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String APP_ID = "wx31a86be60e7515d3";




    /** 从控制台获取productKey、deviceName、deviceSecret信息*/
    public static String productKey = "";
    public static String deviceName = "";
    public static String deviceSecret = "";
    /** 用于测试的topic */
    public static String subTopic = "/"+productKey+"/"+deviceName+"/get"; //下行
    public static String pubTopic = "/"+productKey+"/"+deviceName+"/pub";//上行

    public static  String serverUri = "ssl://"+productKey+".iot-as-mqtt.cn-shanghai.aliyuncs.com:1883";

    public static String t = System.currentTimeMillis()+"";
    public static String mqttclientId = deviceName + "|securemode=2,signmethod=hmacsha1,timestamp="+t+"|";



}
