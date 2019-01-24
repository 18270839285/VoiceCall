package huidu.com.voicecall.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import huidu.com.voicecall.R;
import huidu.com.voicecall.base.BaseActivity;
import huidu.com.voicecall.bean.SignBean;
import huidu.com.voicecall.http.BaseModel;
import huidu.com.voicecall.http.OkHttpUtils;
import huidu.com.voicecall.http.RequestFinish;
import huidu.com.voicecall.utils.Loading;
import huidu.com.voicecall.utils.MD5Util;
import huidu.com.voicecall.utils.OnTextChanged;
import huidu.com.voicecall.utils.PointUtils;
import huidu.com.voicecall.utils.SPUtils;
import huidu.com.voicecall.utils.ToastUtil;

/**
 * 修改密码
 */
public class ChangePwdActivity extends BaseActivity implements RequestFinish {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.et_password1)
    EditText et_password1;
    @BindView(R.id.et_password2)
    EditText et_password2;
    @BindView(R.id.et_password3)
    EditText et_password3;

    Loading mLoading;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_pwd;
    }

    @Override
    protected void initView() {
        tv_title.setText("修改密码");
        mLoading = new Loading(this);
    }

    @Override
    protected void initData() {
        OnTextChanged.onTextChanged(et_password1);
        OnTextChanged.onTextChanged(et_password2);
        OnTextChanged.onTextChanged(et_password3);
    }

    @Override
    public void onSuccess(BaseModel result, String params) {
        mLoading.dismiss();
        ToastUtil.toastShow("密码修改成功，请重新登录");
        Intent intent = new Intent(this,LoginActivity.class);
//        intent.putExtra("telephone",telephone);
//        intent.putExtra("password",et_password2.getText().toString());
        startActivity(intent);
        finish();

    }

    @Override
    public void onError(String result) {
        mLoading.dismiss();
        ToastUtil.toastShow(result);
    }

    @OnClick({R.id.tv_forget, R.id.tv_register,R.id.iv_back})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
               finish();
                break;
            case R.id.tv_forget:
                //忘记密码
                jumpTo(ForgetPwdActivity.class);
                break;
            case R.id.tv_register:
                //确定
                if (PointUtils.isFastClick()){
                    checkChange();
                }
                break;
        }
    }

    private void checkChange(){
        if (et_password1.getText().toString().isEmpty()){
            ToastUtil.toastShow("请输入原始密码");
            return;
        }
        if (et_password2.getText().toString().isEmpty()){
            ToastUtil.toastShow("请输入新密码");
            return;
        }
        if (et_password3.getText().toString().isEmpty()){
            ToastUtil.toastShow("请确认新密码");
            return;
        }
        if (!et_password2.getText().toString().equals(et_password3.getText().toString())){
            ToastUtil.toastShow("两次密码输入不一致");
            return;
        }
        String code1 = MD5Util.GetMD5Code(et_password1.getText().toString());
        String code2 = MD5Util.GetMD5Code(et_password2.getText().toString());
        String code3 = MD5Util.GetMD5Code(et_password3.getText().toString());
        mLoading.show();
        OkHttpUtils.getInstance().change(SPUtils.getValue("token"),code1,code2,code3,this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }
}
