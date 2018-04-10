package com.loybin.baidumap.model;

import java.util.List;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/05/26 下午6:43
 * 描   述: 首页定位的bean
 */
public class ParametersModel {


    /**
     * message : 上传定位信息
     * command : 10006
     * parameters : {"accuracy":64,"deviceId":75,"id":770,"lat":22.550998018259,"lng":113.95182758939,"locationTime":"2017-05-26 18:52:00","mcc":460,"mnc":0,"type":2,"wifiData":"[{\"macAddress\":\"F0:B4:29:EE:DE:FD\",\"signalStrength\":-2},{\"macAddress\":\"8C:F2:28:82:E1:B2\",\"signalStrength\":-48},{\"macAddress\":\"80:81:00:A0:73:88\",\"signalStrength\":-64},{\"macAddress\":\"00:5E:E2:82:B3:68\",\"signalStrength\":-57},{\"macAddress\":\"B0:D5:9D:40:7A:06\",\"signalStrength\":-54},{\"macAddress\":\"FC:D7:33:6B:7E:98\",\"signalStrength\":-64},{\"macAddress\":\"78:52:62:06:CC:2D\",\"signalStrength\":-59},{\"macAddress\":\"C4:12:F5:37:D9:10\",\"signalStrength\":-76},{\"macAddress\":\"B0:D5:9D:57:53:86\",\"signalStrength\":-57},{\"macAddress\":\"BC:96:80:E4:FC:20\",\"signalStrength\":-74},{\"macAddress\":\"00:87:46:0F:54:17\",\"signalStrength\":-56},{\"macAddress\":\"FC:37:2B:50:C0:41\",\"signalStrength\":-65},{\"macAddress\":\"B0:D5:9D:8C:6F:55\",\"signalStrength\":-55},{\"macAddress\":\"EC:17:2F:FE:BF:32\",\"signalStrength\":-45},{\"macAddress\":\"A8:15:4D:FE:38:E2\",\"signalStrength\":-58},{\"macAddress\":\"B0:D5:9D:93:2F:46\",\"signalStrength\":-40},{\"macAddress\":\"78:52:62:2E:72:73\",\"signalStrength\":-50},{\"macAddress\":\"30:B4:9E:2C:5B:6D\",\"signalStrength\":-37},{\"macAddress\":\"CC:81:DA:08:8A:D0\",\"signalStrength\":-50},{\"macAddress\":\"EC:6C:9F:41:F8:7A\",\"signalStrength\":-66},{\"macAddress\":\"00:00:FB:75:9F:B0\",\"signalStrength\":-40},{\"macAddress\":\"88:25:93:7E:22:29\",\"signalStrength\":-45},{\"macAddress\":\"50:FA:84:12:6F:40\",\"signalStrength\":-57},{\"macAddress\":\"C8:E0:EB:58:E9:C7\",\"signalStrength\":-47},{\"macAddress\":\"B0:D5:9D:85:2B:DC\",\"signalStrength\":-47},{\"macAddress\":\"B0:D5:9D:2B:14:52\",\"signalStrength\":-76},{\"macAddress\":\"EE:26:CA:04:65:51\",\"signalStrength\":-53},{\"macAddress\":\"00:1F:8B:77:FE:7F\",\"signalStrength\":-61},{\"macAddress\":\"00:BD:82:16:B6:1B\",\"signalStrength\":-78}]"}
     */

