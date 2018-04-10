package com.loybin.baidumap.model;

import java.io.Serializable;
import java.util.List;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/05/05 下午4:17
 * 描   述: 登入 注册 忘记密码的返回参数
 */
public class ResponseInfoModel implements Serializable {

    /**
     * resultMsg : 请求成功，无任何异常
     * result : {"code":"333428"}
     * time : 1493953180355
     * resultCode : 80000
     */

    private String resultMsg;
    private ResultBean result;
    private long time;
    private int resultCode;
    private int errorCode;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    @Override
    public String toString() {
        return "ResponseInfoModel{" +
                "resultMsg='" + resultMsg + '\'' +
                ", result=" + result +
                ", time=" + time +
                ", resultCode=" + resultCode +
                '}';
    }

    public static class ResultBean implements Serializable {
        /**
         * code : 333428
         */
        private String lastLoginTime;

        private String createUser;

        private String acountType;

        private String createTime;

        private String nickName;

        private String phone;

        private int gender;

        public long acountId;

        private long id;

        private String createDate;

        private String code;

        private String token;

        private String acountName;

        private String birthday;

        private String email;

        private String address;

        private String imgUrl;

        private String url;

        private boolean haseAdmin;

        private String password;

        private String desc;

        private String updateTime;

        private Boolean hasNewVesion;

        private String version;

        private int type;

        private String deviceName;

        private String hardVersion;

        private String softVersion;

        private String deviceType;

        private String sosPhone;

        private String groupId;

        private String imei;

        private String cycle;

        private String endTimeEmStr;

        private String endTimeAmStr;

        private String remark;

        private String stateEM;

        private String endTimePmStr;

        private String startTimeEmStr;

        private String stateAM;

        private String startTimeAmStr;

        private long deviceId;

        private String statePM;

        private String startTimePmStr;

        private String bootState;

        private String powerLevel;

        private long deviceSetUpId;

        private String devicePowerId;

        private int locationStyle;

        private int mobileStyle;

        private int deviceBindStatus;

        private String fenceSwitch;

        private String forbiddenTimeSwitch;

        private String strangeCallSwitch;

        private String devicePowerSwitch;

        private double accuracy;

        private double lat;

        private double lng;

        private String locationTime;

        private int logType;

        private boolean verification;

        //运营商 1-中国移动 2-中国联通 3-中国电信
        private int operator;

        private String customCmd;

        private String flowCmd;

        private String province;

        private String numberCmd;

        private String billCmd;

        private String serviceNumber;

        private long time;

        private int state;

        private String ssid;

        private deviceStepsData deviceStepsData;

        private String updatedFlag;

        private String productKey;

        private String deviceSecret;

        private List<ResultDataBean> resultData;

        private List<DeviceListBean> deviceList;

        public List<MemberListBean> memberList;

        public List<HistoryLocationsBean> historyLocations;

        private List<DevicePowerListBean> devicePowerList;

        private List<ForbiddenTimeListBean> forbiddenTimeList;

        private List<StepsRankingListBean> stepsRankingList;

        private List<storyListBean> storyList;

        private List<MessageListBean> messageList;

        private List<FriendsListBean> friendsList;

        private List<bandRequestListBean> bandRequestList;

        private List<alarmClockListBean> alarmClockList;

        public List<alarmClockListBean> getAlarmClockList() {
            return alarmClockList;
        }

        public boolean isVerification() {
            return verification;
        }

        public void setVerification(boolean verification) {
            this.verification = verification;
        }

        public void setAlarmClockList(List<alarmClockListBean> alarmClockList) {
            this.alarmClockList = alarmClockList;
        }

        public List<FriendsListBean> getFriendsList() {
            return friendsList;
        }

        public void setFriendsList(List<FriendsListBean> friendsList) {
            this.friendsList = friendsList;
        }

        public List<MessageListBean> getMessageList() {
            return messageList;
        }

        public void setMessageList(List<MessageListBean> messageList) {
            this.messageList = messageList;
        }

        public ResultBean.deviceStepsData getDeviceStepsData() {
            return deviceStepsData;
        }

        public void setDeviceStepsData(ResultBean.deviceStepsData deviceStepsData) {
            this.deviceStepsData = deviceStepsData;
        }

        public List<bandRequestListBean> getBandRequestList() {
            return bandRequestList;
        }

        public void setBandRequestList(List<bandRequestListBean> bandRequestList) {
            this.bandRequestList = bandRequestList;
        }

        public static class deviceStepsData {
            private long deviceId;
            private int steps;
            private int rownum;
            private String goalSteps;
            private double distance;
            private String recordTime;
            private double calorie;

            public long getDeviceId() {
                return deviceId;
            }

            public int getSteps() {
                return steps;
            }

            public int getRownum() {
                return rownum;
            }

            public String getGoalSteps() {
                return goalSteps;
            }

            public double getDistance() {
                return distance;
            }

            public String getRecordTime() {
                return recordTime;
            }

            public double getCalorie() {
                return calorie;
            }
        }

        public static class StepsRankingListBean implements Serializable {
            private int gender;
            private int steps;
            private String imgUrl;
            private int rownum;
            private double calorie;
            private String nickName;
            private long deviceId;
            private double distance;
            private String recordTime;

