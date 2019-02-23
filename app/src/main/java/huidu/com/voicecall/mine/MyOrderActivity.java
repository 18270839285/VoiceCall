package huidu.com.voicecall.mine;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import huidu.com.voicecall.R;
import huidu.com.voicecall.base.BaseActivity;
import huidu.com.voicecall.http.BaseModel;
import huidu.com.voicecall.http.RequestFinish;
import huidu.com.voicecall.login.LoginActivity;
import huidu.com.voicecall.main.HotFragment;
import huidu.com.voicecall.utils.ToastUtil;

/**
 * 我的订单
 */
public class MyOrderActivity extends BaseActivity implements RequestFinish{
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tv_title)
    TextView tv_title;

    List<String> typeList;
    List<Fragment> fragmentList = new ArrayList<>();
    int JPushType;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_order;
    }

    @Override
    protected void initView() {
        tv_title.setText("我的订单");
        typeList = new ArrayList<>();
        typeList.add("已下单");
        typeList.add("已接单");

        JPushType = getIntent().getIntExtra("JPushType",0);

    }

    @Override
    protected void initData() {
            MyOrderFragment fragment1 = MyOrderFragment.newInstance((2+""));
            fragmentList.add(fragment1);
            MyOrderFragment fragment2 = MyOrderFragment.newInstance((1+""));
            fragmentList.add(fragment2);


        FragmentPagerAdapter mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
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
                return typeList.get(position);
            }

        };
        viewPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabsFromPagerAdapter(mAdapter);//给Tabs设置适配器
        viewPager.setCurrentItem(JPushType);
    }

    @Override
    public void onSuccess(BaseModel result, String params) {

    }

    @Override
    public void onError(String result) {
        ToastUtil.toastShow(result);
    }
    @OnClick({R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }
}
