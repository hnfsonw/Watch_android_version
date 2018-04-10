package com.loybin.baidumap.base;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loybin.baidumap.util.FileUtils;
import com.loybin.baidumap.util.IOUtils;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;
import com.loybin.baidumap.util.UIUtils;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;


/**
 * 类    名:  BaseProtocol
 * 创 建 者:  LoyBin
 * 创建时间:  2017/9/8 15:56
 * 描    述： 针对听故事里面所有的网络请求
 */
public abstract class BaseProtocol<RESOBJTYPE> {

    private static final String TAG = "StoryBooksFramgent";
    private int mIndex;
    private List<Album> mResult;


    public List<Album> getResult() {
        return mResult;
    }

    public void setResult(List<Album> result) {
        mResult = result;
    }

    /**
     * @return
     * @throws IOException
     * @des 从内存, 返回
     * @des 从本地, 存内存, 返回
     * @des 从网络, 存本地,存内存, 返回
     * @des 从网络加载数据, 返回应的对象
     */
    public List<Album> loadData(int index,String id) throws IOException {
        //从内存, 返回
        mResult = loadDataFromMem(index);
        if (mResult != null) {
            LogUtils.e(TAG,"从内存加载数据-->" + generateCacheKey(index));
            return mResult;
        }

        //从本地, 存内存, 返回
        mResult = loadDataFromLocal(index);
        if (mResult != null) {//本地有有效的缓存
            LogUtils.e(TAG,"从本地加载数据-->" + getCacheFile(index).getAbsolutePath());
            return mResult;
        }
        //从网络, 存本地,存内存, 返回
        LogUtils.e(TAG,"从网络");
        mResult = loadDataFromNet(index,id);
        return mResult;
    }

    /**
     * 从内存加载数据
     *
     * @param index
     * @return
     */
    private List<Album> loadDataFromMem(int index) {
        //找到存储结构
//        MyApplication对象
        MyApplication app = (MyApplication) UIUtils.getContext();

        Map<String, Object> cacheMap = app.getCacheMap();

        //生成缓存key
        String key = generateCacheKey(index);

        if (cacheMap.containsKey(key)) {
            //有
            String memData = (String) cacheMap.get(key);
            //解析
            return parseData(memData, new Gson());
        }
        return null;
    }


