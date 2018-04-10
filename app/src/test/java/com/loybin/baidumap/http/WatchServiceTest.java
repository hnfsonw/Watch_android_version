package com.loybin.baidumap.http;

import android.content.Context;

import com.loybin.baidumap.base.BasePresenter;
import com.loybin.baidumap.model.ResponseInfoModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;

import static org.junit.Assert.*;

/**
 * Created by huangz on 17/8/31.
 */

@RunWith(MockitoJUnitRunner.class)
public class WatchServiceTest{

    @Mock
    private Context mContext;

    private int flag = 0;


    private BasePresenter mBasePresenter;
    private Call<ResponseInfoModel> mResponseInfoModelCall;
    private CountDownLatch latch = new CountDownLatch(1);

    public WatchServiceTest() {
    }

    @Before
    public void setUp() throws Exception {
        mBasePresenter = new BasePresenter(mContext) {
            @Override
            protected void parserJson(ResponseInfoModel data) {
                flag = 0;
                flag++;
                latch.countDown();
            }

            @Override
            protected void onFaiure(ResponseInfoModel s) {
                flag = 0;
                latch.countDown();
            }
        };
    }

//    @Test
//    public void login() throws Exception {
//        Map<String, String> hashmap = new HashMap<>();
//        hashmap.put("mobile", "18565894667");
//        hashmap.put("password", "chsj958100");
//
//        mResponseInfoModelCall = mBasePresenter.mWatchService.login(hashmap);
//        mResponseInfoModelCall.enqueue(mBasePresenter.mCallback);
//
//        latch.await();
//
//        assertEquals(1, flag);
//    }


}