package com.loybin.baidumap.model;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/09/08 上午11:48
 * 描   述: Loaded 状态
 */
public class LoadedResultModel {


    private int state;

    private boolean isDestroy;

    public boolean isDestroy() {
        return isDestroy;
    }

    public void setDestroy(boolean destroy) {
        isDestroy = destroy;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public static final int STATE_LOADING = 0;//加载中
    public static final int STATE_SUCCESS = 1;//成功
    public static final int STATE_EMPTY = 2;//空
    public static final int STATE_ERROR = 3;//错误

    public enum LoadedResult {
        SUCCESS(STATE_SUCCESS), EMPTY(STATE_EMPTY), ERROR(STATE_ERROR);

        public int state;

        public int getState() {
            return state;
        }

        LoadedResult(int state) {
            this.state = state;
        }
    }

    public static int getStateLoading() {
        return STATE_LOADING;
    }

    public static int getStateSuccess() {
        return STATE_SUCCESS;
    }

    public static int getStateEmpty() {
        return STATE_EMPTY;
    }

    public static int getStateError() {
        return STATE_ERROR;
    }
}
