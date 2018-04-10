package com.loybin.baidumap.http;

import com.loybin.baidumap.config.Constants;
import com.loybin.baidumap.model.MessageListModel;
import com.loybin.baidumap.model.ResponseInfoModel;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/04/21 上午11:14
 * 描   述: 所有网络请求服务 post
 */
public interface WatchService {

    /**
     * 发送验证码
     *
     * @param fields 请求参数
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.SEND_CODE)
    Call<ResponseInfoModel> sendCheckCode(@FieldMap Map<String, String> fields);


    /**
     * 注册账号
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.REGISTER)
    Call<ResponseInfoModel> register(@FieldMap Map<String, String> fields);


    /**
     * 验证手机号码有没有注册
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.CHECKACOUNT)
    Call<ResponseInfoModel> checkAcount(@FieldMap Map<String, String> fields);


    /**
     * 登入
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.ACOUNTLOGIN)
    Call<ResponseInfoModel> login(@FieldMap Map<String, String> fields);


    /**
     * 忘记密码
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.RESETPASSWORD)
    Call<ResponseInfoModel> edit(@FieldMap Map<String, String> fields);


    /**
     * 获取绑定设备列表
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.GETACOUNTDEIVCELIST)
    Call<ResponseInfoModel> getAcountDeivceList(@FieldMap Map<String, Object> fields);


    /**
     * APP扫描二维码，流程状态查询
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.CHECKDEVICEBINDSTATUS)
    Call<ResponseInfoModel> checkDeviceBindStatus(@FieldMap Map<String, Object> fields);


    /**
     * 首次绑定设备
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.ACOUNTBINDIMEIFIRST)
    Call<ResponseInfoModel> acountBindImeiFirst(@FieldMap Map<String, Object> fields);


    /**
     * 上传一张图片
     *
     * @return
     */
    @Multipart
    @POST(Constants.UPLOAD)
    Call<ResponseInfoModel> upload(@Query("token") String address,
                                   @Part("file\"; filename=\"avatar.jpg") RequestBody avatar);


