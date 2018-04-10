package com.loybin.baidumap.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hojy.happyfruit.R;
import com.loybin.baidumap.presenter.RecommendedsPresenter;
import com.loybin.baidumap.ui.activity.CheckMoreActivity;
import com.loybin.baidumap.ui.activity.StoryListActivity;
import com.loybin.baidumap.ui.fragment.RecommendedsFramgent;
import com.loybin.baidumap.ui.view.BlurTransformation;
import com.loybin.baidumap.util.LogUtils;
import com.loybin.baidumap.util.MyApplication;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.track.TrackList;

import java.util.Collections;
import java.util.List;

import static com.loybin.baidumap.util.UIUtils.getContext;

/**
 * 创 建 者: LoyBin
 * 创建时间: 2017/10/10 上午10:20
 * 描   述: 推荐的界面适配器
 */
public class RecommendedAdapter extends RecyclerView.Adapter {
    private static final String TAG = "RecommendedFramgent";
    private Context mContext;
    private List<Album> mAlba;
    private String mTrackList;
    private String mJsonAlbum;
    private int mPosition;
    private RecommendedsPresenter mRecommendedPresenter;
    private RecommendedsFramgent mRecommendedsFramgent;
    public static final int TYPE_PLAYHISTORY = 0;
    public static final int TYPE_SEARCHHISTPRY = 1;
    public static final int TYPE_LIKE = 2;
    public static final int TYPE_BATCH = 3;
    public static final int TYPE_CONTENT = 4;
    private Gson mGson;


    public RecommendedAdapter(MyApplication instance, List<Album> alba, String track, String jsonAlbum
            , RecommendedsPresenter recommendedPresenter, RecommendedsFramgent recommendedsFramgent, int position) {
        mContext = instance;
        mAlba = alba;
        mTrackList = track;
        mPosition = position;
        mJsonAlbum = jsonAlbum;
        mRecommendedPresenter = recommendedPresenter;
        mRecommendedsFramgent = recommendedsFramgent;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType) {
            case TYPE_PLAYHISTORY:
                try {
                    itemView = View.inflate(mContext, R.layout.item_play_history, null);
                    PlayHistory playHistory = new PlayHistory(itemView);
                    return playHistory;
                } catch (Exception e) {

                }


            case TYPE_SEARCHHISTPRY:
                itemView = View.inflate(mContext, R.layout.item_search_history, null);
                SearchHistory searchHistory = new SearchHistory(itemView);
                return searchHistory;

            case TYPE_LIKE:
                itemView = View.inflate(mContext, R.layout.item_like, null);
                Like divisionHolder = new Like(itemView);
                return divisionHolder;

            case TYPE_BATCH:
                itemView = View.inflate(mContext, R.layout.item_batch, null);
                Batch batch = new Batch(itemView);
                return batch;

            case TYPE_CONTENT:
                itemView = View.inflate(mContext, R.layout.item_content, null);
                Content content = new Content(itemView);
                return content;

            default:
                return null;
        }

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case TYPE_PLAYHISTORY:
                try {
                    PlayHistory playHistory = (PlayHistory) holder;
                    if (mTrackList == null) {
                        playHistory.setViewGone();
                    } else {
                        playHistory.setView(mTrackList, mPosition);
                    }
                } catch (Exception e) {

                }


                break;

            case TYPE_SEARCHHISTPRY:
                LogUtils.e(TAG, "刷新了!!!!!!!~~~~~~~~~");
                SearchHistory searchHistory = (SearchHistory) holder;
                if (mJsonAlbum == null || mJsonAlbum.equals("")) {
                    searchHistory.setViewGone();
                } else {
                    if (mGson == null) {
                        mGson = new Gson();
                    }
                    List<Album> list = mGson.fromJson(mJsonAlbum, new TypeToken<List<Album>>() {
                    }.getType());
                    searchHistory.successAlbum(list);
                }

                break;

            case TYPE_LIKE:
                Like like = (Like) holder;
                like.setData(mAlba);
                break;

            case TYPE_BATCH:
                Batch batch = (Batch) holder;
                break;

            case TYPE_CONTENT:
                Content content = (Content) holder;
                break;

