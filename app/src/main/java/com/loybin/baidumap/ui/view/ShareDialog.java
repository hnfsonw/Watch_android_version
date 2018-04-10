package com.loybin.baidumap.ui.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.config.Constants;
import com.loybin.baidumap.ui.activity.WXEntryActivity;
import com.loybin.baidumap.ui.activity.WatchLeisureActivity;
import com.loybin.baidumap.ui.fragment.FamilyFragment;
import com.loybin.baidumap.util.MyApplication;
import com.loybin.baidumap.util.UserUtil;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.loybin.baidumap.util.UIUtils.getResources;


/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/04/22 上午10:31
 * 描   述:  选择头像的dialog
 */

public class ShareDialog extends AlertDialog {

    private static final String TAG = "HeadDialog";
    private IWXAPI mApi;
    @BindView(R.id.user_setting_photo)
    LinearLayout mLlBaiduMap;

    @BindView(R.id.user_setting_album)
    LinearLayout mLlGaodeMap;

    @BindView(R.id.user_weixin)
    LinearLayout mLlWeiXin;

    private static final int THUMB_SIZE = 150;
    private FamilyFragment mFamilyFragment;
    private WatchLeisureActivity mWatchLeisureActivity;
    private WXEntryActivity mWXEntryActivity;
    private Context mContext;
    private String mPhone;
    private String mUrl = "https://kidwatch-manager.hojy.com/hgts-manager/app-store/download.html";
    private String mRelation;


    public ShareDialog(Context context, FamilyFragment babyDataActivity) {
        super(context, R.style.MyDialog);
        mApi = WXAPIFactory.createWXAPI(context, Constants.APP_ID);
        mContext = context;
        mFamilyFragment = babyDataActivity;

    }

    public ShareDialog(Context context, WatchLeisureActivity babyDataActivity) {
        super(context, R.style.MyDialog);
        mApi = WXAPIFactory.createWXAPI(context, Constants.APP_ID);
        mContext = context;
        mWatchLeisureActivity = babyDataActivity;
    }

    public ShareDialog(Context context, WXEntryActivity babyDataActivity) {
        super(context, R.style.MyDialog);
        mApi = WXAPIFactory.createWXAPI(context, Constants.APP_ID);
        mContext = context;
        mWXEntryActivity = babyDataActivity;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_share);
        ButterKnife.bind(this);
        getWindow().setGravity(Gravity.BOTTOM); //显示在底部

    }


    /**
     * 设置宽度全屏，要设置在show的后面
     */
    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        getWindow().getDecorView().setPadding(0, 0, 0, 0);

        getWindow().setAttributes(layoutParams);

    }


    @OnClick({R.id.user_setting_photo, R.id.user_setting_album, R.id.user_weixin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.user_setting_photo:
                sendSMS("你好， " + MyApplication.sDeviceListBean.getNickName() + "的" + mRelation + "邀请你加入开心果家庭，" +
                        "让我们一起陪伴宝贝成长吧! 加入方式：\n 1.下载并安装开心果app \n 2.注册app账号后登录，确认邀请即可\n app下载地址" + mUrl, mPhone);
                dismiss();
                break;

            case R.id.user_setting_album:
                dismiss();
                break;

            case R.id.user_weixin:

                weiXin(MyApplication.sDeviceListBean.getNickName() + "的" + mRelation, MyApplication.sDeviceListBean.getImei());

                dismiss();
                break;

        }
    }


    /**
     * 微信分享
     *
     * @param string
     * @param phone
     */
    private void weiXin(String string, String phone) {
        //初始化一个WXTextObject对象
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "https://kidwatch.hojy.com/hgts/weiXinInvite?nickName=" + string + "&inviteCode=" + phone;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = string + "邀请你加入开心果家庭";
        msg.description = "你好， " + string + "邀请你加入开心果家庭，" +
                "让我们一起陪伴宝贝成长吧! 加入方式：\n 1.下载并安装开心果app \n 2.注册app账号后登录，确认邀请即可";
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.app_icon);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();
        msg.thumbData = UserUtil.bmpToByteArray(thumbBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        mApi.sendReq(req);
    }


    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }


    /**
     * 短信邀请
     */
    private void sendSMS(String smsBody, String phone) {
        Uri smsToUri = Uri.parse("smsto:" + phone);

        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);

        intent.putExtra("sms_body", smsBody);
        if (mFamilyFragment != null) {
            mFamilyFragment.startActivity(intent);
        }

        if (mWatchLeisureActivity != null) {
            mWatchLeisureActivity.startActivity(intent);
        }

        if (mWXEntryActivity != null) {
            mWXEntryActivity.startActivity(intent);
        }

    }


    public void setPhone(String newPhone, String relation) {
        mPhone = newPhone;
        mRelation = relation;
    }
}
