package huidu.com.voicecall.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import huidu.com.voicecall.R;
import huidu.com.voicecall.base.BaseActivity;
import huidu.com.voicecall.base.WebActivity;
import huidu.com.voicecall.http.API;
import huidu.com.voicecall.http.BaseModel;
import huidu.com.voicecall.http.OkHttpUtils;
import huidu.com.voicecall.http.RequestFinish;
import huidu.com.voicecall.utils.AtyContainer;
import huidu.com.voicecall.utils.SPUtils;
import huidu.com.voicecall.utils.ToastUtil;

/**
 * 提现
 */
public class CashActivity extends BaseActivity implements RequestFinish {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_can_cash)
    TextView tv_can_cash;
    @BindView(R.id.et_cash)
    EditText et_cash;
    @BindView(R.id.et_ali_account)
    EditText et_ali_account;
    @BindView(R.id.et_weixin_account)
    EditText et_weixin_account;
    @BindView(R.id.tv_money)
    TextView tv_money;
    @BindView(R.id.tv_fee)
    TextView tv_fee;
    @BindView(R.id.iv_check_weixin)
    ImageView iv_check_weixin;
    @BindView(R.id.iv_check_ali)
    ImageView iv_check_ali;

    //Y豆人民币比率
    String exchange_pro;
    //最大提现金额
    String max_money;
    //提现率
    String formalities_pro;

    int payType = 0;
    String RECEIVE_ACCOUNT;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cash;
    }

    @Override
    protected void initView() {
        tv_title.setText("提现");
        if (getIntent() != null) {
            exchange_pro = getIntent().getStringExtra("exchange_pro");
            max_money = getIntent().getStringExtra("max_money");
            formalities_pro = getIntent().getStringExtra("formalities_pro");
        }

        et_cash.setText(max_money);
        et_cash.setSelection(max_money.length());
        initMoney();
        tv_can_cash.setText(max_money + "元");
        et_ali_account.setOnTouchListener(new View.OnTouchListener() {
            //按住和松开的标识
            int touch_flag = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touch_flag++;
                if (touch_flag == 2) {
                    selectPayType(1);
                    touch_flag = 0;
                }
                return false;
            }
        });
        et_weixin_account.setOnTouchListener(new View.OnTouchListener() {
            //按住和松开的标识
            int touch_flag = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touch_flag++;
                if (touch_flag == 2) {
                    selectPayType(2);
                    touch_flag = 0;
                }
                return false;
            }
        });

        et_cash.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && et_cash.getText().toString().length() >= 3) {
                    initMoney();
                }
            }
        });

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onSuccess(BaseModel result, String params) {
        switch (params) {
            case API.ORDER_WITHDRAWAL:
                ToastUtil.toastShow("已提交审核");
                finish();
                AtyContainer.getInstance().finishActivity(MyWealthActivity.class);
//                jumpTo(CashWithdrawalActivity.class);
                break;
        }
    }

    @Override
    public void onError(String result) {
        ToastUtil.toastShow(result);
    }


    private void selectPayType(int type) {
        payType = type;
        if (type == 1) {
            iv_check_ali.setImageResource(R.mipmap.tx_pre);
            iv_check_weixin.setImageResource(R.mipmap.tx_nor);
        } else if (type == 2) {
            iv_check_weixin.setImageResource(R.mipmap.tx_pre);
            iv_check_ali.setImageResource(R.mipmap.tx_nor);
        }
    }

    /**
     * 处理手续费显示
     */
    private void initMoney() {
        int money = Integer.parseInt(et_cash.getText().toString());
        int money1 = (int) (money * Float.parseFloat(formalities_pro));
        int money2 = (int) (money - (money * Float.parseFloat(formalities_pro)));
        tv_money.setText(money1 + "元");
        tv_fee.setText(money2 + "元");
    }

    private void changeCash() {
        if (et_cash.getText() == null || et_cash.getText().toString().isEmpty() || Integer.parseInt(et_cash.getText().toString()) < 100) {
            ToastUtil.toastShow("提现金额不能少于100");
            return;
        }

        if (Integer.parseInt(et_cash.getText().toString()) > Integer.parseInt(max_money)) {
            ToastUtil.toastShow("提现金额不能大于可提现收益");
            return;
        }
        if (Integer.parseInt(et_cash.getText().toString()) % 10 > 0) {
            ToastUtil.toastShow("提现金额必须为10的倍数");
            return;
        }
        if (payType == 0) {
            ToastUtil.toastShow("请选择支付方式");
            return;
        }

        if (payType == 1) {
            if (et_ali_account.getText().toString().isEmpty()) {
                ToastUtil.toastShow("支付宝账号不能为空");
                return;
            }
            RECEIVE_ACCOUNT = et_ali_account.getText().toString();
        } else if (payType == 2) {
            if (et_weixin_account.getText().toString().isEmpty()) {
                ToastUtil.toastShow("微信账号不能为空");
                return;
            }
            RECEIVE_ACCOUNT = et_weixin_account.getText().toString();
        }
        OkHttpUtils.getInstance().order_withdrawal(SPUtils.getValue("token"), et_cash.getText().toString(),
                Integer.parseInt(exchange_pro) * Integer.parseInt(et_cash.getText().toString()) + "", payType + "", RECEIVE_ACCOUNT, this);
    }


    @OnClick({R.id.iv_back, R.id.ll_ali, R.id.ll_weixin, R.id.ll_protocol, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_ali:
                //选择ali提现
                selectPayType(1);
                break;
            case R.id.ll_weixin:
                //选择微信提现
                selectPayType(2);
                break;
            case R.id.ll_protocol:
                //点击协议
                startActivity(new Intent(this, WebActivity.class).putExtra("web_type",5));
                break;
            case R.id.tv_submit:
                //立即提现
                changeCash();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }
}