    /**
     * 根据设备id查询设备的电子围栏信息
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.QUERYFENCEINFOBYDEVICEID)
    Call<ResponseInfoModel> queryFenceInfoByDeviceId(@FieldMap Map<String, Object> fields);


    /**
     * 新增|修改电子围栏
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.INSERTORUPDATEFENCE)
    Call<ResponseInfoModel> insertOrUpdateFence(@FieldMap Map<String, Object> fields);


    /**
     * 删除电子围栏
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.DELETEFENCEBYID)
    Call<ResponseInfoModel> deleteFenceById(@FieldMap Map<String, Object> fields);


    /**
     * 添加设备的绑定账户
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.ACOUNTBINDIMEI)
    Call<ResponseInfoModel> acountBindImei(@FieldMap Map<String, Object> fields);


    /**
     * 获取设备通信群组成员
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.GETGROUPMEMBERLIST)
    Call<ResponseInfoModel> getGroupMemberList(@FieldMap Map<String, Object> fields);


    /**
     * 解除绑定单个用户【非管理员】
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.DISBANDONEACOUNT)
    Call<ResponseInfoModel> disBandOneAcount(@FieldMap Map<String, Object> fields);


    /**
     * 管理员解除自己，需要转移管理员权限给其他绑定设备的账户
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.DISBANDACOUNTANDCHANGEADMIN)
    Call<ResponseInfoModel> disBandAcountAndChangeAdmin(@FieldMap Map<String, Object> fields);


    /**
     * 解除所有人
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.DISBANDALLACOUNT)
    Call<ResponseInfoModel> disBandAllAcount(@FieldMap Map<String, Object> fields);


    /**
     * 修改账户信息
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.UPDATEACOUNTINFO)
    Call<ResponseInfoModel> updateAcountInfo(@FieldMap Map<String, Object> fields);


    /**
     * 获取历史轨迹
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.GETHISTORYLOCATIONS)
    Call<ResponseInfoModel> getHistoryLocations(@FieldMap Map<String, Object> fields);


    /**
     * 发送透传指令
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.APPSENDCMD)
    Call<ResponseInfoModel> appSendCMD(@FieldMap Map<String, Object> fields);


    /**
     * 管理员修改手表绑定的账户信息，并通知手表
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.UPDATEACOUNTINFOANDSENDCMDTOIMEI)
    Call<ResponseInfoModel> updateAcountInfoAndSendCMDToImei(@FieldMap Map<String, Object> fields);


    /**
     * 检查版本是否为最新版本
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.CHECKVERSION)
    Call<ResponseInfoModel> checkVersion(@FieldMap Map<String, Object> fields);


    /**
     * 根据设备id查询设备版本信息
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.QUERYDEVICEBYDEVICEID)
    Call<ResponseInfoModel> queryDeviceByDeviceId(@FieldMap Map<String, Object> fields);


    /**
     * 获取设备通讯群组成员信息,包括手表
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.GETGROUPMEMBERLISTALL)
    Call<ResponseInfoModel> getGroupMemberListAll(@FieldMap Map<String, Object> fields);


    /**
     * 设置/更新设备定时开关机接口
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.UPDATEDEVICEPOWER)
    Call<ResponseInfoModel> updateDevicePower(@FieldMap Map<String, Object> fields);


    /**
     * 通过设备id查询备定时开关机接口
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.QUERYDEVICEPOWERBYDEVICEID)
    Call<ResponseInfoModel> queryDevicePowerByDeviceId(@FieldMap Map<String, Object> fields);


    /**
     * 通过设备id查询备的电子围栏开启状态、拒接陌生来电开启状态、设置定时开关机开启状态、上课禁用开启状态
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.QUERYDEVICESTATEBYDEVICEID)
    Call<ResponseInfoModel> queryDeviceStateByDeviceId(@FieldMap Map<String, Object> fields);


    /**
     * 新增/更新设备开关接口
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.INSERTORUPDATEDEVICEATTR)
    Call<ResponseInfoModel> insertOrUpdateDeviceAttr(@FieldMap Map<String, Object> fields);


    /**
     * 通过设备id查询设备上课禁用时间接口
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.QUERYFORBIDDENTIMEBYDEVICEID)
    Call<ResponseInfoModel> queryForbiddenTimeByDeviceId(@FieldMap Map<String, Object> fields);


    /**
     * 设置/更新设备上课禁用时间接口
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.INSERTORUPDATEFORBIDDENTIME)
    Call<ResponseInfoModel> insertOrUpdateForbiddenTime(@FieldMap Map<String, Object> fields);


    /**
     * 写入/更新意见反馈
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.INSERTORUPDATEOPINIONS)
    Call<ResponseInfoModel> insertOrUpdateOpinions(@FieldMap Map<String, Object> fields);


    /**
     * 更新设备定时开关机开启关闭状态接口
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.UPDATEDEVICEPOWERSTATE)
    Call<ResponseInfoModel> updateDevicePowerState(@FieldMap Map<String, Object> fields);


    /**
     * 申请绑定设备
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.APPLYBINDDEVICE)
    Call<ResponseInfoModel> applyBindDevice(@FieldMap Map<String, Object> fields);


    /**
     * 同意|拒绝绑定申请
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.REPLAYAPPLYBINDDEVICE)
    Call<ResponseInfoModel> replayApplyBindDevice(@FieldMap Map<String, Object> fields);


    /**
     * 获取设备联系人列表【APP】
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.QUERYDEVICECONTRACTSLISTFORAPP)
    Call<ResponseInfoModel> queryDeviceContractsListForApp(@FieldMap Map<String, Object> fields);


    /**
     * 添加联系人
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.ADDDEVICECONTRACTS)
    Call<ResponseInfoModel> addDeviceContracts(@FieldMap Map<String, Object> fields);


    /**
     * 删除联系人
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.DELDEVICECONTRACTS)
    Call<ResponseInfoModel> delDeviceContracts(@FieldMap Map<String, Object> fields);


    /**
     * 编辑联系人
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.EDITDEVICECONTRACTS)
    Call<ResponseInfoModel> editDeviceContracts(@FieldMap Map<String, Object> fields);


    /**
     * 编辑联系人
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.DISBANDDEVICECONTRACTS)
    Call<ResponseInfoModel> disBandDeviceContracts(@FieldMap Map<String, Object> fields);


    /**
     * 管理员转移权限并解除绑定
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.CHANGEADMINBANDDEVICECONTRACTS)
    Call<ResponseInfoModel> changeAdminBandDeviceContracts(@FieldMap Map<String, Object> fields);


    /**
     * 管理员解除绑定所有联系人
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.DISBANDALLDEVICECONTRACTS)
    Call<ResponseInfoModel> disBandAllDeviceContracts(@FieldMap Map<String, Object> fields);


    /**
     * 获取设备短信指令模板
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.GETSMSCMD)
    Call<ResponseInfoModel> getSmsCmd(@FieldMap Map<String, Object> fields);


    /**
     * 通过上课禁用id更新上课禁用的总状态
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.UPDATEFORBIDDENTIMESTATEBYID)
    Call<ResponseInfoModel> updateForbiddenTimeStateById(@FieldMap Map<String, Object> fields);


    /**
     * 通过id删除上课禁用
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.DELETEFORBIDDENTIMEBYID)
    Call<ResponseInfoModel> deleteForbiddenTimeById(@FieldMap Map<String, Object> fields);


    /**
     * APP发送喜马拉雅讲故事的信息到手表端
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.APPSENDSTORYINFOTOIMEI)
    Call<ResponseInfoModel> appSendStoryInfoToImei(@FieldMap Map<String, Object> fields);


    /**
     * app查询手表步数排行
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.QUERYDEVICESTEPSRANKING)
    Call<ResponseInfoModel> queryDeviceStepsRanking(@FieldMap Map<String, Object> fields);


    /**
     * 根据设备id新增/修改设备链接的wifi
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.INSERTORUPDATEDEVICEWIFI)
    Call<ResponseInfoModel> insertOrUpdateDeviceWifi(@FieldMap Map<String, Object> fields);


    /**
     * 根据设备id查询设备链接的wifi
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.QUERYDEVICEWIFIBYDEVICEID)
    Call<ResponseInfoModel> queryDeviceWifiByDeviceId(@FieldMap Map<String, Object> fields);


    /**
     * 设备上报链接WiFi状态
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.UPDATEDEVICELINKWIFIBYDEVICEID)
    Call<ResponseInfoModel> updateDeviceLinkWifiByDeviceId(@FieldMap Map<String, Object> fields);


    /**
     * 根据设备imei查询该设备已订阅的喜马拉雅故事信息
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.QUERYHIMALAYANSTORYBYIMEI)
    Call<ResponseInfoModel> queryHimalayanStoryByImei(@FieldMap Map<String, Object> fields);


    /**
     * 根据设备imei和喜马拉雅故事id 删除该设备已订阅喜马拉雅故事
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.DELETEBYIMEIANDSTORYID)
    Call<ResponseInfoModel> deleteByImeiAndStoryId(@FieldMap Map<String, Object> fields);


    /**
     * 获取历史消息分类和最新一条消息
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.GETTYPESANDLASTMESSAGE)
    Call<ResponseInfoModel> getTypesAndLastMessage(@FieldMap Map<String, Object> fields);


    /**
     * 删除通知中心消息
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.DELETEBATCHMESSAGES)
    Call<ResponseInfoModel> deleteBatchMessages(@FieldMap Map<String, Object> fields);


    /**
     * 获取某一分类的所有历史消息
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.GETMESSAGESBYTYPE)
    Call<MessageListModel> getMessagesByType(@FieldMap Map<String, Object> fields);


    /**
     * 消息状态修改
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.UPDATEMESSAGESTOREAD)
    Call<ResponseInfoModel> updateMessagesToRead(@FieldMap Map<String, Object> fields);


    /**
     * 消息状态修改
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.DELETEMESSAGESBYMSGTYPE)
    Call<ResponseInfoModel> deleteMessagesByMsgType(@FieldMap Map<String, Object> fields);


    /**
     * 根据设备imei查询设备的好友列表信息接口
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.QUERYFRIENDSBYIMEI)
    Call<ResponseInfoModel> queryFriendsByImei(@FieldMap Map<String, Object> fields);


    /**
     * 根据设备账户名和好友账户名删除设备好友
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.DELDEVICEFRIENDBYACOUNTNAME)
    Call<ResponseInfoModel> delDeviceFriendByAcountName(@FieldMap Map<String, Object> fields);


    /**
     * 被邀请人，回复接收/拒绝
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.REPLYBANDDEVICEREQUEST)
    Call<ResponseInfoModel> replyBandDeviceRequest(@FieldMap Map<String, Object> fields);


    /**
     * 邀请好友绑定设备
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.BANDDEVICEREQUEST)
    Call<ResponseInfoModel> bandDeviceRequest(@FieldMap Map<String, Object> fields);


    /**
     * 获取设备闹钟信息列表
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.QUERYALARMCLOCKBYDEVICEID)
    Call<ResponseInfoModel> queryAlarmClockByDeviceId(@FieldMap Map<String, Object> fields);


    /**
     * 添加闹钟|修改闹钟
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.INSERTORUPDATEALARMCLOCK)
    Call<ResponseInfoModel> insertOrUpdateAlarmClock(@FieldMap Map<String, Object> fields);


    /**
     * 删除闹钟
     *
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.DELETEALARMCLOCK)
    Call<ResponseInfoModel> deleteAlarmClock(@FieldMap Map<String, Object> fields);


    /**
     *设备IOT信息查询
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.QUERYIOTDEVICE)
    Call<ResponseInfoModel> queryIotDevice(@FieldMap Map<String, Object> fields);


    /**
     *验证Token
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.VERIFICATIONTOKEN)
    Call<ResponseInfoModel> verificationToken(@FieldMap Map<String, String> fields);


}
