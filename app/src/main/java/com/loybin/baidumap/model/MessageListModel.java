package com.loybin.baidumap.model;

import java.util.List;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/09/25 上午10:06
 * 描   述: 消息中心 model
 */
public class MessageListModel {


    /**
     * resultMsg : 请求成功，无任何异常
     * result : {"messageList":{"currPage":1,"list":[{"addTime":"2017-07-31 17:55:02","fromUser":"865787030001511","id":2103,"msgAction":2,"msgAttr":"{}","msgBody":"已进入 家 安全范围!","msgExpTime":0,"msgStatus":0,"msgType":1,"userName":"17666103375"}],"pageSize":30,"totalCount":1,"totalPage":1}}
     * time : 1506319580287
     * errorCode : 0
     * resultCode : 80000
     */

    private String resultMsg;
    private ResultBean result;
    private long time;
    private int errorCode;
    private int resultCode;

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

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public static class ResultBean {
        /**
         * messageList : {"currPage":1,"list":[{"addTime":"2017-07-31 17:55:02","fromUser":"865787030001511","id":2103,"msgAction":2,"msgAttr":"{}","msgBody":"已进入 家 安全范围!","msgExpTime":0,"msgStatus":0,"msgType":1,"userName":"17666103375"}],"pageSize":30,"totalCount":1,"totalPage":1}
         */

        private MessageListBean messageList;

        public MessageListBean getMessageList() {
            return messageList;
        }

        public void setMessageList(MessageListBean messageList) {
            this.messageList = messageList;
        }

        public static class MessageListBean {
            /**
             * currPage : 1
             * list : [{"addTime":"2017-07-31 17:55:02","fromUser":"865787030001511","id":2103,"msgAction":2,"msgAttr":"{}","msgBody":"已进入 家 安全范围!","msgExpTime":0,"msgStatus":0,"msgType":1,"userName":"17666103375"}]
             * pageSize : 30
             * totalCount : 1
             * totalPage : 1
             */

            private int currPage;
            private int pageSize;
            private int totalCount;
            private int totalPage;
            private List<ListBean> list;

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

            public List<ListBean> getList() {
                return list;
            }

            public void setList(List<ListBean> list) {
                this.list = list;
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
    }
}