            default:
                break;


        }
    }


    @Override
    public int getItemCount() {
        return 5;
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_PLAYHISTORY;
        } else if (position == 1) {
            return TYPE_SEARCHHISTPRY;

        } else if (position == 2) {
            return TYPE_LIKE;

        } else if (position == 3) {
            return TYPE_BATCH;

        } else if (position == 4) {
            return TYPE_CONTENT;
        }

        return TYPE_CONTENT;
    }

    public void setData(List<Album> albums) {
        mAlba = albums;
    }

    public void setJsonAlbum(String jsonAlbum) {
        mJsonAlbum = jsonAlbum;
    }


    class PlayHistory extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final LinearLayout mLlThere;
        private final TextView mTvThere;
        private final ImageView mIvMaxCover;
        private final TextView mTvInTo;
        private final TextView mTvHist;
        private final TextView mTvOne;
        private final ImageView mIvCover;
        private TrackList mTrackList;
        private String mImagUrl;
        //        private Track mTrack;
        private BlurTransformation mBlurTransformation;
        private Intent mIntent;
        private final LinearLayout mLinearLayout;

        public PlayHistory(View itemView) {
            super(itemView);
            mLlThere = (LinearLayout) itemView.findViewById(R.id.ll_there);
            mTvThere = (TextView) itemView.findViewById(R.id.tv_title);
            mIvMaxCover = (ImageView) itemView.findViewById(R.id.iv_max_cover);
            mTvInTo = (TextView) itemView.findViewById(R.id.tv_into);
            mTvHist = (TextView) itemView.findViewById(R.id.tv_title_hist);
            mTvOne = (TextView) itemView.findViewById(R.id.tv_one);
            mIvCover = (ImageView) itemView.findViewById(R.id.iv_cover);
            mLinearLayout = (LinearLayout) itemView.findViewById(R.id.ll_placeholder);

            mTvInTo.setMovementMethod(ScrollingMovementMethod.getInstance());
            //设置触摸事件
            mTvInTo.setOnTouchListener(touchListener);

        }

        /**
         * 设置触摸事件，TextView都处于ListView中，
         * 所以需要在OnTouch事件中通知父控件不拦截子控件事件
         */
        private View.OnTouchListener touchListener = new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN
                        || event.getAction() == MotionEvent.ACTION_MOVE) {
                    //按下或滑动时请求父节点不拦截子节点
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    //抬起时请求父节点拦截子节点
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                }
                return false;
            }
        };


        public void setViewGone() {

        }

        public void setView(String track, int position) {
            try {
                initListener();
                if (mGson == null) {
                    mGson = new Gson();
                }
                mTrackList = mGson.fromJson(track, TrackList.class);
                mTvHist.setVisibility(View.GONE);
                mLlThere.setEnabled(true);
                mTvThere.setEnabled(true);

                if (mTrackList != null) {
                    mLinearLayout.setVisibility(View.GONE);
                    mLlThere.setVisibility(View.VISIBLE);
                    mImagUrl = mTrackList.getCoverUrlLarge();
//                    List<Track> tracks = mTrackList.getTracks();
//                    mTrack = tracks.get(position);

                    if (mTrackList.getAlbumTitle() != null) {
                        mTvThere.setText(mTrackList.getAlbumTitle());
                        mTvThere.setTextColor(Color.WHITE);
                        mTvOne.setTextColor(Color.WHITE);
                    }
                    if (mTrackList.getAlbumIntro() != null) {
                        mTvInTo.setText(mTrackList.getAlbumIntro());
                        mTvThere.setTextColor(Color.WHITE);
                    }
                } else {
                    mLlThere.setVisibility(View.GONE);
                    mLinearLayout.setVisibility(View.VISIBLE);
                }


                if (mBlurTransformation == null) {
                    mBlurTransformation = new BlurTransformation(mContext, 100);
                }

                if (mImagUrl != null) {
                    Glide.with(mContext).load(mImagUrl).into(mIvCover);
                    Glide.with(mContext).load(mImagUrl)
                            .transform(mBlurTransformation)
                            .crossFade()
                            .into(mIvMaxCover);
                }
            } catch (Exception e) {
                LogUtils.e(TAG, "越界了" + e.getMessage());
            }

        }

        private void initListener() {
            mLlThere.setOnClickListener(this);
            mTvInTo.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_there:
                    if (mIntent == null) {
                        mIntent = new Intent(getContext(), StoryListActivity.class);
                    }

                    long albumId = mTrackList.getAlbumId();
                    mIntent.putExtra("id", albumId);
                    mIntent.putExtra("title", mTrackList.getAlbumTitle());
                    mIntent.putExtra("imageUrl", mTrackList.getCoverUrlLarge());
                    mIntent.putExtra("intro", mTrackList.getAlbumIntro());
                    mIntent.putExtra("count", mTrackList.getTotalCount() + "");
                    mRecommendedsFramgent.startActivity(mIntent);
                    break;

                case R.id.tv_into:
                    if (mIntent == null) {
                        mIntent = new Intent(getContext(), StoryListActivity.class);
                    }

                    long albumId2 = mTrackList.getAlbumId();
                    mIntent.putExtra("id", albumId2);
                    mIntent.putExtra("title", mTrackList.getAlbumTitle());
                    mIntent.putExtra("imageUrl", mTrackList.getCoverUrlLarge());
                    mIntent.putExtra("intro", mTrackList.getAlbumIntro());
                    mIntent.putExtra("count", mTrackList.getTotalCount() + "");
                    mRecommendedsFramgent.startActivity(mIntent);
                    break;


                default:
                    break;
            }
        }
    }


    class SearchHistory extends RecyclerView.ViewHolder {

        private static final java.lang.String TAG = "RecommendedFramgent";
        private final LinearLayout mLLStoryHistory;
        private final RecyclerView mListView;
        private final RelativeLayout mTvTv;
        private final TextView mTvViewMore;
        private final StoryHistoryAdapter mStoryHistoryAdapter;

        public SearchHistory(View itemView) {
            super(itemView);

            LogUtils.d(TAG, "SearchHistory");
            mLLStoryHistory = (LinearLayout) itemView.findViewById(R.id.ll_story_history);
            mListView = (RecyclerView) itemView.findViewById(R.id.lv_listview);
            mTvTv = (RelativeLayout) itemView.findViewById(R.id.tv_tv);
            mTvViewMore = (TextView) itemView.findViewById(R.id.tv_view_more);

            mStoryHistoryAdapter = new StoryHistoryAdapter(mContext, mAlba, mRecommendedsFramgent);
            mListView.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyApplication.sInstance);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            mListView.setLayoutManager(linearLayoutManager);
            mListView.setAdapter(mStoryHistoryAdapter);

            mTvViewMore.setOnClickListener(new View.OnClickListener() {
                public Intent mIntent;

                @Override
                public void onClick(View v) {
                    //查看更多点击事件
                    if (mIntent == null) {
                        mIntent = new Intent(mContext, CheckMoreActivity.class);
                    }
                    mIntent.putExtra("json", mJsonAlbum);
                    mRecommendedsFramgent.startActivity(mIntent);
                }
            });
        }

        /**
         * 搜索历史数据为空
         */
        public void setViewGone() {
            mLLStoryHistory.setVisibility(View.GONE);
            mTvTv.setVisibility(View.GONE);
        }

        /**
         * 拿到数据 显示
         *
         * @param list
         */
        public void successAlbum(List<Album> list) {
            mLLStoryHistory.setVisibility(View.VISIBLE);
            mTvTv.setVisibility(View.VISIBLE);
            Collections.reverse(list);

            mStoryHistoryAdapter.setData(list);
            mStoryHistoryAdapter.notifyDataSetChanged();
        }
    }


    class Like extends RecyclerView.ViewHolder {

        private final GridView mGdStory;
        private final ListenStoryAdapter mAdapter;

        public Like(View itemView) {
            super(itemView);
            mGdStory = (GridView) itemView.findViewById(R.id.gv_listen_story);

            mGdStory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public Intent mIntent;

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        LogUtils.e(TAG, "onItemClick" + mAlba.get(position).getId());
                        if (mIntent == null) {
                            mIntent = new Intent(getContext(), StoryListActivity.class);
                        }
                        Album album = mAlba.get(position);
                        mIntent.putExtra("id", album.getId());
                        mIntent.putExtra("title", album.getAlbumTitle());
                        mIntent.putExtra("imageUrl", album.getCoverUrlLarge());
                        mIntent.putExtra("intro", album.getAlbumIntro());
                        mIntent.putExtra("count", album.getIncludeTrackCount() + "");

                        mRecommendedsFramgent.startActivity(mIntent);
                    } catch (Exception e) {
                        LogUtils.e(TAG, "onItemClick 异常 " + e.getMessage());
                    }
                }
            });

            mAdapter = new ListenStoryAdapter(mContext, mAlba);
            mGdStory.setAdapter(mAdapter);
        }


        public void setData(List<Album> alba) {
            mAdapter.setData(alba);
            mAdapter.notifyDataSetChanged();
        }
    }


    class Batch extends RecyclerView.ViewHolder {

        private final LinearLayout mTvBatch;
        private final ImageView mIvTime;
        private final Animation mOperatingAnim;

        public Batch(View view) {
            super(view);
            mTvBatch = (LinearLayout) view.findViewById(R.id.tv_batch);
            mIvTime = (ImageView) view.findViewById(R.id.iv_time);


            mOperatingAnim = AnimationUtils.loadAnimation(MyApplication.sInstance, R.anim.tip);
            LinearInterpolator lin = new LinearInterpolator();
            mOperatingAnim.setInterpolator(lin);

            mTvBatch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mRecommendedPresenter.loadDataNet(mRecommendedsFramgent.mPage, "33");
                    if (mOperatingAnim != null) {
                        mIvTime.startAnimation(mOperatingAnim);
                    }
                }
            });


        }
    }


    class Content extends RecyclerView.ViewHolder {

        public Content(View itemView) {
            super(itemView);
        }
    }
}