            public int getGender() {
                return gender;
            }

            public void setGender(int gender) {
                this.gender = gender;
            }

            public int getSteps() {
                return steps;
            }

            public void setSteps(int steps) {
                this.steps = steps;
            }

            public String getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
            }

            public int getRownum() {
                return rownum;
            }

            public void setRownum(int rownum) {
                this.rownum = rownum;
            }

            public double getCalorie() {
                return calorie;
            }

            public void setCalorie(double calorie) {
                this.calorie = calorie;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public long getDeviceId() {
                return deviceId;
            }

            public void setDeviceId(long deviceId) {
                this.deviceId = deviceId;
            }

            public double getDistance() {
                return distance;
            }

            public void setDistance(double distance) {
                this.distance = distance;
            }

            public String getRecordTime() {
                return recordTime;
            }

            public void setRecordTime(String recordTime) {
                this.recordTime = recordTime;
            }
        }

        public static class ForbiddenTimeListBean implements Serializable {
            private long acountId;
            private String createTime;
            private String cycle;
            private String desc;
            private String name;
            private long deviceId;
            private String endTimeAM;
            private String endTimeAmStr;
            private String endTimeEM;
            private String endTimeEmStr;
            private String endTimePM;
            private String endTimePmStr;
            private long id;
            private String remark;
            private String startTimeAM;
            private String startTimeAmStr;
            private String startTimeEM;
            private String startTimeEmStr;
            private String startTimePM;
            private String startTimePmStr;
            private String state;
            private String stateAM;
            private String stateEM;
            private String statePM;


            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public long getAcountId() {
                return acountId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
            }