    private String message;
    private int command;
    private ParametersBean parameters;

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
         * accuracy : 64
         * deviceId : 75
         * id : 770
         * lat : 22.550998018259
         * lng : 113.95182758939
         * locationTime : 2017-05-26 18:52:00
         * mcc : 460
         * mnc : 0
         * type : 2
         * wifiData : [{"macAddress":"F0:B4:29:EE:DE:FD","signalStrength":-2},{"macAddress":"8C:F2:28:82:E1:B2","signalStrength":-48},{"macAddress":"80:81:00:A0:73:88","signalStrength":-64},{"macAddress":"00:5E:E2:82:B3:68","signalStrength":-57},{"macAddress":"B0:D5:9D:40:7A:06","signalStrength":-54},{"macAddress":"FC:D7:33:6B:7E:98","signalStrength":-64},{"macAddress":"78:52:62:06:CC:2D","signalStrength":-59},{"macAddress":"C4:12:F5:37:D9:10","signalStrength":-76},{"macAddress":"B0:D5:9D:57:53:86","signalStrength":-57},{"macAddress":"BC:96:80:E4:FC:20","signalStrength":-74},{"macAddress":"00:87:46:0F:54:17","signalStrength":-56},{"macAddress":"FC:37:2B:50:C0:41","signalStrength":-65},{"macAddress":"B0:D5:9D:8C:6F:55","signalStrength":-55},{"macAddress":"EC:17:2F:FE:BF:32","signalStrength":-45},{"macAddress":"A8:15:4D:FE:38:E2","signalStrength":-58},{"macAddress":"B0:D5:9D:93:2F:46","signalStrength":-40},{"macAddress":"78:52:62:2E:72:73","signalStrength":-50},{"macAddress":"30:B4:9E:2C:5B:6D","signalStrength":-37},{"macAddress":"CC:81:DA:08:8A:D0","signalStrength":-50},{"macAddress":"EC:6C:9F:41:F8:7A","signalStrength":-66},{"macAddress":"00:00:FB:75:9F:B0","signalStrength":-40},{"macAddress":"88:25:93:7E:22:29","signalStrength":-45},{"macAddress":"50:FA:84:12:6F:40","signalStrength":-57},{"macAddress":"C8:E0:EB:58:E9:C7","signalStrength":-47},{"macAddress":"B0:D5:9D:85:2B:DC","signalStrength":-47},{"macAddress":"B0:D5:9D:2B:14:52","signalStrength":-76},{"macAddress":"EE:26:CA:04:65:51","signalStrength":-53},{"macAddress":"00:1F:8B:77:FE:7F","signalStrength":-61},{"macAddress":"00:BD:82:16:B6:1B","signalStrength":-78}]
         */

        private String accuracy;
        private long deviceId;
        private int id;
        private double lat;
        private double lng;
        private String locationTime;
        private int mcc;
        private int mnc;
        private int type;
        private String wifiData;
        private long acountId;
        private String address;
        private String nickName;
        //开机状态
        private String bootState;
        //拒绝陌生人来电
        private String strangeCallSwitch;
        //定位类型
        private String locationStyle;
        //电量类型
        private String powerLevel;

        private String content;

        private List<WifiDataListBean> wifiDataList;

        private String connectedSsid;

        private String connectedSuccess;

        private long imGroupId;

        public long getImGroupId() {
            return imGroupId;
        }

        public void setImGroupId(long imGroupId) {
            this.imGroupId = imGroupId;
        }

        public List<WifiDataListBean> getWifiDataList() {
            return wifiDataList;
        }

        public void setWifiDataList(List<WifiDataListBean> wifiDataList) {
            this.wifiDataList = wifiDataList;
        }

        public String getConnectedSsid() {
            return connectedSsid;
        }

        public void setConnectedSsid(String connectedSsid) {
            this.connectedSsid = connectedSsid;
        }

        public String getConnectedSuccess() {
            return connectedSuccess;
        }

        public void setConnectedSuccess(String connectedSuccess) {
            this.connectedSuccess = connectedSuccess;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getBootState() {
            return bootState;
        }

        public void setBootState(String bootState) {
            this.bootState = bootState;
        }

        public String getStrangeCallSwitch() {
            return strangeCallSwitch;
        }

        public void setStrangeCallSwitch(String strangeCallSwitch) {
            this.strangeCallSwitch = strangeCallSwitch;
        }

        public String getLocationStyle() {
            return locationStyle;
        }

        public void setLocationStyle(String locationStyle) {
            this.locationStyle = locationStyle;
        }

        public String getPowerLevel() {
            return powerLevel;
        }

        public void setPowerLevel(String powerLevel) {
            this.powerLevel = powerLevel;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public long getAcountId() {
            return acountId;
        }

        public void setAcountId(long acountId) {
            this.acountId = acountId;
        }

        public String getAccuracy() {
            return accuracy;
        }

        public void setAccuracy(String accuracy) {
            this.accuracy = accuracy;
        }

        public long getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(long deviceId) {
            this.deviceId = deviceId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public int getMcc() {
            return mcc;
        }

        public void setMcc(int mcc) {
            this.mcc = mcc;
        }

        public int getMnc() {
            return mnc;
        }

        public void setMnc(int mnc) {
            this.mnc = mnc;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getWifiData() {
            return wifiData;
        }

        public void setWifiData(String wifiData) {
            this.wifiData = wifiData;
        }
    }

    public static class WifiDataListBean {
        private String ssid;
        private String locked;
        private int signal;
        private boolean isConnect;

        public boolean isConnect() {
            return isConnect;
        }

        public void setConnect(boolean connect) {
            isConnect = connect;
        }

        public String getSsid() {
            return ssid;
        }

        public void setSsid(String ssid) {
            this.ssid = ssid;
        }

        public String getLocked() {
            return locked;
        }

        public void setLocked(String locked) {
            this.locked = locked;
        }

        public int getSignal() {
            return signal;
        }

        public void setSignal(int signal) {
            this.signal = signal;
        }
    }
}
