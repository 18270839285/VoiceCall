package huidu.com.voicecall.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import huidu.com.voicecall.R;
import huidu.com.voicecall.VoiceApp;
import huidu.com.voicecall.base.BaseFragment;
import huidu.com.voicecall.base.WebActivity;
import huidu.com.voicecall.bean.Home;
import huidu.com.voicecall.http.API;
import huidu.com.voicecall.http.BaseModel;
import huidu.com.voicecall.http.OkHttpUtils;
import huidu.com.voicecall.http.RequestFinish;
import huidu.com.voicecall.mine.AnchorsSkillsActivity;
import huidu.com.voicecall.utils.CustomGLManager;
import huidu.com.voicecall.utils.GlideImageLoader;
import huidu.com.voicecall.utils.Loading;
import huidu.com.voicecall.utils.RefreshEvent;
import huidu.com.voicecall.utils.SPUtils;
import huidu.com.voicecall.utils.ToastUtil;

/**
 * Description:
 * Data：2019/1/3-16:12
 * Author: lin
 */
public class HotFragment extends BaseFragment implements RequestFinish {

    //    @BindView(R.id.tv_title)
//    TextView tv_title;
//    @BindView(R.id.banner)
//    Banner banner;
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    //    @BindView(R.id.iv_rec)
//    ImageView iv_rec;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    Unbinder unbinder;
    BaseQuickAdapter mAdapter;
    List<Home.Anchor> mList = new ArrayList<>();

    private String type_id;
    private int mPage = 1;

