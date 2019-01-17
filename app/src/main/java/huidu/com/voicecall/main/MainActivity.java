package huidu.com.voicecall.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import huidu.com.voicecall.R;
import huidu.com.voicecall.base.BaseActivity;
import huidu.com.voicecall.utils.AtyContainer;
import huidu.com.voicecall.utils.Loading;
import huidu.com.voicecall.utils.NetWorkUtil;
import huidu.com.voicecall.utils.ToastUtil;

public class MainActivity extends BaseActivity implements ChangeFragmentListener {
    @BindView(R.id.activity_container)
    FrameLayout mActivityContainer;
    @BindView(R.id.rb_mine)
    RadioButton rb_mine;
    @BindView(R.id.main_activity_rg)
    RadioGroup mainActivityRg;
    @BindView(R.id.rb_message)
    RadioButton rb_message;
    @BindView(R.id.rb_main)
    RadioButton rb_main;
    private List<Fragment> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        mList = new ArrayList<>();
        mList.add(new MainFragment());//主页
        mList.add(new MessageFragment());//消息
        mList.add(new MineFragment());//我的
        setTabView(0);
        mainActivityRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_main:
                        setTabView(0);
                        break;
                    case R.id.rb_message:
                        setTabView(1);
                        break;
                    case R.id.rb_mine:
                        setTabView(2);
                        break;
                }
            }
        });
    }

    @Override
    protected void initData() {
    }

    @Override
    public void changeFragment(int position) {

    }

    private void setTabView(int position) {
        FragmentUtil.switchFragment(getSupportFragmentManager(), mList, position, R.id.activity_container);
        switch (position) {
            case 0:
                rb_main.setChecked(true);
                rb_message.setChecked(false);
                rb_mine.setChecked(false);
                rb_main.setTextColor(getResources().getColor(R.color.textSelectColor));
                rb_message.setTextColor(getResources().getColor(R.color.textColor2));
                rb_mine.setTextColor(getResources().getColor(R.color.textColor2));
                break;
            case 1:
                rb_main.setChecked(false);
                rb_message.setChecked(true);
                rb_mine.setChecked(false);
                rb_main.setTextColor(getResources().getColor(R.color.textColor2));
                rb_message.setTextColor(getResources().getColor(R.color.textSelectColor));
                rb_mine.setTextColor(getResources().getColor(R.color.textColor2));
                break;
            case 2:
                rb_main.setChecked(false);
                rb_message.setChecked(false);
                rb_mine.setChecked(true);
                rb_main.setTextColor(getResources().getColor(R.color.textColor2));
                rb_mine.setTextColor(getResources().getColor(R.color.textSelectColor));
                rb_message.setTextColor(getResources().getColor(R.color.textColor2));
                break;
        }
    }

    private long mFirstTime = 0;

    //改写物理按键——返回的逻辑
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - mFirstTime > 2000) {   //如果两次按键时间间隔大于2秒，则不退出
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mFirstTime = secondTime;//更新firstTime
                return true;
            } else { //两次按键小于2秒时，退出应用
                AtyContainer.getInstance().finishAllActivity();
                System.exit(0);
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
