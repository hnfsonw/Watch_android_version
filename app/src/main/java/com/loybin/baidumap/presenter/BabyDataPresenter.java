package com.loybin.baidumap.presenter;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import com.hojy.happyfruit.R;
import com.loybin.baidumap.base.BasePresenter;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.BabyDataActivity;
import com.loybin.baidumap.util.LogUtils;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/05/08 下午5:29
 * 描   述: 设置宝贝资料的业务逻辑
 */
public class BabyDataPresenter extends BasePresenter {

    private static final String TAG = "BabyDataActivity";
    private Context mContext;

    private BabyDataActivity mBabyDataActivity;
    public Call<ResponseInfoModel> mAcountBindImeiFirst;
    public Call<ResponseInfoModel> mUpload;
    public Call<ResponseInfoModel> mAcountDeivceList;

    public BabyDataPresenter(Context context, BabyDataActivity babyDataActivity) {
        super(context);
        mContext = context;
        mBabyDataActivity = babyDataActivity;
    }


    /**
     * 填写宝贝资料 post提交
     *
     * @param acountId
     * @param imei
     * @param babyName
     * @param gender
     * @param imgUrl
     * @param watchNumber
     * @param birthday
     * @param relation
     * @param token
     */
    public void acountBindImeiFirst(long acountId, String imei, String babyName, int gender,
                                    String imgUrl, String watchNumber, String birthday,
                                    String relation, String token) {

        if (TextUtils.isEmpty(watchNumber)) {
            mBabyDataActivity.numbeiEmpty();
            return;
        }

        if (TextUtils.isEmpty(babyName)) {
            mBabyDataActivity.nameEmpty(mContext.getString(R.string.name_enpty));
            return;
        }

        if (TextUtils.isEmpty(birthday)) {
            mBabyDataActivity.birthdayNull();
            return;
        }

        if (TextUtils.isEmpty(relation)) {
            mBabyDataActivity.relationEmpty();
            return;
        }

        HashMap<String, Object> paramsMap = new HashMap<>();

        paramsMap.put("acountId", acountId);
        paramsMap.put("imei", imei);
        paramsMap.put("nickName", babyName);
        paramsMap.put("gender", gender + "");
        paramsMap.put("imgUrl", imgUrl);
        paramsMap.put("phone", watchNumber);
        paramsMap.put("birthday", birthday);
        paramsMap.put("relation", relation);
        paramsMap.put("token", token);
        Log.e("BabyDataActivity", "toRegis: " + String.valueOf(paramsMap));
        //执行enqueue
        mAcountBindImeiFirst = mWatchService.acountBindImeiFirst(paramsMap);
        mBabyDataActivity.showLoading(mContext.getString(R.string.dialogMessage),mContext);
        mAcountBindImeiFirst.enqueue(mCallback);
    }


    /**
     * 上传图片
     *
     * @param bitmapFile
     * @param token
     */
    public void upload(File bitmapFile, String token) {
        Log.d("BabyDataActivity", "upload: " + bitmapFile.getAbsolutePath());
        Log.d("BabyDataActivity", "upload: " + token);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), bitmapFile);
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("token", token);
        paramsMap.put("file", bitmapFile.getAbsolutePath());

        Log.e("BabyDataActivity", "toRegis: " + String.valueOf(paramsMap));
        //执行enqueue
        mUpload = mWatchService.upload(token, requestFile);
        mBabyDataActivity.showLoading(mContext.getString(R.string.dialogMessage),mContext);
        mUpload.enqueue(mCallback2);
    }


    /**
     * 上传图片成功的回调
     *
     * @param body
     */
    @Override
    protected void onSuccess(ResponseInfoModel body) {
        String imgUrl = body.getResult().getUrl();
        mBabyDataActivity.onSuccess(imgUrl);

        Log.d("BabyDataActivity", "imgUrl: " + imgUrl);
        Log.d("BabyDataActivity", "body.getResultMsg(): " + body.getResultMsg());
        Log.d("BabyDataActivity", "onSuccess: " + body.getResult().getId());
        Log.d("BabyDataActivity", "onSuccess: " + body.getResult().getCreateDate());
    }


    /**
     * 上传图片80001失败的回掉
     *
     * @param body
     */
    @Override
    protected void onError(ResponseInfoModel body) {
        mBabyDataActivity.error(body.getResultMsg());
        Log.d("BabyDataActivity", "onError: " + body.getResultMsg());
    }


    /**
     * 宝贝资料设置完毕成功的回调
     *
     * @param data
     */
    @Override
    protected void parserJson(ResponseInfoModel data) {
        mBabyDataActivity.succeed();
        Log.d(TAG, "parserJson: " + data.getResultMsg());

    }


    /**
     * 宝贝资料 80001失败的回掉
     *
     * @param s
     */
    @Override
    protected void onFaiure(ResponseInfoModel s) {
        mBabyDataActivity.error(s.getResultMsg());
    }


    /**
     * 没连接上服务器
     *
     * @param s
     */
    @Override
    protected void onDissms(String s) {
        Log.d("BabyDataActivity", "onDissms: " + s);
        mBabyDataActivity.dismissLoading();
    }


    /**
     * 获取绑定设备列表
     *
     * @param acountId
     * @param token
     */
    public void getAcountDeivceList(long acountId, String token) {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("acountId", acountId + "");
        paramsMap.put("token", token);

        Log.e(TAG, "toRegis: " + String.valueOf(paramsMap));
        mAcountDeivceList = mWatchService.getAcountDeivceList(paramsMap);
        mBabyDataActivity.showLoading("",mContext);
        mAcountDeivceList.enqueue(mCallback3);
    }


    @Override
    protected void memberListSuccess(ResponseInfoModel body) {
        //
        LogUtils.e(TAG, body.getResultMsg());
        List<ResponseInfoModel.ResultBean.DeviceListBean> deviceList = body.getResult().getDeviceList();
        Collections.sort(deviceList, new Comparator<ResponseInfoModel.ResultBean.DeviceListBean>() {
            @Override
            public int compare(ResponseInfoModel.ResultBean.DeviceListBean o1, ResponseInfoModel.ResultBean.DeviceListBean o2) {
                //降序排序
                return (o2.getIsAdmin() - o1.getIsAdmin());
            }
        });
        mBabyDataActivity.selectDialogShow(deviceList);
    }


    /**
     * 验证通讯录权限
     */
    public void chekPermissions() {
        if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            //申请权限  第二个参数是一个 数组 说明可以同时申请多个权限
            ActivityCompat.requestPermissions(mBabyDataActivity, new String[]{android.Manifest.permission.READ_CONTACTS}, 1);
        } else {//已授权
            obtionContacts();
        }
    }


    public void obtionContacts() {
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        Intent intent = new Intent(Intent.ACTION_PICK, uri);
        mBabyDataActivity.startActivityForResult(intent, 0);
    }


    public String[] getPhoneContacts(Uri uri) {
        String[] contact = new String[2];
        //得到ContentResolver对象
        ContentResolver cr = mContext.getContentResolver();
        //取得电话本中开始一项的光标
        Cursor cursor = cr.query(uri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            //取得联系人姓名
            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            contact[0] = cursor.getString(nameFieldColumnIndex);
            //取得电话号码
            String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);
            if (phone != null) {
                phone.moveToFirst();
                contact[1] = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                phone.close();
            }
            cursor.close();
        } else {
            return null;
        }
        return contact;
    }
}