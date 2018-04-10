package com.loybin.baidumap.widget.chatrow;

import android.content.Context;

import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.model.EaseAtMessageHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DemoModel {
    UserDao dao = null;
    protected Context mContext = null;
    protected Map<Key, Object> mValueCache = new HashMap<Key, Object>();

    public DemoModel(Context ctx) {
        mContext = ctx;
        PreferenceManager.init(mContext);
    }

    public boolean saveContactList(List<EaseUser> contactList) {
        UserDao dao = new UserDao(mContext);
        dao.saveContactList(contactList);
        return true;
    }

    public Map<String, EaseUser> getContactList() {
        UserDao dao = new UserDao(mContext);
        return dao.getContactList();
    }

    public void saveContact(EaseUser user) {
        UserDao dao = new UserDao(mContext);
        dao.saveContact(user);
    }

    /**
     * save current mUsername
     *
     * @param username
     */
    public void setCurrentUserName(String username) {
        PreferenceManager.getInstance().setCurrentUserName(username);
    }

    public String getCurrentUsernName() {
        return PreferenceManager.getInstance().getCurrentUsername();
    }

    public Map<String, RobotUser> getRobotList() {
        UserDao dao = new UserDao(mContext);
        return dao.getRobotUser();
    }

    public boolean saveRobotList(List<RobotUser> robotList) {
        UserDao dao = new UserDao(mContext);
        dao.saveRobotUser(robotList);
        return true;
    }

    public void setSettingMsgNotification(boolean paramBoolean) {
        PreferenceManager.getInstance().setSettingMsgNotification(paramBoolean);
        mValueCache.put(Key.VibrateAndPlayToneOn, paramBoolean);
    }

    public boolean getSettingMsgNotification() {
        Object val = mValueCache.get(Key.VibrateAndPlayToneOn);

        if (val == null) {
            val = PreferenceManager.getInstance().getSettingMsgNotification();
            mValueCache.put(Key.VibrateAndPlayToneOn, val);
        }

        return (Boolean) (val != null ? val : true);
    }

    public void setSettingMsgSound(boolean paramBoolean) {
        PreferenceManager.getInstance().setSettingMsgSound(paramBoolean);
        mValueCache.put(Key.PlayToneOn, paramBoolean);
    }

    public boolean getSettingMsgSound() {
        Object val = mValueCache.get(Key.PlayToneOn);

        if (val == null) {
            val = PreferenceManager.getInstance().getSettingMsgSound();
            mValueCache.put(Key.PlayToneOn, val);
        }

        return (Boolean) (val != null ? val : true);
    }

    public void setSettingMsgVibrate(boolean paramBoolean) {
        PreferenceManager.getInstance().setSettingMsgVibrate(paramBoolean);
        mValueCache.put(Key.VibrateOn, paramBoolean);
    }

    public boolean getSettingMsgVibrate() {
        Object val = mValueCache.get(Key.VibrateOn);

        if (val == null) {
            val = PreferenceManager.getInstance().getSettingMsgVibrate();
            mValueCache.put(Key.VibrateOn, val);
        }

        return (Boolean) (val != null ? val : true);
    }

    public void setSettingMsgSpeaker(boolean paramBoolean) {
        PreferenceManager.getInstance().setSettingMsgSpeaker(paramBoolean);
        mValueCache.put(Key.SpakerOn, paramBoolean);
    }

    public boolean getSettingMsgSpeaker() {
        Object val = mValueCache.get(Key.SpakerOn);

        if (val == null) {
            val = PreferenceManager.getInstance().getSettingMsgSpeaker();
            mValueCache.put(Key.SpakerOn, val);
        }

        return (Boolean) (val != null ? val : true);
    }


    public void setDisabledGroups(List<String> groups) {
        if (dao == null) {
            dao = new UserDao(mContext);
        }

        List<String> list = new ArrayList<String>();
        list.addAll(groups);
        for (int i = 0; i < list.size(); i++) {
            if (EaseAtMessageHelper.get().getAtMeGroups().contains(list.get(i))) {
                list.remove(i);
                i--;
            }
        }

        dao.setDisabledGroups(list);
        mValueCache.put(Key.DisabledGroups, list);
    }

    public List<String> getDisabledGroups() {
        Object val = mValueCache.get(Key.DisabledGroups);

        if (dao == null) {
            dao = new UserDao(mContext);
        }

        if (val == null) {
            val = dao.getDisabledGroups();
            mValueCache.put(Key.DisabledGroups, val);
        }

        //noinspection unchecked
        return (List<String>) val;
    }

    public void setDisabledIds(List<String> ids) {
        if (dao == null) {
            dao = new UserDao(mContext);
        }

        dao.setDisabledIds(ids);
        mValueCache.put(Key.DisabledIds, ids);
    }

    public List<String> getDisabledIds() {
        Object val = mValueCache.get(Key.DisabledIds);

        if (dao == null) {
            dao = new UserDao(mContext);
        }

        if (val == null) {
            val = dao.getDisabledIds();
            mValueCache.put(Key.DisabledIds, val);
        }

        //noinspection unchecked
        return (List<String>) val;
    }

    public void setGroupsSynced(boolean synced) {
        PreferenceManager.getInstance().setGroupsSynced(synced);
    }

    public boolean isGroupsSynced() {
        return PreferenceManager.getInstance().isGroupsSynced();
    }

    public void setContactSynced(boolean synced) {
        PreferenceManager.getInstance().setContactSynced(synced);
    }

    public boolean isContactSynced() {
        return PreferenceManager.getInstance().isContactSynced();
    }

    public void setBlacklistSynced(boolean synced) {
        PreferenceManager.getInstance().setBlacklistSynced(synced);
    }

    public boolean isBacklistSynced() {
        return PreferenceManager.getInstance().isBacklistSynced();
    }

    public void allowChatroomOwnerLeave(boolean value) {
        PreferenceManager.getInstance().setSettingAllowChatroomOwnerLeave(value);
    }

    public boolean isChatroomOwnerLeaveAllowed() {
        return PreferenceManager.getInstance().getSettingAllowChatroomOwnerLeave();
    }

    public void setDeleteMessagesAsExitGroup(boolean value) {
        PreferenceManager.getInstance().setDeleteMessagesAsExitGroup(value);
    }

    public boolean isDeleteMessagesAsExitGroup() {
        return PreferenceManager.getInstance().isDeleteMessagesAsExitGroup();
    }

    public void setAutoAcceptGroupInvitation(boolean value) {
        PreferenceManager.getInstance().setAutoAcceptGroupInvitation(value);
    }

    public boolean isAutoAcceptGroupInvitation() {
        return PreferenceManager.getInstance().isAutoAcceptGroupInvitation();
    }


    public void setAdaptiveVideoEncode(boolean value) {
        PreferenceManager.getInstance().setAdaptiveVideoEncode(value);
    }

    public boolean isAdaptiveVideoEncode() {
        return PreferenceManager.getInstance().isAdaptiveVideoEncode();
    }

    public void setPushCall(boolean value) {
        PreferenceManager.getInstance().setPushCall(value);
    }

    public boolean isPushCall() {
        return PreferenceManager.getInstance().isPushCall();
    }

    public void setRestServer(String restServer) {
        PreferenceManager.getInstance().setRestServer(restServer);
    }

    public String getRestServer() {
        return PreferenceManager.getInstance().getRestServer();
    }

    public void setIMServer(String imServer) {
        PreferenceManager.getInstance().setIMServer(imServer);
    }

    public String getIMServer() {
        return PreferenceManager.getInstance().getIMServer();
    }

    public void enableCustomServer(boolean enable) {
        PreferenceManager.getInstance().enableCustomServer(enable);
    }

    public boolean isCustomServerEnable() {
        return PreferenceManager.getInstance().isCustomServerEnable();
    }

    public void enableCustomAppkey(boolean enable) {
        PreferenceManager.getInstance().enableCustomAppkey(enable);
    }

    public boolean isCustomAppkeyEnabled() {
        return PreferenceManager.getInstance().isCustomAppkeyEnabled();
    }

    public void setCustomAppkey(String appkey) {
        PreferenceManager.getInstance().setCustomAppkey(appkey);
    }

    public String getCutomAppkey() {
        return PreferenceManager.getInstance().getCustomAppkey();
    }

    enum Key {
        VibrateAndPlayToneOn,
        VibrateOn,
        PlayToneOn,
        SpakerOn,
        DisabledGroups,
        DisabledIds
    }
}