    /**
     * 从本地加载数据
     *
     * @param index
     * @return
     */
    private List<Album>  loadDataFromLocal(int index) {
        BufferedReader reader = null;
        try {
            File cacheFile = getCacheFile(index);

            if (cacheFile.exists()) {//存在
                //是否有效
                //读取缓存的生成时间-->缓存文件的第一行
                reader = new BufferedReader(new FileReader(cacheFile));

                //缓存文件的第一行
                String firstLine = reader.readLine();

                long cacheInsertTime = Long.parseLong(firstLine);

//                if ((System.currentTimeMillis() - cacheInsertTime) < Constant.PROTOCOL_TIMEOUT) {
                    //有效缓存
                    String cacheData = reader.readLine();


                    //保存数据到内存中
                    saveData2Mem(index, cacheData);

                    //解析
                    return parseData(cacheData, new Gson());
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭流
            IOUtils.close(reader);
        }
        return null;
    }



    /**
     * 去本地加载数据
     * @param imei 唯一标识
     */
    public String loadDataFromLocal(String imei,long timeout) {
        LogUtils.e(TAG,timeout+"");
        BufferedReader reader = null;
        try {
            File cacheFile = getCacheFile(imei);
            LogUtils.e(TAG,cacheFile.exists()+"");

            if (cacheFile.exists()) {//存在
                //是否有效
                //读取缓存的生成时间-->缓存文件的第一行
                reader = new BufferedReader(new FileReader(cacheFile));

                //缓存文件的第一行
                String firstLine = reader.readLine();

                long cacheInsertTime = Long.parseLong(firstLine);
                LogUtils.e(TAG,System.currentTimeMillis()+"~~~" + cacheInsertTime+"~~~"+timeout);

                    //有效缓存
                    String cacheData = reader.readLine();


                    //保存数据到内存中
                    saveData2Mem(imei, cacheData);

                    //解析
                    return cacheData ;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭流
            IOUtils.close(reader);
        }
        return null;
    }


    /**
     * 保存数据到内存中
     *
     * @param index
     * @param cacheData
     */
    public void saveData2Mem(int index, String cacheData) {
        //缓存的key
        String key = generateCacheKey(index);

        //存储结构
        MyApplication app = (MyApplication) UIUtils.getContext();
        Map<String, Object> cacheMap = app.getCacheMap();

        //开始存
        cacheMap.put(key, cacheData);

        LogUtils.e(TAG,"保存数据到内存中-->" + key);
    }
    /**
     * 保存数据到内存中
     *
     * @param index
     * @param cacheData
     */
    public void saveData2Mem(String index, String cacheData) {
        //缓存的key
        String key = generateCacheKey(index);

        //存储结构
        MyApplication app = (MyApplication) UIUtils.getContext();
        Map<String, Object> cacheMap = app.getCacheMap();

        //开始存
        cacheMap.put(key, cacheData);

        LogUtils.e(TAG,"保存数据到内存中-->" + key);
    }

    /**
     * 得到缓存文件
     *
     * @param index
     * @return
     */
    private File getCacheFile(int index) {
        String dir = FileUtils.getDir("cache");//sdcard/Android/data/包目录/json
        String fileName = generateCacheKey(index);//唯一(interface+"."+index)
        LogUtils.d(TAG,"fileName " + fileName);
        File cacheFile = new File(dir, fileName);
        return cacheFile;
    }
    /**
     * 得到缓存文件
     *
     * @param index
     * @return
     */
    private File getCacheFile(String index) {
        String dir = FileUtils.getDir("cache");//sdcard/Android/data/包目录/json
        String fileName = generateCacheKey(index);//唯一(interface+"."+index)
        LogUtils.d(TAG,"fileName " + fileName);
        File cacheFile = new File(dir, fileName);
        return cacheFile;
    }

    /**
     * 生成缓存的Key
     *
     * @param index
     * @return
     */
    @NonNull
    public String generateCacheKey(int index) {
        LogUtils.d(TAG,"getInterfaceKey() " +getInterfaceKey() + "." + index);

        return getInterfaceKey() + "." + index;
    }

    /**
     * 生成缓存的Key
     *
     * @param index
     * @return
     */
    @NonNull
    public String generateCacheKey(String index) {
        LogUtils.d(TAG,"getInterfaceKey() " +getInterfaceKey() + "." + index);

        return getInterfaceKey() + "." + index;
    }


    public abstract List<Album> loadDataFromNet(int index,String id);



    /**
     * @param parmasMap
     * @des 初始化请求参数对应的map的具体值
     * @des 子类可以选择性的覆写该方法, 传递不同的参数进来
     */
    public void initParmasMapData(Map<String, Object> parmasMap) {
        parmasMap.put("index", mIndex);//默认值
    }

    /**
     * 保存数据到本地
     *
     * @param index
     * @param resJson
     */
    public void saveData2Local(int index, String resJson) {
        BufferedWriter writer = null;
        try {
            File cacheFile = getCacheFile(index);

            writer = new BufferedWriter(new FileWriter(cacheFile));
            //写入第一行,当前时间,缓存的生成时间
            writer.write(System.currentTimeMillis() + "");

            //写入第二行
            writer.newLine();//换行
            writer.write(resJson);

            //打印一个日志
            LogUtils.e(TAG,"缓存数据到本地-->" + cacheFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(writer);
        }
    }


    /**
     * @return
     * @des 得到协议的关键字
     * @des 子类是必须实现, 定义成为抽象方法, 交给子类具体实现
     */
    @NonNull
    public abstract String getInterfaceKey();


    /**
     * @param resJson
     * @param gson
     * @return
     * @des gson解析请求回来的数据
     * @des 在BaseProtocol中不知道parseData()方法的具体实现, 交给子类
     */
    public List<Album> parseData(String resJson, Gson gson) {
        Type listType = new TypeToken<List<Album>>(){}.getType();
        List<Album> albun = gson.fromJson(resJson, listType);
        LogUtils.e(TAG,"base albun" + albun.size());
        return albun;
    }
}
