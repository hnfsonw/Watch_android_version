package com.loybin.baidumap.base;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.hojy.happyfruit.BuildConfig;
import com.hojy.happyfruit.R;
import com.loybin.baidumap.config.Constants;
import com.loybin.baidumap.http.WatchService;
import com.loybin.baidumap.model.MessageListModel;
import com.loybin.baidumap.model.ResponseInfoModel;
import com.loybin.baidumap.ui.activity.DevicesHomeActivity;
import com.loybin.baidumap.ui.activity.LoginActivity;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/04/21 上午11:14
 * 描   述: 数据请求的业务基类
 */
public abstract class BasePresenter {

    private static final String TAG = "BasePresenter";
    private final SharedPreferences mGlobalvariable;
    protected Retrofit mRetrofit;
    protected WatchService mWatchService;
    private HttpLoggingInterceptor mInterceptor;
    private Context mContext;

    public BasePresenter(Context context) {
        mGlobalvariable = context.getSharedPreferences("globalvariable", 0);
        mContext = context;
        OkHttpClient builder = getBuilder();

        mRetrofit = new Retrofit.Builder().baseUrl(Constants.HOST).addConverterFactory
                (GsonConverterFactory.create()).client(builder).build();
        mWatchService = mRetrofit.create(WatchService.class);
        //把所有接口都封装到service里面
    }


    /**
     * 获取okhttp配置
     * @return
     */
    private OkHttpClient getBuilder() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // log用拦截器

        mInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.e(TAG, message);
            }
        });
        // 开发模式记录整个body，否则只记录基本信息如返回200，http协议版本等
        if (BuildConfig.DEBUG) {
            mInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            mInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        }
        builder.addInterceptor(mInterceptor)
                .connectTimeout(60, TimeUnit.SECONDS)//设置超时
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);//错误重连
        return builder.build();
    }


    protected Callback mCallback = new Callback<ResponseInfoModel>() {

        @Override
        public void onResponse(Call<ResponseInfoModel> call, Response<ResponseInfoModel> response) {
            ResponseInfoModel body = response.body();
            if (body == null){
                return;
            }
            int state = body.getResultCode();
            Log.e(TAG, "onResponse: " + body.getResultMsg());
            Log.e(TAG, "onResponse: " + body.getResultCode());
//            成功取到数据
            if (Constants.State80000 == state ) {
                parserJson(body);

            }else if (Constants.State80001 == state){
                //验证Token是否失效
                boolean chekToken = chekToken(body);
                if (body.getErrorCode() == Constants.ERROR92001){
                    return;
                }
                if (!chekToken){
                onFaiure(body);
                }else {
                }
            }
        }


        @Override
        public void onFailure(Call<ResponseInfoModel> call, Throwable t) {
            onDissms(t.getMessage());
            //没连上服务器
            Toast.makeText(MyApplication.sInstance,
                    MyApplication.sInstance.getString(R.string.Network_Error),
                    Toast.LENGTH_SHORT).show();
        }
    };

    protected void tokenError() {

    }


    protected Callback<ResponseInfoModel> mCallback2 = new Callback<ResponseInfoModel>() {
        @Override
        public void onResponse(Call<ResponseInfoModel> call, Response<ResponseInfoModel> response) {
            ResponseInfoModel body = response.body();
            if (body == null){
                return;
            }
            Log.e(TAG, "onResponse: " + body.getResultMsg() );
            Log.e(TAG, "onResponse: " + body.getResultCode());
            int state = body.getResultCode();
            Log.e(TAG, "onResponse: " + body.getResultMsg() );
            Log.e(TAG, "onResponse: " + body.getResultCode());
            if (Constants.State80000 == state){
                onSuccess(body);
            }else if (Constants.State80001 == state){
                //验证Token是否失效
                boolean chekToken = chekToken(body);
                if (body.getErrorCode() == Constants.ERROR92001)
                    return;
                if (!chekToken)
                onError(body);
            }

        }

        @Override
        public void onFailure(Call<ResponseInfoModel> call, Throwable t) {
            onDissms(t.getMessage());
            //没连上服务器
        }
    };


    protected Callback<ResponseInfoModel> mCallback3 = new Callback<ResponseInfoModel>() {
        @Override
        public void onResponse(Call<ResponseInfoModel> call, Response<ResponseInfoModel> response) {
            ResponseInfoModel body = response.body();
            if (body == null){
                return;
            }
            Log.e(TAG, "onResponse: " + body.getResultMsg() );
            Log.e(TAG, "onResponse: " + body.getResultCode());
            int state = body.getResultCode();
            Log.e(TAG, "onResponse: " + body.getResultMsg() );
            Log.e(TAG, "onResponse: " + body.getResultCode());
            if (Constants.State80000 == state){
                memberListSuccess(body);
            }else if (Constants.State80001 == state){
                //验证Token是否失效
                LogUtils.e(TAG,"错误码 "+body.getErrorCode());
                boolean chekToken = chekToken(body);
                if (body.getErrorCode() == Constants.ERROR92001){
                    return;
                }
                if (!chekToken){
                onError(body);
                }

            }

        }

        @Override
        public void onFailure(Call<ResponseInfoModel> call, Throwable t) {
            onDissms(t.getMessage());
            //没连上服务器

        }
    };



    protected Callback<ResponseInfoModel> mCallback4 = new Callback<ResponseInfoModel>() {
        @Override
        public void onResponse(Call<ResponseInfoModel> call, Response<ResponseInfoModel> response) {
            ResponseInfoModel body = response.body();
            if (body == null){
                return;
            }
            Log.e(TAG, "onResponse: " + body.getResultMsg() );
            Log.e(TAG, "onResponse: " + body.getResultCode());
            int state = body.getResultCode();
            Log.e(TAG, "onResponse: " + body.getResultMsg() );
            Log.e(TAG, "onResponse: " + body.getResultCode());
            if (Constants.State80000 == state){
                appCMDSuccess(body);
            }else if (Constants.State80001 == state){
                //验证Token是否失效
                LogUtils.e(TAG,"错误码 "+body.getErrorCode());
                boolean chekToken = chekToken(body);
                if (!chekToken){
                onError(body);
                }

            }

        }

        @Override
        public void onFailure(Call<ResponseInfoModel> call, Throwable t) {
            onDissms(t.getMessage());
            //没连上服务器

        }
    };



    protected Callback mCallback5 = new Callback<MessageListModel>() {

        @Override
        public void onResponse(Call<MessageListModel> call, Response<MessageListModel> response) {
            MessageListModel body = response.body();
            if (body == null){
                return;
            }
            int state = body.getResultCode();
            Log.e(TAG, "onResponse: " + body.getResultMsg());
            Log.e(TAG, "onResponse: " + body.getResultCode());
//            成功取到数据
            if (Constants.State80000 == state ) {
                parserJson(body);

            }else if (Constants.State80001 == state){
                //验证Token是否失效
                boolean chekToken = chekToken(body);
                if (body.getErrorCode() == Constants.ERROR92001){
                    return;
                }
                if (!chekToken){
                    onFaiure(body);
                }
            }
        }


        @Override
        public void onFailure(Call<MessageListModel> call, Throwable t) {
            onDissms(t.getMessage());
            //没连上服务器
            Toast.makeText(MyApplication.sInstance,
                    MyApplication.sInstance.getString(R.string.Network_Error),
                    Toast.LENGTH_SHORT).show();
        }
    };


    protected Callback<ResponseInfoModel> mCallback6 = new Callback<ResponseInfoModel>() {
        @Override
        public void onResponse(Call<ResponseInfoModel> call, Response<ResponseInfoModel> response) {
            ResponseInfoModel body = response.body();
            if (body == null){
                return;
            }
            Log.e(TAG, "onResponse: " + body.getResultMsg() );
            Log.e(TAG, "onResponse: " + body.getResultCode());
            int state = body.getResultCode();
            Log.e(TAG, "onResponse: " + body.getResultMsg() );
            Log.e(TAG, "onResponse: " + body.getResultCode());
            if (Constants.State80000 == state){
                messageSuccess(body);
            }else if (Constants.State80001 == state){
                //验证Token是否失效
                LogUtils.e(TAG,"错误码 "+body.getErrorCode());
                boolean chekToken = chekToken(body);
                if (!chekToken){
                    onError(body);
                }

            }

        }

        @Override
        public void onFailure(Call<ResponseInfoModel> call, Throwable t) {
            onDissms(t.getMessage());
            //没连上服务器

        }
    };


    protected Callback<ResponseInfoModel> mCallback7 = new Callback<ResponseInfoModel>() {
        @Override
        public void onResponse(Call<ResponseInfoModel> call, Response<ResponseInfoModel> response) {
            ResponseInfoModel body = response.body();
            if (body == null){
                return;
            }
            Log.e(TAG, "onResponse: " + body.getResultMsg() );
            Log.e(TAG, "onResponse: " + body.getResultCode());
            int state = body.getResultCode();
            Log.e(TAG, "onResponse: " + body.getResultMsg() );
            Log.e(TAG, "onResponse: " + body.getResultCode());
            if (Constants.State80000 == state){
                tokenSuccess(body);
            }else if (Constants.State80001 == state){
                //验证Token是否失效
                LogUtils.e(TAG,"错误码 "+body.getErrorCode());
                boolean chekToken = chekToken(body);
                if (!chekToken){
                    onError(body);
                }

            }

        }

        @Override
        public void onFailure(Call<ResponseInfoModel> call, Throwable t) {
            onDissms(t.getMessage());
            //没连上服务器

        }
    };

    protected void tokenSuccess(ResponseInfoModel body) {

    }

    protected void messageSuccess(ResponseInfoModel body) {
    }


    protected void appCMDSuccess(ResponseInfoModel body) {

    }


    protected void memberListSuccess(ResponseInfoModel body) {

    }


    protected void onDissms(String s) {

    }


    protected void onError(ResponseInfoModel body) {

    }


    protected void onSuccess(ResponseInfoModel body) {

    }


    protected abstract void parserJson(ResponseInfoModel data);


    protected abstract void onFaiure(ResponseInfoModel s);

    protected void parserJson(MessageListModel data){

    }

    protected void onFaiure(MessageListModel data){

    }


    protected boolean chekToken(ResponseInfoModel body) {
        if (body.getResultMsg().equals(mContext.getString(R.string.log))){
            tokenError();
            exit();
            return true;
        }
        return false;
    }


    public void exit() {
        MyApplication.sInUpdata = true;
        EMClient.getInstance().logout(false, new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.i(TAG, "logout success");
            }

            @Override
            public void onError(int i, String s) {
                Log.i(TAG, "logout error " + i + " - " + s);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
        boolean aBoolean = mGlobalvariable.getBoolean("login", false);
        if (aBoolean){
            Toast.makeText(mContext,mContext.getString(R.string.login_has_expired_please_login_again),Toast.LENGTH_SHORT).show();
        }
        XmPlayerManager.release();
        Intent intent2 = new Intent("jason.broadcast.action");
        intent2.putExtra("closeAll", 1);
        mContext.sendBroadcast(intent2);//发送广播

        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        mGlobalvariable.edit().putBoolean("login", false).apply();
        mContext.startActivity(intent);
    }


    private boolean chekToken(MessageListModel body) {
        if (body.getResultMsg().equals(mContext.getString(R.string.log))){

            MyApplication.sInUpdata = true;
            EMClient.getInstance().logout(false, new EMCallBack() {
                @Override
                public void onSuccess() {
                    Log.i(TAG, "logout success");
                }

                @Override
                public void onError(int i, String s) {
                    Log.i(TAG, "logout error " + i + " - " + s);
                }

                @Override
                public void onProgress(int i, String s) {

                }
            });
            boolean aBoolean = mGlobalvariable.getBoolean("login", false);
            if (aBoolean){
            Toast.makeText(mContext,mContext.getString(R.string.login_has_expired_please_login_again),Toast.LENGTH_SHORT).show();
            }
            XmPlayerManager.release();
            Intent intent = new Intent(mContext, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            mGlobalvariable.edit().putBoolean("login", false).apply();
            mContext.startActivity(intent);
            return true;
        }
        return false;
    }



}
