package huidu.com.voicecall.login;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import huidu.com.voicecall.R;
import huidu.com.voicecall.base.BaseActivity;
import huidu.com.voicecall.bean.SignBean;
import huidu.com.voicecall.http.BaseModel;
import huidu.com.voicecall.http.OkHttpUtils;
import huidu.com.voicecall.http.RequestFinish;
import huidu.com.voicecall.main.MainActivity;
import huidu.com.voicecall.utils.CheckUtils;
import huidu.com.voicecall.utils.MD5Util;
import huidu.com.voicecall.utils.ToastUtil;

/**
 * 登录
 */
public class LoginActivity extends BaseActivity implements RequestFinish {

    @BindView(R.id.cb_agree)
    CheckBox cb_agree;
    @BindView(R.id.et_account)
    EditText et_account;
    @BindView(R.id.et_password)
    EditText et_password;
    boolean isCheck = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    String telephone;
    String password;

    @Override
    protected void initView() {
        if (getIntent() != null) {
            telephone = getIntent().getStringExtra("telephone");
            password = getIntent().getStringExtra("password");
            et_account.setText(telephone);
            et_password.setText(password);
        }
        cb_agree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCheck = isChecked;
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onSuccess(BaseModel result, String params) {
        SignBean signBean = (SignBean) result.getData();
        String token = signBean.getToken();
        String user_id = signBean.getUser_id();
        jumpTo(MainActivity.class);

    }

    @Override
    public void onError(String result) {
        ToastUtil.toastShow(result);
    }

    private void checkLogin() {
        if (!CheckUtils.isMobile(et_account.getText().toString())) {
            ToastUtil.toastShow("请输入正确的手机号码");
            return;
        }
        if (et_password.getText().toString().isEmpty()) {
            ToastUtil.toastShow("密码不能为空");
            return;
        }
        if (!isCheck) {
            ToastUtil.toastShow("请先阅读并同意用户协议");
            return;
        }
        String code = MD5Util.GetMD5Code(et_password.getText().toString());
        OkHttpUtils.getInstance().login(et_account.getText().toString(), code, this);
    }

    @OnClick({R.id.tv_forget, R.id.tv_protocol1, R.id.tv_protocol2, R.id.tv_go_register, R.id.tv_login})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.tv_forget:
                //忘记密码
                jumpTo(ForgetPwdActivity.class);
                break;
            case R.id.tv_protocol1:
                //用户协议
                break;
            case R.id.tv_protocol2:
                //隐私协议
                break;
            case R.id.tv_go_register:
                //去注册
                jumpTo(RegisterActivity.class);
                break;
            case R.id.tv_login:
                //登录
                checkLogin();
                break;
            case R.id.tv_agree:
                if (isCheck)
                    cb_agree.setChecked(false);
                else
                    cb_agree.setChecked(true);


                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }
}
