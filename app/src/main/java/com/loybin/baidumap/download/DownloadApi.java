package com.loybin.baidumap.download;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by  LoyBin
 */
public interface DownloadApi {
    /**
     * 下载APK
     *
     * @param fileUrl
     * @return
     */
    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlSync(@Url String fileUrl);

}
