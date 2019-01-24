package huidu.com.voicecall.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import huidu.com.voicecall.R;
import huidu.com.voicecall.base.BaseActivity;
import huidu.com.voicecall.http.BaseModel;
import huidu.com.voicecall.http.OkHttpUtils;
import huidu.com.voicecall.http.RequestFinish;
import huidu.com.voicecall.utils.MD5Util;
import huidu.com.voicecall.utils.OnTextChanged;
import huidu.com.voicecall.utils.ToastUtil;

/**
 * 重置密码
 */
public class ResetPwdActivity extends BaseActivity implements RequestFinish {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.et_password1)
    EditText et_password1;
    @BindView(R.id.et_password2)
    EditText et_password2;

    String telephone;
    String verify_code;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_reset_pwd;
    }

    @Override
    protected void initView() {
        tv_title.setText("重置密码");
        if (getIntent() != null) {
            telephone = getIntent().getStringExtra("telephone");
            verify_code = getIntent().getStringExtra("verify_code");
        }
        OnTextChanged.onTextChanged(et_password1);
        OnTextChanged.onTextChanged(et_password2);
    }

    @Override
    protected void initData() {

    }

    private void checkRegister(){

        if (et_password1.getText().toString().isEmpty()){
            ToastUtil.toastShow("请设置登录密码");
            return;
        }
        if (et_password2.getText().toString().isEmpty()){
            ToastUtil.toastShow("请确认登录密码");
            return;
        }
        if (!et_password1.getText().toString().equals(et_password2.getText().toString())){
            ToastUtil.toastShow("两次密码输入不一致");
            return;
        }
        String code1 = MD5Util.GetMD5Code(et_password1.getText().toString());
        String code2 = MD5Util.GetMD5Code(et_password2.getText().toString());
        OkHttpUtils.getInstance().sign_forget(telephone,verify_code,code1,code2,this);
    }

    @Override
    public void onSuccess(BaseModel result, String params) {
        ToastUtil.toastShow("密码修改成功，请重新登录");
        Intent intent = new Intent(this,LoginActivity.class);
        intent.putExtra("telephone",telephone);
        intent.putExtra("password",et_password1.getText().toString());
        startActivity(intent);
        finish();
    }

    @Override
    public void onError(String result) {
        ToastUtil.toastShow(result);
    }

    @OnClick({R.id.iv_back, R.id.tv_register})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_register:
                //注册
                checkRegister();
                break;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
