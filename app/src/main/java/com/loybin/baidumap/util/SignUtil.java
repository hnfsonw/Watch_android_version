/**
 * Alibaba.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.loybin.baidumap.util;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.loybin.baidumap.mqtt.ALiyunIotX509TrustManager;

import java.util.Arrays;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;


/**
 * 签名工具类
 *
 * <p>具体说明</p>
 *
 * @version $Id: SignUtil.java,v 0.1 2016年1月19日 下午4:18:09  Exp $
 */
public class SignUtil {
    
    
    /**
     * 对参数签名 hmac 算法 可选 HmacMD5 或  HmacSHA1 或 MD5（不推荐）
     * @param params
     * @return
     */
    public static String sign(Map<String, String> params, String deviceSecret,String signMethod) {
        //将参数Key按字典顺序排序
        String[] sortedKeys = params.keySet().toArray(new String[] {});
        Arrays.sort(sortedKeys);

        //生成规范化请求字符串
        StringBuilder canonicalizedQueryString = new StringBuilder();
        for (String key : sortedKeys) {
            if ("sign".equalsIgnoreCase(key)) {
                continue;
            }
            canonicalizedQueryString.append(key).append(params.get(key));
        }
        LogUtils.e("signmethod:[" + signMethod + "]");
        if ("MD5".equalsIgnoreCase(signMethod)) {
            String content = deviceSecret + canonicalizedQueryString.toString() + deviceSecret;
            LogUtils.e(content);
            return Md5.getInstance().md5_32(content).toUpperCase();
        } else {
            try {
                String key = deviceSecret;
                LogUtils.e("sign with key[" + key + "]");
                LogUtils.e("sign content:[" + canonicalizedQueryString.toString() + "]");
                return encryptHMAC(signMethod,canonicalizedQueryString.toString(), key);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * HMAC加密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptHMAC(String signMethod,String content, String key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key.getBytes("utf-8"), signMethod);
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        mac.init(secretKey);
        byte[] data = mac.doFinal(content.getBytes("utf-8"));
        return CipherUtils.bytesToHexString(data);
    }
    /**
     * HMAC加密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptHMACbase64(String signMethod,String content, String key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key.getBytes("utf-8"), signMethod);
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        mac.init(secretKey);
        byte[] data = mac.doFinal(content.getBytes("utf-8"));
//        return Base64.encodeBytes(data);
        return null;
    }


    public static SSLSocketFactory createSSLSocket() throws Exception {
        SSLContext context = SSLContext.getInstance("TLSV1.2");
        context.init(null, new TrustManager[] {new ALiyunIotX509TrustManager()}, null);
        SSLSocketFactory socketFactory = context.getSocketFactory();
        return socketFactory;
    }

}
