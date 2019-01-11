package huidu.com.voicecall.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import huidu.com.voicecall.R;
import huidu.com.voicecall.base.BaseActivity;
import huidu.com.voicecall.http.BaseModel;
import huidu.com.voicecall.http.RequestFinish;
import huidu.com.voicecall.utils.ToastUtil;

/**
 * 订单详情
 */
public class OrderDetailActivity extends BaseActivity implements RequestFinish{
    @BindView(R.id.tv_title)
    TextView tv_title;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_detail;
    }

    @Override
    protected void initView() {
        tv_title.setText("订单详情");
    }

    @Override
    protected void initData() {

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
