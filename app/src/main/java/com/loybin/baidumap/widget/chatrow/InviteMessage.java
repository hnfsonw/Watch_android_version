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

public class InviteMessage {
    private String mFrom;
    private long mTime;
    private String mReason;

    private InviteMesageStatus mStatus;
    private String mGroupId;
    private String mGroupName;
    private String mGroupInviter;

    private int mId;

    public String getFrom() {
        return mFrom;
    }

    public void setFrom(String from) {
        this.mFrom = from;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        this.mTime = time;
    }


    public String getReason() {
        return mReason;
    }

    public void setReason(String reason) {
        this.mReason = reason;
    }

    public InviteMesageStatus getStatus() {
        return mStatus;
    }

    public void setStatus(InviteMesageStatus status) {
        this.mStatus = status;
    }


    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getGroupId() {
        return mGroupId;
    }

    public void setGroupId(String groupId) {
        this.mGroupId = groupId;
    }

    public String getGroupName() {
        return mGroupName;
    }

    public void setGroupName(String groupName) {
        this.mGroupName = groupName;
    }

    public void setGroupInviter(String inviter) {
        mGroupInviter = inviter;
    }

    public String getGroupInviter() {
        return mGroupInviter;
    }


    public enum InviteMesageStatus {

        //==contact
        /**being invited*/
        BEINVITEED,
        /**being refused*/
        BEREFUSED,
        /**remote user already agreed*/
        BEAGREED,

        //==group application
        /**remote user apply to join*/
        BEAPPLYED,
        /**you have agreed to join*/
        AGREED,
        /**you refused the join request*/
        REFUSED,

        //==group invitation
        /**received remote user's invitation**/
        GROUPINVITATION,
        /**remote user accept your invitation**/
        GROUPINVITATION_ACCEPTED,
        /**remote user declined your invitation**/
        GROUPINVITATION_DECLINED
    }

}



