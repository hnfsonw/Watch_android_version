/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.loybin.baidumap.widget.chatrow;

import com.hyphenate.easeui.EaseConstant;
import com.loybin.baidumap.util.LogUtils;

public class Constant extends EaseConstant {

    public static final String NEW_FRIENDS_USERNAME = "item_new_friends";
    public static final String GROUP_USERNAME = "item_groups";
    public static final String CHAT_ROOM = "item_chatroom";
    public static final String ACCOUNT_REMOVED = "account_removed";
    public static final String ACCOUNT_CONFLICT = "conflict";
    public static final String ACCOUNT_FORBIDDEN = "user_forbidden";
    public static final String CHAT_ROBOT = "item_robots";
    public static final String MESSAGE_ATTR_ROBOT_MSGTYPE = "msgtype";
    public static final String ACTION_GROUP_CHANAGED = "action_group_changed";
    public static final String ACTION_CONTACT_CHANAGED = "action_contact_changed";
    public static final String XIAOMI_ID = "2882303761517589924";
    public static final String XIAOMI_KEY = "5761758980924";

    public static final String HEAD_IMAGE_URL = "headImageUrl";//发送人的头像
    public static final String USER_ID = "userid";
    public static final String USER_NAME = "mUsername";
    public static final String SEX = "sex";
    public static final String OBJECT_HEAD_IMAGE_URL = "objectHeadImageUrl";//接收人的头像
    public static final String OBJECT_USERID = "objectUserid";
    public static final String OBJECT_USER_NAME = "objectUserName";
    public static final String OBJECT_USER_SEX = "objectUserSex";

    public static final int DEBUGLEVEL = LogUtils.LEVEL_ALL;
    public static final long PROTOCOL_TIMEOUT = 5 * 60 * 1000;//5分钟
    public static final long PROTOCOL_TIMEOUT_HOURS = 60 * 60 * 1000;//一小时
    public static final long PROTOCOL_TIMEOUT_DAY = 24 * 60 * 60 * 1000;//一天
    public static final long PROTOCOL_TIMEOUT_MONTH = (long) (30L * 24L * 60L * 60L * 1000L);//一个月

}
