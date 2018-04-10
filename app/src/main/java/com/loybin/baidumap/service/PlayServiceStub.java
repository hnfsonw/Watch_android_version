package com.loybin.baidumap.service;

import android.os.Binder;

import com.ximalaya.ting.android.opensdk.model.track.TrackList;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/08/28 上午10:18
 * 描   述: 音乐播放器服务的桥梁
 */
public class PlayServiceStub extends Binder{

    private final PlayService mService;

    public PlayServiceStub(PlayService service) {
        mService = service;
    }

    public void playList(TrackList list, int position) {
        mService.playList(list,position);
    }

    public String getTitle() {
        return mService.getTitle();
    }

    public void playNext() {
        mService.playNext();
    }

    public void playPre() {
        mService.playPre();
    }

    public boolean getIsPlaying() {
        return mService.getIsPlaying();
    }


    public void play() {
        mService.play();
    }

    public void pause() {
        mService.pause();
    }

    public void seekToByPercent(float v) {
        mService.seekToByPercent(v);
    }

    public void setModelState(int playModelState) {
        mService.setModelState(playModelState);
    }


    public String getImagurl() {
        return mService.getImagurl();
    }
}
