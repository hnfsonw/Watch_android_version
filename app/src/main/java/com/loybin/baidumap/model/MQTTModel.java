package com.loybin.baidumap.model;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/11/27 下午2:34
 * 描   述: MQTT 透传bean
 */
public class MQTTModel {


    /**
     * message : 上传定位信息
     * command : 10006
     * parameters : {"acountId":222}
     */

    private String message;
    private int command;
    private long msgId;
    private int msgType;
    private int msgAction;
    private String type;
    private String msg;
    private long time;
    private String from;
    private ParametersBean parameters;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public int getMsgAction() {
        return msgAction;
    }

    public void setMsgAction(int msgAction) {
        this.msgAction = msgAction;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCommand() {
        return command;
    }

    public void setCommand(int command) {
        this.command = command;
    }

    public ParametersBean getParameters() {
        return parameters;
    }

    public void setParameters(ParametersBean parameters) {
        this.parameters = parameters;
    }

    public static class ParametersBean {
        /**
         * acountId : 222
         */

        private String acountId;
        private String deviceId;
        private String imei;
        private String nickName;
        private String imgUrl;
        private String address;
        private String lng;
        private String lat;
        private String locationTime;
        private String success;
        private String ssid;
        private String isBand;
        private String replayStatus;

        public String getAcountId() {
            return acountId;
        }

        public void setAcountId(String acountId) {
            this.acountId = acountId;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getImei() {
            return imei;
        }

        public void setImei(String imei) {
            this.imei = imei;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLocationTime() {
            return locationTime;
        }

        public void setLocationTime(String locationTime) {
            this.locationTime = locationTime;
        }

        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
        }

        public String getSsid() {
            return ssid;
        }

        public void setSsid(String ssid) {
            this.ssid = ssid;
        }

        public String getIsBand() {
            return isBand;
        }

        public void setIsBand(String isBand) {
            this.isBand = isBand;
        }

        public String getReplayStatus() {
            return replayStatus;
        }

        public void setReplayStatus(String replayStatus) {
            this.replayStatus = replayStatus;
        }

        @Override
        public String toString() {
            return "ParametersBean{" +
                    "acountId='" + acountId + '\'' +
                    ", deviceId='" + deviceId + '\'' +
                    ", imei='" + imei + '\'' +
                    ", nickName='" + nickName + '\'' +
                    ", imgUrl='" + imgUrl + '\'' +
                    ", address='" + address + '\'' +
                    ", lng='" + lng + '\'' +
                    ", lat='" + lat + '\'' +
                    ", locationTime='" + locationTime + '\'' +
                    ", success='" + success + '\'' +
                    ", ssid='" + ssid + '\'' +
                    ", isBand='" + isBand + '\'' +
                    ", replayStatus='" + replayStatus + '\'' +
                    '}';
        }
    }
}
