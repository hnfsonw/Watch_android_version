package com.loybin.baidumap.factory;


import com.loybin.baidumap.base.BaseFragment;
import com.loybin.baidumap.ui.fragment.ChildrenSongFramgent;
import com.loybin.baidumap.ui.fragment.EnglishEnlightenmentFramgent;
import com.loybin.baidumap.ui.fragment.ParentsSchoolsFramgent;
import com.loybin.baidumap.ui.fragment.PoetryFramgent;
import com.loybin.baidumap.ui.fragment.PopularScienceFramgent;
import com.loybin.baidumap.ui.fragment.RecommendedsFramgent;
import com.loybin.baidumap.ui.fragment.StoryBooksFramgent;

import java.util.HashMap;
import java.util.Map;

/**
 * 类    名:  FragmentFactory
 * 创 建 者:  LogBin
 * 创建时间:  2017/9/7 3:19
 * 描    述： 封装Fragment的创建过程
 */
public class FragmentFactory {

    public static final int FRAGMENT_HOME                    = 0;//推荐
    public static final int FRAGMENT_STORY_BOOKS             = 1;//故事绘本
    public static final int FRAGMENT_ENGLISH_ENLIGHTENMENT   = 2;//英语启蒙
    public static final int FRAGMENT_CHILDREN_SONG           = 3;//儿歌大全
    public static final int FRAGMENT_POETRY                  = 4;//国学诗词
    public static final int FRAGMENT_PARENTS_SCHOOLS         = 5;//亲子学堂
    public static final int FRAGMENT_POPULAR_SCIENCE         = 6;//科普知识

    public static Map<Integer, BaseFragment> mCacheFragmentMap = new HashMap<>();

    public static BaseFragment createFragment(int position) {
        BaseFragment fragment = null;
        //优先从集合中取出来
        if (mCacheFragmentMap.containsKey(position)) {
            return mCacheFragmentMap.get(position);
        }

        switch (position) {
            case FRAGMENT_HOME:
                fragment = new RecommendedsFramgent();
                break;

            case FRAGMENT_STORY_BOOKS:
                fragment = new StoryBooksFramgent();
                break;

            case FRAGMENT_ENGLISH_ENLIGHTENMENT:
                fragment = new EnglishEnlightenmentFramgent();
                break;

            case FRAGMENT_CHILDREN_SONG:
                fragment = new ChildrenSongFramgent();
                break;

            case FRAGMENT_POETRY:
                fragment = new PoetryFramgent();
                break;

            case FRAGMENT_PARENTS_SCHOOLS:
                fragment = new ParentsSchoolsFramgent();
                break;

            case FRAGMENT_POPULAR_SCIENCE:
                fragment = new PopularScienceFramgent();
                break;

            default:
                break;
        }
        //保存fragment到集合中
        mCacheFragmentMap.put(position, fragment);

        return fragment;
    }
}
