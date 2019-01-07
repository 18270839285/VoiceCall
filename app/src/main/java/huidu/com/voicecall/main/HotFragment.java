package huidu.com.voicecall.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import huidu.com.voicecall.R;
import huidu.com.voicecall.base.BaseFragment;
import huidu.com.voicecall.utils.GlideImageLoader;
import huidu.com.voicecall.utils.Loading;

/**
 * Description:
 * Data：2019/1/3-16:12
 * Author: lin
 */
public class HotFragment extends BaseFragment {
    @BindView(R.id.textView1)
    TextView textView1;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.banner)
    Banner banner;
    Unbinder unbinder;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_hot;
    }

    Loading mLoading;

    @Override
    protected void initView(View view) {
        mLoading = new Loading(getActivity());
        String string = getArguments().getString("type");
        tv_title.setText(string);
        List<String> images = new ArrayList<>();
        images.add("http://t2.hddhhn.com/uploads/tu/201901/30/4.jpg");
        images.add("http://t2.hddhhn.com/uploads/tu/201901/30/1.jpg");
        initBanner(images);
    }

    /**
     * fragment静态传值
     */
    public static HotFragment newInstance(String str) {
        HotFragment fragment = new HotFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", str);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initData() {
        // 添加Loading

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
