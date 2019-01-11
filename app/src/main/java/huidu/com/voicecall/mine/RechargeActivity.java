package huidu.com.voicecall.mine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import huidu.com.voicecall.R;
import huidu.com.voicecall.base.BaseActivity;

/**
 * 充值
 */
public class RechargeActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_recharge;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }
}
