package huidu.com.voicecall.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import huidu.com.voicecall.R;
import huidu.com.voicecall.base.BaseFragment;
import huidu.com.voicecall.bean.Home;
import huidu.com.voicecall.bean.UserAttention;
import huidu.com.voicecall.http.API;
import huidu.com.voicecall.http.BaseModel;
import huidu.com.voicecall.http.OkHttpUtils;
import huidu.com.voicecall.http.RequestFinish;
import huidu.com.voicecall.mine.AnchorsSkillsActivity;
import huidu.com.voicecall.mine.MyAttentionActivity;
import huidu.com.voicecall.utils.GlideImageLoader;
import huidu.com.voicecall.utils.Loading;
import huidu.com.voicecall.utils.ToastUtil;

/**
 * Description:
 * Data：2019/1/3-16:12
 * Author: lin
 */
public class HotFragment extends BaseFragment implements RequestFinish{

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    Unbinder unbinder;
    BaseQuickAdapter mAdapter;
    List<Home.Anchor> mList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_hot;
    }

    Loading mLoading;

    @Override
    protected void initView(View view) {
        mLoading = new Loading(getActivity());
        loadStart();
        String string = getArguments().getString("type_name");
        String type_id = getArguments().getString("type_id")+"";
        tv_title.setText(string);

        OkHttpUtils.getInstance().home(API.TOKEN_TEST,type_id,"",this);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });

    }

    @Override
    public void onSuccess(BaseModel result, String params) {
        loadCancel();
        Home home = (Home)result.getData();
        List<Home.Banner> banners = home.getBanner();

        List<String> images = new ArrayList<>();
        for (Home.Banner img : banners){
            images.add(img.getImage_url());
        }
        initBanner(images);
        mList = home.getAnchor();
        mAdapter.setNewData(mList);

    }

    @Override
    public void onError(String result) {
        loadCancel();
        ToastUtil.toastShow(result);
    }

    /**
     * fragment静态传值
     */
    public static HotFragment newInstance(String type_id,String type_name) {
        HotFragment fragment = new HotFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type_id", type_id);
        bundle.putString("type_name", type_name);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initData() {
        recycleView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recycleView.setHasFixedSize(true);
        mAdapter = new BaseQuickAdapter<Home.Anchor,BaseViewHolder>(R.layout.item_author,mList) {
            @Override
            protected void convert(BaseViewHolder helper, Home.Anchor item) {
                ImageView iv_sex = helper.getView(R.id.iv_sex);
                ImageView iv_head = helper.getView(R.id.iv_head);
                helper.setText(R.id.tv_name,item.getNickname());
                helper.setText(R.id.tv_age,item.getAge());
                if (item.getSex().equals("1")){
                    iv_sex.setImageResource(R.mipmap.boy);
                }else if (item.getSex().equals("2")){
                    iv_sex.setImageResource(R.mipmap.girl);
                }
                Glide.with(getActivity()).load(item.getHead_image()).into(iv_head);
            }
        };
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), AnchorsSkillsActivity.class);
                intent.putExtra("anchor_id",mList.get(position).getAnchor_id());
                intent.putExtra("anchor_type_id",mList.get(position).getAnchor_type_id());
                startActivity(intent);
            }
        });
        recycleView.setAdapter(mAdapter);
    }

    private void loadStart() {
        if (mLoading != null)
            mLoading.show();
    }

    private void loadCancel() {
        if (mLoading != null && mLoading.isShowing())
            mLoading.dismiss();
    }

    private void initBanner(List<String> images) {
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
                Log.e(TAG, "OnBannerClick: position = "+position );
            }
        });
        //设置轮播时间
        banner.setDelayTime(2000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    @OnClick({R.id.rl_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_more:
                //跳转更多
                break;
        }

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
        banner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        //结束轮播
        banner.stopAutoPlay();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }
}
