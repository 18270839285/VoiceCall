package huidu.com.voicecall.mine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import huidu.com.voicecall.R;
import huidu.com.voicecall.base.BaseActivity;
import huidu.com.voicecall.bean.UserMyAccount2;
import huidu.com.voicecall.http.API;
import huidu.com.voicecall.http.BaseModel;
import huidu.com.voicecall.http.OkHttpUtils;
import huidu.com.voicecall.http.RequestFinish;

/**
 * 我的财富
 */
public class MyWealthActivity extends BaseActivity implements RequestFinish{
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_balance)
    TextView tv_balance;
    @BindView(R.id.tv_right)
    TextView tv_right;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_wealth;
    }

    @Override
    protected void initView() {
        tv_title.setText("我的财富");
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText("提现记录");
    }

    @Override
    protected void initData() {

        OkHttpUtils.getInstance().user_myaccount2(API.TOKEN_TEST,this);

    }

    @OnClick({R.id.iv_back,R.id.tv_cash,R.id.tv_right})
    public void onViewClick(View view){
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:
                //提现记录
                jumpTo(CashWithdrawalActivity.class);
            case R.id.tv_cash:
                //提现
                break;
        }
    }

    @Override
    public void onSuccess(BaseModel result, String params) {
       switch (params){
           case API.ORDER_WITHDRAWAL:

               break;
           case API.USER_MYACCOUNT2:
               UserMyAccount2 myAccount2 = (UserMyAccount2)result.getData();
               UserMyAccount2.Info info = myAccount2.getInfo();
               int coin = Integer.parseInt(info.getEarnings_coin());
               int pro = Integer.parseInt(info.getPro());
               String money = coin/pro +"";//info.getPro()
               tv_balance.setText(money);
               break;
       }
    }

    @Override
    public void onError(String result) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }
}
