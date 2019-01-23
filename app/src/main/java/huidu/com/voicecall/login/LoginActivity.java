package huidu.com.voicecall.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import huidu.com.voicecall.R;
import huidu.com.voicecall.base.BaseActivity;
import huidu.com.voicecall.base.WebActivity;
import huidu.com.voicecall.bean.ImInfo;
import huidu.com.voicecall.bean.SignBean;
import huidu.com.voicecall.http.API;
import huidu.com.voicecall.http.BaseModel;
import huidu.com.voicecall.http.OkHttpUtils;
import huidu.com.voicecall.http.RequestFinish;
import huidu.com.voicecall.main.MainActivity;
import huidu.com.voicecall.utils.CheckUtils;
import huidu.com.voicecall.utils.MD5Util;
import huidu.com.voicecall.utils.SPUtils;
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
    boolean isCheck = true;

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
        switch (params) {
            case API.SIGN:
                SignBean signBean = (SignBean) result.getData();
                String token = signBean.getToken();
                String user_id = signBean.getUser_id();
                SPUtils.putValue("token", token);
                SPUtils.putValue("user_id", user_id);
                OkHttpUtils.getInstance().sign_im_info(token, this);
                break;
            case API.SIGN_IM_INFO:
                ImInfo imInfo = (ImInfo) result.getData();
                SPUtils.putValue("accid", imInfo.getAccid());
                doLogin(SPUtils.getValue("token"));
                jumpTo(MainActivity.class);
                break;
        }

    }

    @Override
    public void onError(String result) {
        ToastUtil.toastShow(result);
    }

    public void doLogin(String token) {
        Log.e("doLogin", "accid = " + SPUtils.getValue("accid") + "  token = " + token);
        LoginInfo info = new LoginInfo(SPUtils.getValue("accid"), token); // config...tokenFromPassword(token)
        RequestCallback<LoginInfo> callback =
                new RequestCallback<LoginInfo>() {
                    @Override
                    public void onSuccess(LoginInfo param) {
                        ToastUtil.toastShow("登录成功");
//                        Log.e(TAG, "onSuccess: 登录成功");
                        Log.e("RequestCallback", "onSuccess: account1 = " + param.getAccount() + "  password1 = " + param.getToken());
                        // 可以在此保存LoginInfo到本地，下次启动APP做自动登录用
                        SPUtils.putValue("account1", param.getAccount());
                    }

                    @Override
                    public void onFailed(int code) {
                        Log.e("RequestCallback", "onFailed: code = " + code);
                    }

                    @Override
                    public void onException(Throwable exception) {
                        Log.e("RequestCallback", "onException: " + exception);
                    }

                };
        NIMClient.getService(AuthService.class).login(info)
                .setCallback(callback);
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
                startActivity(new Intent(this, WebActivity.class).putExtra("web_type", 2));
                break;
            case R.id.tv_protocol2:
                //隐私协议
                startActivity(new Intent(this, WebActivity.class).putExtra("web_type", 4));
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
