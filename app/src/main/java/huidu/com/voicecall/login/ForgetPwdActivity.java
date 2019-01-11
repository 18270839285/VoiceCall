package huidu.com.voicecall.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import huidu.com.voicecall.R;
import huidu.com.voicecall.base.BaseActivity;
import huidu.com.voicecall.http.API;
import huidu.com.voicecall.http.BaseModel;
import huidu.com.voicecall.http.OkHttpUtils;
import huidu.com.voicecall.http.RequestFinish;
import huidu.com.voicecall.utils.CheckUtils;
import huidu.com.voicecall.utils.Loading;
import huidu.com.voicecall.utils.TimeCountUtil;
import huidu.com.voicecall.utils.ToastUtil;

/**
 * 忘记密码
 */
public class ForgetPwdActivity extends BaseActivity implements RequestFinish {
    @BindView(R.id.et_account)
    EditText et_account;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.tv_getCode)
    TextView tv_getCode;
    @BindView(R.id.tv_title)
    TextView tv_title;

    private TimeCountUtil mTimeCount;
    Loading loading;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_forget_pwd;
    }

    @Override
    protected void initView() {
        tv_title.setText("找回密码");
        loading = new Loading(this);
        mTimeCount = new TimeCountUtil(60000, 1000, tv_getCode);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onSuccess(BaseModel result, String params) {
        loading.dismiss();
        switch (params){
            case API.SIGN_GET_VERIFY:
                //获取验证码
                mTimeCount.start();
                break;
            case API.SIGN_VERIFY_CODE:
                //验证验证码
                Intent intent = new Intent(this,Register2Activity.class);
                intent.putExtra("telephone",et_account.getText().toString());
                intent.putExtra("verify_code",et_password.getText().toString());
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onError(String result) {
        ToastUtil.toastShow(result);
    }
    private void checkNext(){
        if (!CheckUtils.isMobile(et_account.getText().toString())){
            ToastUtil.toastShow("请输入正确的手机号码");
            return;
        }
        if (et_password.getText().toString().isEmpty()){
            ToastUtil.toastShow("验证码不能为空");
            return;
        }
        loading.show();
        OkHttpUtils.getInstance().sign_verify_code(et_account.getText().toString(),et_password.getText().toString(),1,this);

    }

    @OnClick({R.id.tv_next, R.id.tv_getCode,R.id.iv_back})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_getCode:
                //获取验证码
                if (!CheckUtils.isMobile(et_account.getText().toString())){
                    ToastUtil.toastShow("请输入正确的手机号码");
                    return;
                }
                loading.show();
                OkHttpUtils.getInstance().sign_get_verify(et_account.getText().toString(),1,this);
                break;
            case R.id.tv_next:
                //下一步
                checkNext();
                break;
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }
}
