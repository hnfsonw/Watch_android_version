package com.hyphenate.easeui.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.controller.EaseUI.EaseUserProfileProvider;
import com.hyphenate.easeui.domain.EaseUser;

public class EaseUserUtils {

    private static final String TAG = "ChatActivity";
    static EaseUserProfileProvider userProvider;
    
    static {
        userProvider = EaseUI.getInstance().getUserProfileProvider();
    }


    public final static String [] mTitles = {
            "爸爸","妈妈","爷爷","奶奶",
            "外公","外婆","伯父","伯母",
            "叔叔","阿姨","哥哥","姐姐",
            "自定义"
    };


    public final static int [] mIcons = {
            R.drawable.fathers,R.drawable.mothers,R.drawable.grandfathers,R.drawable.grandmas,
            R.drawable.grandpas,R.drawable.grandmothers,R.drawable.nuncles,R.drawable.aunts,
            R.drawable.uncles,R.drawable.aunties,R.drawable.elder_brothers,R.drawable.elder_sisters,
            R.drawable.a
    };
    
    /**
     * get EaseUser according username
     * @param username
     * @return
     */
    public static EaseUser getUserInfo(String username){
        Log.e(TAG,"getUserInfo1111 "+ username);
        if(userProvider != null){
            EaseUser user = userProvider.getUser(username);
            if (user != null){
            Log.d(TAG, "getUserInfo: user" + user);
            Log.d(TAG, "getUserInfo: getNick" + user.getNick());
            Log.d(TAG, "getUserInfo: getNickname" + user.getNickname());
            Log.d(TAG, "getUserInfo:getUsername " + user.getUsername());

            }else {
                Log.e(TAG,"getUserInfo user是null");
            }
            return user;
        }

        return null;
    }
    
    /**
     * set user avatar
     * @param username
     */
    public static void setUserAvatar(final Context context, String username, final ImageView imageView){
    	EaseUser user = getUserInfo(username);
        if(user != null && user.getAvatar() != null){
            try {
                Log.e(TAG, "图片 url!: " + user.getAvatar());
                Log.e(TAG, "username:  " + username);
                Log.e(TAG, "性别 ::getInitialLetter: " + user.getInitialLetter());
                Log.d(TAG, "getNickname~~~: " +user.getNickname()+Thread.currentThread().getName());
                String nick = user.getNickname();
                for (int i = 0; i < mTitles.length; i++) {
                    Log.e(TAG,"如果相同设置他的头像"+user+"~~~~"+mTitles[i]);
                    Log.e(TAG,"如果相同设置他的头像"+nick.equals(mTitles[i]));
                    if (nick.equals(mTitles[i])) {
                        Log.d(TAG, "1111111111mTitles[i]: " + mTitles[i]);
                        imageView.setImageResource(mIcons[i]);
                        return;
                    }
                }
                Log.d(TAG,"设置url : setUserAvatar"+user.getAvatar());


                Glide.with(context).load(user.getAvatar()).asBitmap().centerCrop().into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });
                Log.d(TAG,"设置URL 图片 ~~~");
//                int avatarResId = Integer.parseInt(user.getAvatar());
            } catch (Exception e) {
                Log.e(TAG,"设置头像异常"+e.getMessage());
                Glide.with(context).load(R.drawable.other3xs).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.other3xs).into(imageView);
            }
        }else{
                if (user == null){
                    return;
                }
            String nick = user.getNick();
            Log.d(TAG, "user.getNick()!!!: " +user.getNick());
            Log.d(TAG, "user.getAvatar()!!!: " +user.getAvatar());
            Log.d(TAG, "user.getNickname()!!!: " +user.getNickname());
            Log.d(TAG, "user.getNick()!!!: " +user.getUsername());
            for (int i = 0; i < mTitles.length; i++) {
                if (nick.equals(mTitles[i])) {
                    Log.d(TAG, "22222222mTitles[i]: " + mTitles[i]);
                    imageView.setImageResource(mIcons[i]);
                    return;
                }
            }

            if (user.getAvatar() == null && user.getInitialLetter() != null) {
                if (user.getInitialLetter().equals("1")) {
                    imageView.setImageResource(R.drawable.a);
                    return;
                } else {
                    imageView.setImageResource(R.drawable.b);
                    return;
                }
            }
//            Glide.with(context).load(R.drawable.the_default).into(imageView);
        }
    }
    
    /**
     * set user's nickname
     */
    public static void setUserNick(String username,TextView textView){
        if(textView != null){
        	EaseUser user = getUserInfo(username);
        	if(user != null && user.getNick() != null){
        		textView.setText(user.getNick());
        	}else{
        		textView.setText(username);
        	}
        }
    }
    
}