    CustomGLManager glManager;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_hot;
    }

    @Override
    protected void initView(View view) {
        glManager = new CustomGLManager(getActivity(),2);
//        String string = getArguments().getString("type_name");
        type_id = getArguments().getString("type_id") + "";
//        tv_title.setText(string);
        mPage = 1;
        EventBus.getDefault().register(this);
        loadStart();
        OkHttpUtils.getInstance().home(SPUtils.getValue("token"), type_id, mPage + "", this);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                glManager.setScrollEnabled(false);
                mPage = 1;
                mList.clear();
                OkHttpUtils.getInstance().home(SPUtils.getValue("token"), type_id, mPage + "", new RequestFinish() {
                    @Override
                    public void onSuccess(BaseModel result, String params) {
                        mPage++;
                        refreshLayout.setRefreshing(false);
                        glManager.setScrollEnabled(true);
                        Home home = (Home) result.getData();
                        List<Home.Banner> banners = home.getBanner();

//                        List<String> images = new ArrayList<>();
//                        for (Home.Banner img : banners) {
//                            images.add(img.getImage_url());
//                        }
                        if (type_id.equals("0")) {
                            initBanner(banners, home.getType_image().getRec_img());
                        } else {
                            initBanner(banners, home.getType_image().getType_img());
                        }
                        mList = home.getAnchor();
                        mAdapter.setNewData(mList);
                    }

                    @Override
                    public void onError(String result) {
                        refreshLayout.setRefreshing(false);
                        glManager.setScrollEnabled(true);
                        ToastUtil.toastShow(result);
                    }
                });
            }
        });

    }

    @Override
    public void onSuccess(BaseModel result, String params) {
        loadCancel();
        refreshLayout.setRefreshing(false);
        mPage++;
        Home home = (Home) result.getData();
        List<Home.Banner> banners = home.getBanner();
//        List<String> images = new ArrayList<>();
//        for (Home.Banner img : banners) {
//            images.add(img.getImage_url());
//        }
        Home.TypeImage typeImg = home.getType_image();
        if (type_id.equals("0")) {
            initBanner(banners, typeImg.getRec_img());
        } else {
            initBanner(banners, typeImg.getType_img());
        }

        if (mPage==2){
//            mList.clear();
            mList = home.getAnchor();
            mAdapter.setNewData(mList);
        }else {
            mAdapter.addData(home.getAnchor());
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onError(String result) {
        loadCancel();
        if (refreshLayout != null && refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
        ToastUtil.toastShow(result);
    }

    /**
     * fragment静态传值
     */
    public static HotFragment newInstance(String type_id, String type_name) {
        HotFragment fragment = new HotFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type_id", type_id);
        bundle.putString("type_name", type_name);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initData() {
//        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
//        layoutManager.setSmoothScrollbarEnabled(true);
        recycleView.setLayoutManager(glManager);
        recycleView.setHasFixedSize(true);
        recycleView.setNestedScrollingEnabled(false);
        mAdapter = new BaseQuickAdapter<Home.Anchor, BaseViewHolder>(R.layout.item_author, mList) {
            @Override
            protected void convert(BaseViewHolder helper, Home.Anchor item) {
                ImageView iv_sex = helper.getView(R.id.iv_sex);
                ImageView iv_head = helper.getView(R.id.iv_head);
                LinearLayout ll_sex_age = helper.getView(R.id.ll_sex_age);
                helper.setText(R.id.tv_name, item.getNickname());
                helper.setText(R.id.tv_age, item.getAge());
                if (item.getSex().equals("1")) {
                    ll_sex_age.setBackgroundResource(R.drawable.shape_corner5_boy);
                    iv_sex.setImageResource(R.mipmap.boy);
                } else if (item.getSex().equals("2")) {
                    ll_sex_age.setBackgroundResource(R.drawable.shape_corner5_red);
                    iv_sex.setImageResource(R.mipmap.girl);
                }
                //设置图片圆角角度
                RoundedCorners roundedCorners = new RoundedCorners(10);
                //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
                RequestOptions options = RequestOptions.bitmapTransform(roundedCorners).override(300, 300);

                Glide.with(getActivity()).load(item.getCover()).apply(options).into(iv_head);

            }
        };
        mAdapter.openLoadAnimation();

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                OkHttpUtils.getInstance().home(SPUtils.getValue("token"), type_id, mPage + "", new RequestFinish() {
                    @Override
                    public void onSuccess(BaseModel result, String params) {
                        mAdapter.loadMoreComplete();
                        Home home = (Home) result.getData();
                        if (home.getAnchor().isEmpty()) {
                            mAdapter.loadMoreEnd();
//                            ToastUtil.toastShow("暂无更多数据");
                        } else {
                            mPage++;
                            mAdapter.addData(home.getAnchor());
                        }
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(String result) {
                        mAdapter.loadMoreComplete();
                        ToastUtil.toastShow(result);
                    }
                });
            }
        }, recycleView);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), AnchorsSkillsActivity.class);
                intent.putExtra("anchor_id", mList.get(position).getAnchor_id());
                intent.putExtra("anchor_type_id", mList.get(position).getAnchor_type_id());
                boolean is_robot = false;
                if (mList.get(position).getIs_robot() != null && mList.get(position).getIs_robot().equals("1")) {
                    is_robot = true;
                }
                intent.putExtra("isRobot", is_robot);
                startActivity(intent);
            }
        });
        mAdapter.addHeaderView(getHeadView());
        recycleView.setAdapter(mAdapter);

        recycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                refreshLayout.setEnabled(topRowVerticalPosition >= 0 && recyclerView != null && !recyclerView.canScrollVertically(-1));

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    private void loadStart() {
        if (refreshLayout!=null){
            refreshLayout.setRefreshing(true);
        }
    }

    private void loadCancel() {
        if (refreshLayout!=null){
            refreshLayout.setRefreshing(false);
        }
    }

    Banner banner;
    ImageView iv_rec;

    private View getHeadView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_hot_head, null);
        banner = (Banner) view.findViewById(R.id.banner);
        iv_rec = (ImageView) view.findViewById(R.id.iv_rec);
        return view;
    }

    private void initBanner(final List<Home.Banner> banners, String imageUrl) {
        List<String> images = new ArrayList<>();
        for (Home.Banner img : banners) {
            images.add(img.getImage_url());
        }
        Glide.with(getActivity()).load(imageUrl).into(iv_rec);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
//        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(images);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
//        banner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Log.e(TAG, "OnBannerClick: position = " + position);
                if (banners.get(position).getUrl()!=null&&banners.get(position).getUrl().length()>8){
                    Intent intent = new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("web_type",9);
                    intent.putExtra("web_url",banners.get(position).getUrl());
                    startActivity(intent);
                }
            }
        });
        //设置轮播时间
        banner.setDelayTime(2000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onStart() {
        super.onStart();
        //开始轮播
        if (banner != null)
            banner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        //结束轮播
        banner.stopAutoPlay();
        mPage = 1;
        if(EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshEvent(RefreshEvent event) {
        /* Do something */
        Log.e(TAG, "onRefreshEvent:+"+event.getMessage()+" type = "+type_id );
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

}
