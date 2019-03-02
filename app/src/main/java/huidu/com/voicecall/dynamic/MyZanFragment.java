package huidu.com.voicecall.dynamic;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import huidu.com.voicecall.R;
import huidu.com.voicecall.VoiceApp;
import huidu.com.voicecall.base.BaseFragment;
import huidu.com.voicecall.bean.DynamicData;
import huidu.com.voicecall.http.BaseModel;
import huidu.com.voicecall.http.OkHttpUtils;
import huidu.com.voicecall.http.RequestFinish;
import huidu.com.voicecall.utils.CustomLLManager;
import huidu.com.voicecall.utils.EmptyViewUtil;
import huidu.com.voicecall.utils.Loading;
import huidu.com.voicecall.utils.SPUtils;
import huidu.com.voicecall.utils.TimeCountUtil4;
import huidu.com.voicecall.utils.ToastUtil;

/**
 * Description:
 * Data：2019/2/21-16:52
 * Author: lin
 */
public class MyZanFragment extends BaseFragment implements RequestFinish {
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.ll_voice_show)
    LinearLayout ll_voice_show;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.iv_sex)
    ImageView iv_sex;
    @BindView(R.id.iv_pause)
    ImageView iv_pause;

    Unbinder unbinder;
    BaseQuickAdapter mAdapter;
    List<DynamicData.DynamicList> mList = new ArrayList<>();

