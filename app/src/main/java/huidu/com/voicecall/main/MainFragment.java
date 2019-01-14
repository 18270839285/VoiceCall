package huidu.com.voicecall.main;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import huidu.com.voicecall.R;
import huidu.com.voicecall.base.BaseFragment;
import huidu.com.voicecall.bean.Home;
import huidu.com.voicecall.http.API;
import huidu.com.voicecall.http.BaseModel;
import huidu.com.voicecall.http.OkHttpUtils;
import huidu.com.voicecall.http.RequestFinish;
import huidu.com.voicecall.utils.Loading;
import huidu.com.voicecall.utils.ToastUtil;

/**
 * Description:首页
 * Data：2019/1/3-11:28
 * Author: lin
 */
public class MainFragment extends BaseFragment implements RequestFinish {

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    Unbinder unbinder;
    List<Home.Type> tabList = new ArrayList<>();
    List<Fragment> fragmentList = new ArrayList<>();

    Loading loading ;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initView(View view) {
        loading = new Loading(getActivity());
        loading.show();
        OkHttpUtils.getInstance().home(API.TOKEN_TEST,"","",this);

    }

    @Override
    protected void initData() {

    }
    @Override
    public void onSuccess(BaseModel result, String params) {
        loading.dismiss();
        Home home = (Home)result.getData();
        tabList = home.getType();
        initTab();
    }

    @Override
    public void onError(String result) {
        loading.dismiss();
        ToastUtil.toastShow(result);
    }

    private void initTab(){
//        tabList.add("热门推荐");
//        tabList.add("语音陪聊");
//        tabList.add("线上歌手");
//        tabList.add("哄睡觉");
//        tabList.add("逗你笑");
//        tabList.add("瞎几把搞");
//        tabList.add("不想乱搞");
        for (int i = 0; i < tabList.size(); i++) {
            HotFragment fragment = HotFragment.newInstance(tabList.get(i).getId(),tabList.get(i).getType_name());
            fragmentList.add(fragment);

        }
        //mTabLayout.setTabMode(TabLayout.SCROLL_AXIS_HORIZONTAL);//设置tab模式，当前为系统默认模式
        for (int i = 0; i < tabList.size(); i++) {
            RelativeLayout relativeLayout = (RelativeLayout)LayoutInflater.from(getActivity()).inflate(R.layout.view_titlebar,null);
            TextView textView = relativeLayout.findViewById(R.id.tv_title);
            ImageView imageView = relativeLayout.findViewById(R.id.iv_icon);
            textView.setText(tabList.get(i).getType_name());
            if (i==0){
                textView.setTextSize(18);
                imageView.setVisibility(View.VISIBLE);
                textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.textColor));
            }
            tabLayout.addTab(tabLayout.newTab().setCustomView(relativeLayout));
//            tabLayout.addTab(tabLayout.newTab().setText(tabList.get(i)));//添加tab选项
        }
        FragmentPagerAdapter mAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return tabList.get(position).getType_name();
            }

        };
        viewPager.setAdapter(mAdapter);
//        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.setTabsFromPagerAdapter(mAdapter);//给Tabs设置适配器
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                View view = tab.getCustomView();
                if (null != view && view instanceof RelativeLayout) {

                    ((TextView) ((RelativeLayout) view).getChildAt(1)).setTextSize(18);
                    ((ImageView) ((RelativeLayout) view).getChildAt(0)).setVisibility(View.VISIBLE);
                    ((TextView) ((RelativeLayout) view).getChildAt(1)).setTextColor(ContextCompat.getColor(getActivity(), R.color.textColor));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                if (null != view && view instanceof RelativeLayout) {
                    ((TextView) ((RelativeLayout) view).getChildAt(1)).setTextSize(14);
                    ((ImageView) ((RelativeLayout) view).getChildAt(0)).setVisibility(View.GONE);
                    ((TextView) ((RelativeLayout) view).getChildAt(1)).setTextColor(ContextCompat.getColor(getActivity(), R.color.textColor2));
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
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
}