            public void setAcountId(long acountId) {
                this.acountId = acountId;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getCycle() {
                return cycle;
            }

            public void setCycle(String cycle) {
                this.cycle = cycle;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public long getDeviceId() {
                return deviceId;
            }

            public void setDeviceId(long deviceId) {
                this.deviceId = deviceId;
            }

            public String getEndTimeAM() {
                return endTimeAM;
            }

            public void setEndTimeAM(String endTimeAM) {
                this.endTimeAM = endTimeAM;
            }

            public String getEndTimeAmStr() {
                return endTimeAmStr;
            }

            public void setEndTimeAmStr(String endTimeAmStr) {
                this.endTimeAmStr = endTimeAmStr;
            }

            public String getEndTimeEM() {
                return endTimeEM;
            }

            public void setEndTimeEM(String endTimeEM) {
                this.endTimeEM = endTimeEM;
            }

            public String getEndTimePM() {
                return endTimePM;
            }

            public void setEndTimePM(String endTimePM) {
                this.endTimePM = endTimePM;
            }

            public String getEndTimeEmStr() {
                return endTimeEmStr;
            }

            public void setEndTimeEmStr(String endTimeEmStr) {
                this.endTimeEmStr = endTimeEmStr;
            }

            public String getEndTimePmStr() {
                return endTimePmStr;
            }

            public void setEndTimePmStr(String endTimePmStr) {
                this.endTimePmStr = endTimePmStr;
            }


            public String getStartTimeAM() {
                return startTimeAM;
            }

            public void setStartTimeAM(String startTimeAM) {
                this.startTimeAM = startTimeAM;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getStartTimeAmStr() {
                return startTimeAmStr;
            }

            public void setStartTimeAmStr(String startTimeAmStr) {
                this.startTimeAmStr = startTimeAmStr;
            }

            public String getStartTimeEM() {
                return startTimeEM;
            }

            public void setStartTimeEM(String startTimeEM) {
                this.startTimeEM = startTimeEM;
            }

            public String getStartTimeEmStr() {
                return startTimeEmStr;
            }

            public void setStartTimeEmStr(String startTimeEmStr) {
                this.startTimeEmStr = startTimeEmStr;
            }

            public String getStartTimePM() {
                return startTimePM;
            }

            public void setStartTimePM(String startTimePM) {
                this.startTimePM = startTimePM;
            }

            public String getStartTimePmStr() {
                return startTimePmStr;
            }

            public void setStartTimePmStr(String startTimePmStr) {
                this.startTimePmStr = startTimePmStr;
            }

            public String getStateAM() {
                return stateAM;
            }

            public void setStateAM(String stateAM) {
                this.stateAM = stateAM;
            }

            public String getStateEM() {
                return stateEM;
            }

            public void setStateEM(String stateEM) {
                this.stateEM = stateEM;
            }

            public String getStatePM() {
                return statePM;
            }

            public void setStatePM(String statePM) {
                this.statePM = statePM;
            }
        }

        public static class ResultDataBean implements Serializable {

            public int acountId;
            public String alarmModel;
            public String alarmType;
            public int deviceId;
            public double lat;
            public String name;
            public String positionState;
            public int radius;
            public String startTime;
            public String createTime;
            public String desc;
            public int fenceId;
            public int state;
            public double lng;
            public String endTime;
            public int fenceType;


            public int getAcountId() {
                return acountId;
            }

            public void setAcountId(int acountId) {
                this.acountId = acountId;
            }

            public String getAlarmType() {
                return alarmType;
            }

            public void setAlarmType(String alarmType) {
                this.alarmType = alarmType;
            }

            public String getAlarmModel() {
                return alarmModel;
            }

            public void setAlarmModel(String alarmModel) {
                this.alarmModel = alarmModel;
            }

            public int getDeviceId() {
                return deviceId;
            }

            public void setDeviceId(int deviceId) {
                this.deviceId = deviceId;
            }

            public double getLat() {
                return lat;
            }

            public void setLat(double lat) {
                this.lat = lat;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPositionState() {
                return positionState;
            }

            public void setPositionState(String positionState) {
                this.positionState = positionState;
            }

            public int getRadius() {
                return radius;
            }


            public void setRadius(int radius) {
                this.radius = radius;
            }

            public String getStartTime() {
                return startTime;
            }

            public void setStartTime(String startTime) {
                this.startTime = startTime;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public int getFenceId() {
                return fenceId;
            }

            public void setFenceId(int fenceId) {
                this.fenceId = fenceId;
            }

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
            }

            public double getLng() {
                return lng;
            }

            public void setLng(double lng) {
                this.lng = lng;
            }

            public String getEndTime() {
                return endTime;
            }

            public void setEndTime(String endTime) {
                this.endTime = endTime;
            }

            public int getFenceType() {
                return fenceType;
            }

            public void setFenceType(int fenceType) {
                this.fenceType = fenceType;
            }
        }


        public static class DeviceListBean implements Serializable {
            /**
             * acountId : 1
             * groupId : 15076778115073
             * acountName : 18664960759
             * nickName : 溜溜球
             * gender : 0
             * birthday : 2017-05-0800: 00: 00
             * email : lwf@hojy.com
             * address : 科技园
             * phone : 18664960759
             * imgUrl : https//kidwatch.hojy.com/a.jpg
             * deviceId : 1
             * imei : 0123456789
             * deviceName : 华域智能手表
             * deviceType : Q8
             * sosPhone : 110
             * isAdmin : 0
             */

            private long acountId;
            private long groupId;
            private String acountName;
            private String nickName;
            private int gender;
            private String birthday;
            private String email;
            private String shortPhone;
            private String address;
            private String phone;
            private String imgUrl;
            private int deviceId;
            private String imei;
            private String deviceName;
            private String deviceType;
            private String sosPhone;
            private int isAdmin;
            private String softVersion;
            private String hardVersion;

            public void setGroupId(long groupId) {
                this.groupId = groupId;
            }

            public String getHardVersion() {
                return hardVersion;
            }

            public void setHardVersion(String hardVersion) {
                this.hardVersion = hardVersion;
            }

            public String getSoftVersion() {
                return softVersion;
            }

            public void setSoftVersion(String softVersion) {
                this.softVersion = softVersion;
            }

            public String getShortPhone() {
                return shortPhone;
            }

            public void setShortPhone(String shortPhone) {
                this.shortPhone = shortPhone;
            }

            public long getAcountId() {
                return acountId;
            }

            public void setAcountId(long acountId) {
                this.acountId = acountId;
            }

            public long getGroupId() {
                return groupId;
            }

            public void setGroupId(int groupId) {
                this.groupId = groupId;
            }

            public String getAcountName() {
                return acountName;
            }

            public void setAcountName(String acountName) {
                this.acountName = acountName;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public int getGender() {
                return gender;
            }

            public void setGender(int gender) {
                this.gender = gender;
            }

            public String getBirthday() {
                return birthday;
            }

            public void setBirthday(String birthday) {
                this.birthday = birthday;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
            }

            public int getDeviceId() {
                return deviceId;
            }

            public void setDeviceId(int deviceId) {
                this.deviceId = deviceId;
            }

            public String getImei() {
                return imei;
            }

            public void setImei(String imei) {
                this.imei = imei;
            }

            public String getDeviceName() {
                return deviceName;
            }

            public void setDeviceName(String deviceName) {
                this.deviceName = deviceName;
            }

            public String getDeviceType() {
                return deviceType;
            }

            public void setDeviceType(String deviceType) {
                this.deviceType = deviceType;
            }

            public String getSosPhone() {
                return sosPhone;
            }

            public void setSosPhone(String sosPhone) {
                this.sosPhone = sosPhone;
            }

            public int getIsAdmin() {
                return isAdmin;
            }

            public void setIsAdmin(int isAdmin) {
                this.isAdmin = isAdmin;
            }
        }


        public static class MemberListBean implements Serializable {
            public String acountType;
            public String imgUrl;
            public long groupId;
            public String birthday;
            public String phone;
            public String acountName;
            public String address;
            public String email;
            public String nickName;
            public String relation;
            public int gender;
            public String isAdmin;
            public String acountId;
            public String status;
            public String isBind;
            public String deviceId;
            public String shortPhone;

            public String getShortPhone() {
                return shortPhone;
            }

            public void setShortPhone(String shortPhone) {
                this.shortPhone = shortPhone;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getIsBind() {
                return isBind;
            }

            public void setIsBind(String isBind) {
                this.isBind = isBind;
            }

            public String getDeviceId() {
                return deviceId;
            }

            public void setDeviceId(String deviceId) {
                this.deviceId = deviceId;
            }

            public String getAcountType() {
                return acountType;
            }

            public void setAcountType(String acountType) {
                this.acountType = acountType;
            }

            public String getAcountId() {
                return acountId;
            }

            public void setAcountId(String acountId) {
                this.acountId = acountId;
            }

            public String getIsAdmin() {
                return isAdmin;
            }

            public void setIsAdmin(String isAdmin) {
                this.isAdmin = isAdmin;
            }

            public int getGender() {
                return gender;
            }

            public void setGender(int gender) {
                this.gender = gender;
            }

            public String getRelation() {
                return relation;
            }

            public void setRelation(String relation) {
                this.relation = relation;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getAcountName() {
                return acountName;
            }

            public void setAcountName(String acountName) {
                this.acountName = acountName;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getBirthday() {
                return birthday;
            }

            public void setBirthday(String birthday) {
                this.birthday = birthday;
            }

            public long getGroupId() {
                return groupId;
            }

            public void setGroupId(long groupId) {
                this.groupId = groupId;
            }

            public String getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
            }
        }


        public static class HistoryLocationsBean implements Serializable {
            private int id;
            private String lbsData;
            private int mcc;
            private String wifiData;
            private int mnc;
            private double lng;
            private int type;
            private String locationTime;
            private double lat;
            private int deviceId;
            private String address;

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getLbsData() {
                return lbsData;
            }

            public void setLbsData(String lbsData) {
                this.lbsData = lbsData;
            }

            public int getMcc() {
                return mcc;
            }

            public void setMcc(int mcc) {
                this.mcc = mcc;
            }

            public String getWifiData() {
                return wifiData;
            }

            public void setWifiData(String wifiData) {
                this.wifiData = wifiData;
            }

            public int getMnc() {
                return mnc;
            }

            public void setMnc(int mnc) {
                this.mnc = mnc;
            }

            public double getLng() {
                return lng;
            }

            public void setLng(double lng) {
                this.lng = lng;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getLocationTime() {
                return locationTime;
            }

            public void setLocationTime(String locationTime) {
                this.locationTime = locationTime;
            }

            public double getLat() {
                return lat;
            }

            public void setLat(double lat) {
                this.lat = lat;
            }

            public int getDeviceId() {
                return deviceId;
            }

            public void setDeviceId(int deviceId) {
                this.deviceId = deviceId;
            }
        }


        public static class DevicePowerListBean implements Serializable {
            private long id;
            private String createTime;
            private String cycle;
            private String closeTimeStr;
            private String remark;
            private int state;
            private String openTimeStr;
            private long acountId;
            private long deviceId;

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getCycle() {
                return cycle;
            }

            public void setCycle(String cycle) {
                this.cycle = cycle;
            }

            public String getCloseTimeStr() {
                return closeTimeStr;
            }

            public void setCloseTimeStr(String closeTimeStr) {
                this.closeTimeStr = closeTimeStr;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
            }

            public String getOpenTimeStr() {
                return openTimeStr;
            }

            public void setOpenTimeStr(String openTimeStr) {
                this.openTimeStr = openTimeStr;
            }

            public long getAcountId() {
                return acountId;
            }

            public void setAcountId(long acountId) {
                this.acountId = acountId;
            }

            public long getDeviceId() {
                return deviceId;
            }

            public void setDeviceId(long deviceId) {
                this.deviceId = deviceId;
            }
        }


        public static class storyListBean implements Serializable {

            private String storyName;
            private String remark;
            private String storyImgUrl;
            private int size;
            private int storyTime;
            private String storyDesc;
            private String imei;
            private String createTime;
            private int storyId;
            private String acountName;
            private long id;


            public String getStoryName() {
                return storyName;
            }

            public void setStoryName(String storyName) {
                this.storyName = storyName;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getStoryImgUrl() {
                return storyImgUrl;
            }

            public void setStoryImgUrl(String storyImgUrl) {
                this.storyImgUrl = storyImgUrl;
            }

            public int getSize() {
                return size;
            }

            public void setSize(int size) {
                this.size = size;
            }

            public int getStoryTime() {
                return storyTime;
            }

            public void setStoryTime(int storyTime) {
                this.storyTime = storyTime;
            }

            public String getStoryDesc() {
                return storyDesc;
            }

            public void setStoryDesc(String storyDesc) {
                this.storyDesc = storyDesc;
            }

            public String getImei() {
                return imei;
            }

            public void setImei(String imei) {
                this.imei = imei;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public int getStoryId() {
                return storyId;
            }

            public void setStoryId(int storyId) {
                this.storyId = storyId;
            }

            public String getAcountName() {
                return acountName;
            }

            public void setAcountName(String acountName) {
                this.acountName = acountName;
            }

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            @Override
            public String toString() {
                return "storyList{" +
                        "storyName='" + storyName + '\'' +
                        ", remark='" + remark + '\'' +
                        ", storyImgUrl='" + storyImgUrl + '\'' +
                        ", size=" + size +
                        ", storyTime=" + storyTime +
                        ", storyDesc='" + storyDesc + '\'' +
                        ", imei='" + imei + '\'' +
                        ", createTime='" + createTime + '\'' +
                        ", storyId=" + storyId +
                        ", acountName='" + acountName + '\'' +
                        ", id=" + id +
                        '}';
            }
        }


        public static class MessageListBean implements Serializable {

            private String lastMessageAttr;
            private int lastMessageStatus;
            private String typeName;
            private int lastMessageAction;
            private String typeDesc;
            private int lastMessageType;
            private int lastMessageId;
            private int typeStatus;
            private String lastMessage;
            private int unReadNum;
            private long lastMessageExpTime;
            private int id;
            private String lastMessageAddTime;

            private String msgBody;
            private long msgExpTime;
            private String addTime;
            private String msgAttr;
            private int msgStatus;
            private String fromUser;
            private String userName;
            private int msgAction;
            private int msgType;

            private int currPage;
            private int pageSize;
            private int totalCount;
            private int totalPage;
            private List<ListBean> list;

            public List<ListBean> getList() {
                return list;
            }

            public void setList(List<ListBean> list) {
                this.list = list;
            }

            public int getCurrPage() {
                return currPage;
            }

            public void setCurrPage(int currPage) {
                this.currPage = currPage;
            }

            public int getPageSize() {
                return pageSize;
            }

            public void setPageSize(int pageSize) {
                this.pageSize = pageSize;
            }

            public int getTotalCount() {
                return totalCount;
            }

            public void setTotalCount(int totalCount) {
                this.totalCount = totalCount;
            }

            public int getTotalPage() {
                return totalPage;
            }

            public void setTotalPage(int totalPage) {
                this.totalPage = totalPage;
            }

            public long getMsgExpTime() {
                return msgExpTime;
            }

            public void setMsgExpTime(long msgExpTime) {
                this.msgExpTime = msgExpTime;
            }

            public String getAddTime() {
                return addTime;
            }

            public void setAddTime(String addTime) {
                this.addTime = addTime;
            }

            public String getMsgAttr() {
                return msgAttr;
            }

            public void setMsgAttr(String msgAttr) {
                this.msgAttr = msgAttr;
            }

            public int getMsgStatus() {
                return msgStatus;
            }

            public void setMsgStatus(int msgStatus) {
                this.msgStatus = msgStatus;
            }

            public String getFromUser() {
                return fromUser;
            }

            public void setFromUser(String fromUser) {
                this.fromUser = fromUser;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public int getMsgAction() {
                return msgAction;
            }

            public void setMsgAction(int msgAction) {
                this.msgAction = msgAction;
            }

            public int getMsgType() {
                return msgType;
            }

            public void setMsgType(int msgType) {
                this.msgType = msgType;
            }

            public String getMsgBody() {
                return msgBody;
            }

            public void setMsgBody(String msgBody) {
                this.msgBody = msgBody;
            }

            public String getLastMessageAttr() {
                return lastMessageAttr;
            }

            public void setLastMessageAttr(String lastMessageAttr) {
                this.lastMessageAttr = lastMessageAttr;
            }

            public String getLastMessageAddTime() {
                return lastMessageAddTime;
            }

            public void setLastMessageAddTime(String lastMessageAddTime) {
                this.lastMessageAddTime = lastMessageAddTime;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public long getLastMessageExpTime() {
                return lastMessageExpTime;
            }

            public void setLastMessageExpTime(long lastMessageExpTime) {
                this.lastMessageExpTime = lastMessageExpTime;
            }

            public int getUnReadNum() {
                return unReadNum;
            }

            public void setUnReadNum(int unReadNum) {
                this.unReadNum = unReadNum;
            }

            public String getLastMessage() {
                return lastMessage;
            }

            public void setLastMessage(String lastMessage) {
                this.lastMessage = lastMessage;
            }

            public int getTypeStatus() {
                return typeStatus;
            }

            public void setTypeStatus(int typeStatus) {
                this.typeStatus = typeStatus;
            }

            public int getLastMessageId() {
                return lastMessageId;
            }

            public void setLastMessageId(int lastMessageId) {
                this.lastMessageId = lastMessageId;
            }

            public int getLastMessageType() {
                return lastMessageType;
            }

            public void setLastMessageType(int lastMessageType) {
                this.lastMessageType = lastMessageType;
            }

            public String getTypeDesc() {
                return typeDesc;
            }

            public void setTypeDesc(String typeDesc) {
                this.typeDesc = typeDesc;
            }

            public int getLastMessageAction() {
                return lastMessageAction;
            }

            public void setLastMessageAction(int lastMessageAction) {
                this.lastMessageAction = lastMessageAction;
            }

            public String getTypeName() {
                return typeName;
            }

            public void setTypeName(String typeName) {
                this.typeName = typeName;
            }

            public int getLastMessageStatus() {
                return lastMessageStatus;
            }

            public void setLastMessageStatus(int lastMessageStatus) {
                this.lastMessageStatus = lastMessageStatus;
            }

            public static class ListBean {
                /**
                 * addTime : 2017-07-31 17:55:02
                 * fromUser : 865787030001511
                 * id : 2103
                 * msgAction : 2
                 * msgAttr : {}
                 * msgBody : 已进入 家 安全范围!
                 * msgExpTime : 0
                 * msgStatus : 0
                 * msgType : 1
                 * userName : 17666103375
                 */

                private String addTime;
                private String fromUser;
                private int id;
                private int msgAction;
                private String msgAttr;
                private String msgBody;
                private int msgExpTime;
                private int msgStatus;
                private int msgType;
                private String userName;

                public String getAddTime() {
                    return addTime;
                }

                public void setAddTime(String addTime) {
                    this.addTime = addTime;
                }

                public String getFromUser() {
                    return fromUser;
                }

                public void setFromUser(String fromUser) {
                    this.fromUser = fromUser;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public int getMsgAction() {
                    return msgAction;
                }

                public void setMsgAction(int msgAction) {
                    this.msgAction = msgAction;
                }

                public String getMsgAttr() {
                    return msgAttr;
                }

                public void setMsgAttr(String msgAttr) {
                    this.msgAttr = msgAttr;
                }

                public String getMsgBody() {
                    return msgBody;
                }

                public void setMsgBody(String msgBody) {
                    this.msgBody = msgBody;
                }

                public int getMsgExpTime() {
                    return msgExpTime;
                }

                public void setMsgExpTime(int msgExpTime) {
                    this.msgExpTime = msgExpTime;
                }

                public int getMsgStatus() {
                    return msgStatus;
                }

                public void setMsgStatus(int msgStatus) {
                    this.msgStatus = msgStatus;
                }

                public int getMsgType() {
                    return msgType;
                }

                public void setMsgType(int msgType) {
                    this.msgType = msgType;
                }

                public String getUserName() {
                    return userName;
                }

                public void setUserName(String userName) {
                    this.userName = userName;
                }
            }
        }


        public static class FriendsListBean implements Serializable {
            //            acountName:	string
//            required: false
//            好友账户名
//            nickName:	string
//            required: false
//            好友账户昵称
//            gender:	integer ($int32)
//            required: false
//            性别：0 未知 1 男 2 女
//            imgUrl:	string
//            required: false
//            好友头像地址
//            phone:	string
//            required: false
//            好友电话
            private String acountName;
            private String nickName;
            private int gender;
            private String imgUrl;
            private String phone;

            public String getAcountName() {
                return acountName;
            }

            public void setAcountName(String acountName) {
                this.acountName = acountName;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public int getGender() {
                return gender;
            }

            public void setGender(int gender) {
                this.gender = gender;
            }

            public String getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }
        }


        public static class bandRequestListBean implements Serializable {
            private int deviceId;
            private String imei;
            private String nickName;
            private String imgUrl;

            public int getDeviceId() {
                return deviceId;
            }

            public void setDeviceId(int deviceId) {
                this.deviceId = deviceId;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public String getImei() {
                return imei;
            }

            public void setImei(String imei) {
                this.imei = imei;
            }

            public String getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
            }
        }

        public static class alarmClockListBean implements Serializable {
            private int nextTime;
            private String remark;
            private int repeatNumber;
            private String cycle;
            private String createTime;
            private long acountId;
            private String alarmTimeString;
            private int isActive;
            private long deviceId;
            private long id;

            public int getNextTime() {
                return nextTime;
            }

            public void setNextTime(int nextTime) {
                this.nextTime = nextTime;
            }

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public long getDeviceId() {
                return deviceId;
            }

            public void setDeviceId(long deviceId) {
                this.deviceId = deviceId;
            }

            public int getIsActive() {
                return isActive;
            }

            public void setIsActive(int isActive) {
                this.isActive = isActive;
            }

            public String getAlarmTimeString() {
                return alarmTimeString;
            }

            public void setAlarmTimeString(String alarmTimeString) {
                this.alarmTimeString = alarmTimeString;
            }

            public long getAcountId() {
                return acountId;
            }

            public void setAcountId(long acountId) {
                this.acountId = acountId;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getCycle() {
                return cycle;
            }

            public void setCycle(String cycle) {
                this.cycle = cycle;
            }

            public int getRepeatNumber() {
                return repeatNumber;
            }

            public void setRepeatNumber(int repeatNumber) {
                this.repeatNumber = repeatNumber;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            @Override
            public String toString() {
                return "alarmClockListBean{" +
                        "nextTime=" + nextTime +
                        ", remark='" + remark + '\'' +
                        ", repeatNumber=" + repeatNumber +
                        ", cycle='" + cycle + '\'' +
                        ", createTime='" + createTime + '\'' +
                        ", acountId=" + acountId +
                        ", alarmTimeString='" + alarmTimeString + '\'' +
                        ", isActive=" + isActive +
                        ", deviceId=" + deviceId +
                        ", id=" + id +
                        '}';
            }
        }


        public String getProductKey() {
            return productKey;
        }

        public void setProductKey(String productKey) {
            this.productKey = productKey;
        }

        public String getDeviceSecret() {
            return deviceSecret;
        }

        public void setDeviceSecret(String deviceSecret) {
            this.deviceSecret = deviceSecret;
        }

        public String getUpdatedFlag() {
            return updatedFlag;
        }

        public void setUpdatedFlag(String updatedFlag) {
            this.updatedFlag = updatedFlag;
        }

        public String getSsid() {
            return ssid;
        }

        public void setSsid(String ssid) {
            this.ssid = ssid;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public void setDevicePowerId(String devicePowerId) {
            this.devicePowerId = devicePowerId;
        }

        public int getOperator() {
            return operator;
        }

        public void setOperator(int operator) {
            this.operator = operator;
        }

        public String getCustomCmd() {
            return customCmd;
        }

        public void setCustomCmd(String customCmd) {
            this.customCmd = customCmd;
        }

        public String getFlowCmd() {
            return flowCmd;
        }

        public void setFlowCmd(String flowCmd) {
            this.flowCmd = flowCmd;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getNumberCmd() {
            return numberCmd;
        }

        public void setNumberCmd(String numberCmd) {
            this.numberCmd = numberCmd;
        }

        public String getBillCmd() {
            return billCmd;
        }

        public void setBillCmd(String billCmd) {
            this.billCmd = billCmd;
        }

        public String getServiceNumber() {
            return serviceNumber;
        }

        public void setServiceNumber(String serviceNumber) {
            this.serviceNumber = serviceNumber;
        }

        public double getAccuracy() {
            return accuracy;
        }

        public void setAccuracy(double accuracy) {
            this.accuracy = accuracy;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public String getLocationTime() {
            return locationTime;
        }

        public void setLocationTime(String locationTime) {
            this.locationTime = locationTime;
        }

        public int getLogType() {
            return logType;
        }

        public void setLogType(int logType) {
            this.logType = logType;
        }

        public int getDeviceBindStatus() {
            return deviceBindStatus;
        }

        public void setDeviceBindStatus(int deviceBindStatus) {
            this.deviceBindStatus = deviceBindStatus;
        }

        public int getLocationStyle() {
            return locationStyle;
        }

        public void setLocationStyle(int locationStyle) {
            this.locationStyle = locationStyle;
        }

        public int getMobileStyle() {
            return mobileStyle;
        }

        public void setMobileStyle(int mobileStyle) {
            this.mobileStyle = mobileStyle;
        }

        public long getDeviceSetUpId() {
            return deviceSetUpId;
        }

        public void setDeviceSetUpId(long deviceSetUpId) {
            this.deviceSetUpId = deviceSetUpId;
        }

        public String getDevicePowerId() {
            return devicePowerId;
        }

        public String getBootState() {
            return bootState;
        }

        public void setBootState(String bootState) {
            this.bootState = bootState;
        }

        public String getPowerLevel() {
            return powerLevel;
        }

        public void setPowerLevel(String powerLevel) {
            this.powerLevel = powerLevel;
        }

        public List<ForbiddenTimeListBean> getForbiddenTimeList() {
            return forbiddenTimeList;
        }

        public void setForbiddenTimeList(List<ForbiddenTimeListBean> forbiddenTimeList) {
            this.forbiddenTimeList = forbiddenTimeList;
        }

        public List<storyListBean> getStoryList() {
            return storyList;
        }

        public void setStoryList(List<storyListBean> storyLists) {
            storyLists = storyList;
        }


        public List<StepsRankingListBean> getStepsRankingList() {
            return stepsRankingList;
        }

        public void setStepsRankingList(List<StepsRankingListBean> stepsRankingList) {
            this.stepsRankingList = stepsRankingList;
        }

        public String getCycle() {
            return cycle;
        }

        public void setCycle(String cycle) {
            this.cycle = cycle;
        }

        public String getEndTimeEmStr() {
            return endTimeEmStr;
        }

        public void setEndTimeEmStr(String endTimeEmStr) {
            this.endTimeEmStr = endTimeEmStr;
        }

        public String getEndTimeAmStr() {
            return endTimeAmStr;
        }

        public void setEndTimeAmStr(String endTimeAmStr) {
            this.endTimeAmStr = endTimeAmStr;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getStateEM() {
            return stateEM;
        }

        public void setStateEM(String stateEM) {
            this.stateEM = stateEM;
        }

        public String getEndTimePmStr() {
            return endTimePmStr;
        }

        public void setEndTimePmStr(String endTimePmStr) {
            this.endTimePmStr = endTimePmStr;
        }

        public String getStartTimeEmStr() {
            return startTimeEmStr;
        }

        public void setStartTimeEmStr(String startTimeEmStr) {
            this.startTimeEmStr = startTimeEmStr;
        }

        public String getStateAM() {
            return stateAM;
        }

        public void setStateAM(String stateAM) {
            this.stateAM = stateAM;
        }

        public String getStartTimeAmStr() {
            return startTimeAmStr;
        }

        public void setStartTimeAmStr(String startTimeAmStr) {
            this.startTimeAmStr = startTimeAmStr;
        }

        public long getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(long deviceId) {
            this.deviceId = deviceId;
        }

        public String getStatePM() {
            return statePM;
        }

        public void setStatePM(String statePM) {
            this.statePM = statePM;
        }

        public String getStartTimePmStr() {
            return startTimePmStr;
        }

        public void setStartTimePmStr(String startTimePmStr) {
            this.startTimePmStr = startTimePmStr;
        }

        public String getFenceSwitch() {
            return fenceSwitch;
        }

        public void setFenceSwitch(String fenceSwitchss) {
            fenceSwitch = fenceSwitchss;
        }

        public String getForbiddenTimeSwitch() {
            return forbiddenTimeSwitch;
        }

        public void setForbiddenTimeSwitch(String forbiddenTimeSwitchs) {
            forbiddenTimeSwitch = forbiddenTimeSwitchs;
        }


        public String getStrangeCallSwitch() {
            return strangeCallSwitch;
        }

        public void setStrangeCallSwitch(String strangeCallSwitchs) {
            strangeCallSwitch = strangeCallSwitchs;
        }

        public String getDevicePowerSwitch() {
            return devicePowerSwitch;
        }

        public void setDevicePowerSwitch(String devicePowerSwitchs) {
            devicePowerSwitch = devicePowerSwitchs;
        }

        public String getImei() {
            return imei;
        }

        public void setImei(String imeis) {
            this.imei = imeis;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public String getHardVersion() {
            return hardVersion;
        }

        public void setHardVersion(String hardVersion) {
            this.hardVersion = hardVersion;
        }

        public String getSoftVersion() {
            return softVersion;
        }

        public void setSoftVersion(String softVersion) {
            this.softVersion = softVersion;
        }

        public String getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(String deviceType) {
            this.deviceType = deviceType;
        }

        public String getSosPhone() {
            return sosPhone;
        }

        public void setSosPhone(String sosPhone) {
            this.sosPhone = sosPhone;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public Boolean getHasNewVesion() {
            return hasNewVesion;
        }

        public void setHasNewVesion(Boolean hasNewVesion) {
            this.hasNewVesion = hasNewVesion;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getLastLoginTime() {
            return lastLoginTime;
        }

        public void setLastLoginTime(String lastLoginTime) {
            this.lastLoginTime = lastLoginTime;
        }

        public String getCreateUser() {
            return createUser;
        }

        public void setCreateUser(String createUser) {
            this.createUser = createUser;
        }

        public String getAcountType() {
            return acountType;
        }

        public void setAcountType(String acountType) {
            this.acountType = acountType;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public boolean isHaseAdmin() {
            return haseAdmin;
        }

        public List<HistoryLocationsBean> getHistoryLocations() {
            return historyLocations;
        }

        public void setHistoryLocations(List<HistoryLocationsBean> historyLocations) {
            this.historyLocations = historyLocations;
        }

        public List<DevicePowerListBean> getDevicePowerList() {
            return devicePowerList;
        }

        public void setDevicePowerList(List<DevicePowerListBean> devicePowerList) {
            this.devicePowerList = devicePowerList;
        }

        public List<DeviceListBean> getDeviceList() {
            return deviceList;
        }


        public List<ResultDataBean> getResultData() {
            return resultData;
        }

        public List<MemberListBean> getMemberList() {
            return memberList;
        }

        public void setMemberList(List<MemberListBean> memberList) {
            this.memberList = memberList;
        }

        public void setResultData(List<ResultDataBean> resultData) {
            this.resultData = resultData;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }


        public boolean getHaseAdmin() {
            return haseAdmin;
        }

        public void setHaseAdmin(boolean haseAdmin) {
            this.haseAdmin = haseAdmin;
        }


        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }


        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public void setDeviceList(List<DeviceListBean> deviceList) {
            this.deviceList = deviceList;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public long getAcountId() {
            return acountId;
        }

        public void setAcountId(long acountId) {
            this.acountId = acountId;
        }

        public String getAcountName() {
            return acountName;
        }

        public void setAcountName(String acountName) {
            this.acountName = acountName;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        @Override
        public String toString() {
            return "ResultBean{" +
                    "lastLoginTime='" + lastLoginTime + '\'' +
                    ", createUser='" + createUser + '\'' +
                    ", acountType='" + acountType + '\'' +
                    ", createTime='" + createTime + '\'' +
                    ", nickName='" + nickName + '\'' +
                    ", phone='" + phone + '\'' +
                    ", gender=" + gender +
                    ", acountId=" + acountId +
                    ", id=" + id +
                    ", createDate='" + createDate + '\'' +
                    ", code='" + code + '\'' +
                    ", token='" + token + '\'' +
                    ", acountName='" + acountName + '\'' +
                    ", birthday='" + birthday + '\'' +
                    ", email='" + email + '\'' +
                    ", address='" + address + '\'' +
                    ", imgUrl='" + imgUrl + '\'' +
                    ", url='" + url + '\'' +
                    ", haseAdmin=" + haseAdmin +
                    ", password='" + password + '\'' +
                    ", desc='" + desc + '\'' +
                    ", updateTime='" + updateTime + '\'' +
                    ", hasNewVesion=" + hasNewVesion +
                    ", version='" + version + '\'' +
                    ", type=" + type +
                    ", resultData=" + resultData +
                    ", deviceList=" + deviceList +
                    ", memberList=" + memberList +
                    ", historyLocations=" + historyLocations +
                    '}';
        }
    }
}