//    int mPage = 1;

    CustomLLManager llManager;

    private MediaPlayer mediaPlayer;
    private TimeCountUtil4 mTimeCount;

    private int ITEM_POSITION = -1;
    private int PLAY_POSITION = -1;

    boolean isPause = false;
    boolean isPlay = false;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_dynamic;
    }

    @Override
    protected void initView(View view) {
        llManager = new CustomLLManager(getActivity());
        OkHttpUtils.getInstance().dynamic_my_like(SPUtils.getValue("token"), this);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    private void refresh(){
        llManager.setScrollEnabled(false);
//        mPage = 1;
        stopPlay();
        mList.clear();
        OkHttpUtils.getInstance().dynamic_my_like(SPUtils.getValue("token"), new RequestFinish() {
            @Override
            public void onSuccess(BaseModel result, String params) {
                refreshLayout.setRefreshing(false);
                llManager.setScrollEnabled(true);
//                mPage++;
                DynamicData orderList = (DynamicData) result.getData();
                mList = orderList.getList();
                mAdapter.setNewData(mList);
            }

            @Override
            public void onError(String result) {
                refreshLayout.setRefreshing(false);
                llManager.setScrollEnabled(true);
                ToastUtil.toastShow(result);
            }
        });
    }
    @Override
    protected void initData() {
        recycleView.setLayoutManager(llManager);
        recycleView.setHasFixedSize(true);
        mAdapter = new BaseQuickAdapter<DynamicData.DynamicList, BaseViewHolder>(R.layout.item_dynamic, mList) {
            @Override
            protected void convert(final BaseViewHolder helper, final DynamicData.DynamicList item) {
                CircleImageView iv_head = helper.getView(R.id.iv_head);
                final ImageView iv_zan = helper.getView(R.id.iv_zan);
                final TextView tv_num = helper.getView(R.id.tv_num);
                final TextView tv_content = helper.getView(R.id.tv_content);
                final TextView tv_more = helper.getView(R.id.tv_more);
                ImageView iv_sex = helper.getView(R.id.iv_sex);
                final LinearLayout ll_voice = helper.getView(R.id.ll_voice);
                final ImageView iv_image_gif = helper.getView(R.id.iv_image_gif);
                final TextView tv_music_time = helper.getView(R.id.tv_music_time);
                RecyclerView item_recycleView = helper.getView(R.id.item_recycleView);
                helper.setText(R.id.tv_nickName, item.getNickname());
                helper.setText(R.id.tv_time, item.getCreated_at());
                final ImageView iv_dialog = helper.getView(R.id.iv_dialog);
                iv_dialog.setVisibility(View.GONE);
                tv_content.setText(item.getContent());
                tv_num.setText(item.getLike_count()+"");

                if (item.getAudio() == null || item.getAudio().isEmpty()) {
                    ll_voice.setVisibility(View.GONE);
                    item_recycleView.setVisibility(View.VISIBLE);
                } else {
                    ll_voice.setVisibility(View.VISIBLE);
                    if (item.getAudio_time() != null && !item.getAudio_time().isEmpty()) {
                        tv_music_time.setText((Integer.parseInt(item.getAudio_time()) ) / 1000 + "s");
                    }
                    item_recycleView.setVisibility(View.GONE);
                    if (helper.getAdapterPosition() == ITEM_POSITION) {
                        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                            Glide.with(getActivity()).load(R.mipmap.bofangdh).into(iv_image_gif);
                            if (isPause){
                                isPause = false;
                                mTimeCount = new TimeCountUtil4(Integer.parseInt(item.getAudio_time()) , 1000, tv_music_time);
                                mTimeCount.start();
                            }
                        } else {
                            Glide.with(getActivity()).load(R.mipmap.yystop).into(iv_image_gif);

                        }
                    } else {
                        Glide.with(getActivity()).load(R.mipmap.yystop).into(iv_image_gif);
                    }

                    if (isPlay&&PLAY_POSITION == helper.getAdapterPosition()){
                        isPlay = false;
                        Glide.with(getActivity()).load(R.mipmap.bofangdh).into(iv_image_gif);
                        mTimeCount = new TimeCountUtil4(Integer.parseInt(item.getAudio_time()) , 1000, tv_music_time);
                        mTimeCount.start();
                    }else if(PLAY_POSITION != helper.getAdapterPosition()){
                        Glide.with(getActivity()).load(R.mipmap.yystop).into(iv_image_gif);
                        tv_music_time.setText((Integer.parseInt(item.getAudio_time()) ) / 1000 + "s");
                    }
                    if (isStop&&PLAY_POSITION == helper.getAdapterPosition()){
                        isStop = false;
                        Glide.with(getActivity()).load(R.mipmap.yystop).into(iv_image_gif);
                        tv_music_time.setText((Integer.parseInt(item.getAudio_time())) / 1000 + "s");
                    }
                }

                if (item.getIs_my().equals("1")){
                    iv_zan.setImageResource(R.mipmap.zan_pre);
                }else {
                    iv_zan.setImageResource(R.mipmap.zan);
                }
                if (item.getSex().equals("1")){
                    iv_sex.setImageResource(R.mipmap.dt_boy);
                }else {
                    iv_sex.setImageResource(R.mipmap.girl1);
                }
                Glide.with(getActivity()).load(item.getHead_image()).into(iv_head);

                tv_content.post(new Runnable() {
                    @Override
                    public void run() {
                        if (tv_content.getLineCount() > 5) {//判断TextView有没有超过5行
                            tv_content.setMaxLines(5);//超过5行就设置只能显示5行
                            tv_more.setVisibility(View.VISIBLE);
                        } else {
                            tv_more.setVisibility(View.GONE);
                        }
                    }
                });

                tv_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tv_content.getMaxLines() > 5) {
                            tv_content.setMaxLines(5);
                            tv_more.setText("全文");
                        } else {
                            tv_content.setMaxLines(Integer.MAX_VALUE);
                            tv_more.setText("收起");
                        }
                    }
                });

                iv_zan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mLoading.show();
                        OkHttpUtils.getInstance().dynamic_like(SPUtils.getValue("token"), item.getDynamic_id(), new RequestFinish() {
                            @Override
                            public void onSuccess(BaseModel result, String params) {
                                finishLoad();
                                if (item.getIs_my().equals("1")){
                                    ToastUtil.toastShow("取消点赞成功");
                                    item.setIs_my("2");
                                    iv_zan.setImageResource(R.mipmap.zan);
                                    item.setLike_count((Integer.parseInt(item.getLike_count())-1)+"");
                                }else {
                                    ToastUtil.toastShow("点赞成功");
                                    item.setIs_my("1");
                                    iv_zan.setImageResource(R.mipmap.zan_pre);
                                    item.setLike_count((Integer.parseInt(item.getLike_count())+1)+"");
                                }
                                mList.remove(helper.getAdapterPosition());
                                VoiceApp.needRefresh1 = true;
                                mAdapter.notifyDataSetChanged();
                                tv_num.setText(item.getLike_count());
                            }

                            @Override
                            public void onError(String result) {
                                finishLoad();
                            }
                        });
                    }
                });
                ll_voice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ITEM_POSITION == helper.getAdapterPosition()) {
                            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                                if (mTimeCount != null) {
                                    mTimeCount.cancel();
                                    mTimeCount = null;
                                }
                                iv_pause.setImageResource(R.mipmap.dt_zt);
                                mediaPlayer.pause();
                            } else {
                                if (mediaPlayer != null) {
                                    if (mTimeCount != null) {
                                        mTimeCount.cancel();
                                        mTimeCount = null;
                                    }
                                    showHead(helper.getAdapterPosition());
                                    play(item.getAudio());
                                    mTimeCount = new TimeCountUtil4(Integer.parseInt(item.getAudio().isEmpty() ? "0" : item.getAudio_time()) , 1000, tv_music_time);
//                                    }
                                    iv_pause.setImageResource(R.mipmap.dt_bf);
                                    mediaPlayer.start();
                                    mTimeCount.start();
                                } else {
                                    showHead(helper.getAdapterPosition());
                                    play(item.getAudio());
                                    if (mTimeCount != null) {
                                        mTimeCount.cancel();
                                        mTimeCount = null;
                                    }
                                    mTimeCount = new TimeCountUtil4(Integer.parseInt(item.getAudio().isEmpty() ? "0" : item.getAudio_time()) , 1000, tv_music_time);
                                    iv_pause.setImageResource(R.mipmap.dt_bf);
                                    mTimeCount.start();
                                    mediaPlayer.start();
                                }
                            }
                        } else {
                            showHead(helper.getAdapterPosition());
                            play(item.getAudio());
                            if (mTimeCount != null) {
                                mTimeCount.cancel();
                                mTimeCount = null;
                            }
                            mTimeCount = new TimeCountUtil4(Integer.parseInt(item.getAudio().isEmpty() ? "0" : item.getAudio_time()) , 1000, tv_music_time);
                            iv_pause.setImageResource(R.mipmap.dt_bf);
                            mTimeCount.start();
                            mediaPlayer.start();
                        }
                        ITEM_POSITION = helper.getAdapterPosition();
                        PLAY_POSITION = ITEM_POSITION;
                        notifyDataSetChanged();

                    }
                });

                item_recycleView.setLayoutManager(new GridLayoutManager(getActivity(),3));
                item_recycleView.setHasFixedSize(true);
                final List<String> imageList = item.getImage();
                BaseQuickAdapter adapter = new BaseQuickAdapter<String ,BaseViewHolder>(R.layout.item_dynamic_photo,imageList) {
                    @Override
                    protected void convert(final BaseViewHolder helper, final String item) {
                        final ImageView imageView = helper.getView(R.id.iv_photo);
                        Glide.with(getActivity()).load(item).into(imageView);

                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (Build.VERSION.SDK_INT >= 21) {
                                    Intent intent = new Intent(getActivity(), PhotoViewActivity.class);
//                                    Intent intent = new Intent(getActivity(), PictureActivity.class);
                                    intent.putExtra("imgUrlList", (Serializable) imageList);
                                    intent.putExtra("position", helper.getAdapterPosition());
                                    intent.putExtra("imageUrl", item);
                                    ActivityOptionsCompat options = ActivityOptionsCompat.
                                            makeSceneTransitionAnimation(getActivity(), imageView, "voicecall");
                                    startActivity(intent, options.toBundle());
                                }
                            }
                        });
                    }
                };
                item_recycleView.setAdapter(adapter);
            }
        };
        mAdapter.setEmptyView(EmptyViewUtil.getEmptyView(getActivity(), 5));
        recycleView.setAdapter(mAdapter);
    }

    @Override
    public void onSuccess(BaseModel result, String params) {
        if (refreshLayout != null && refreshLayout.isRefreshing())
            refreshLayout.setRefreshing(false);
        DynamicData orderList = (DynamicData) result.getData();
        mList = orderList.getList();
        mAdapter.setNewData(mList);
    }

    @Override
    public void onError(String result) {
        if (refreshLayout != null && refreshLayout.isRefreshing())
            refreshLayout.setRefreshing(false);
        ToastUtil.toastShow(result);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            if (VoiceApp.needRefresh2){
                refreshLayout.setRefreshing(true);
                refresh();
                VoiceApp.needRefresh2 = false;
            }
        }
    }

    @OnClick({R.id.iv_next, R.id.iv_pause, R.id.iv_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_next:
                //下一曲
                nextPlay();
                break;
            case R.id.iv_pause:
                //暂停
                pausePlay();
                break;
            case R.id.iv_close:
                //关闭播放
                stopPlay();
                break;
        }
    }

    boolean isStop = false;

    /**
     * 判断是否有下一首
     * @param position
     * @return
     */
    private int nextVoice(int position) {
        for (int i = position + 1; i < mList.size(); i++) {
            if (!mList.get(i).getAudio().isEmpty()) {
                return i;
            }
        }
        return PLAY_POSITION;
    }

    /**
     * 下一首
     */
    private void nextPlay(){
        PLAY_POSITION = nextVoice(ITEM_POSITION);
        if (PLAY_POSITION!=ITEM_POSITION){
            showHead(PLAY_POSITION);
            isPause = true;
            play(mList.get(PLAY_POSITION).getAudio());
            ITEM_POSITION = PLAY_POSITION;
            if (mTimeCount != null) {
                mTimeCount.cancel();
                mTimeCount = null;
            }

            mediaPlayer.start();
            mAdapter.notifyDataSetChanged();
        }else {
            ToastUtil.toastShow("已经是最后一首了!");
        }
    }
    /**
     * 停止播放
     */
    private void pausePlay() {
        if (mTimeCount != null) {
            mTimeCount.cancel();
            mTimeCount = null;
        }
        if(mediaPlayer!=null&&mediaPlayer.isPlaying()){
            iv_pause.setImageResource(R.mipmap.dt_zt);
            mediaPlayer.stop();
        }else {
            isPlay = true;
            iv_pause.setImageResource(R.mipmap.dt_bf);
            play(mList.get(ITEM_POSITION).getAudio());
            mediaPlayer.start();
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 停止播放所有
     */
    private void stopPlay() {
        ll_voice_show.setVisibility(View.GONE);
        if(mediaPlayer!=null){
            mediaPlayer.reset();
        }
        if (mTimeCount != null) {
            mTimeCount.cancel();
            mTimeCount = null;
        }
        isStop = true;
        mAdapter.notifyDataSetChanged();
    }

    private void play(String audioUrl) {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.reset();
            } else {
                mediaPlayer = new MediaPlayer();
            }
            // 设置音乐播放源
            mediaPlayer.setDataSource(audioUrl);
            // 准备
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer media) {
                    mediaPlayer.stop();
                    iv_pause.setImageResource(R.mipmap.dt_zt);
                    mAdapter.notifyDataSetChanged();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }

    private void showHead(int position){
        ll_voice_show.setVisibility(View.VISIBLE);
        tv_name.setText(mList.get(position).getNickname());
        String sex = mList.get(position).getSex();
        if (sex.equals("1")){
            iv_sex.setImageResource(R.mipmap.dt_boy);
        }else {
            iv_sex.setImageResource(R.mipmap.girl1);
        }
    }
}
