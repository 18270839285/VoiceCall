package huidu.com.voicecall.login;

import android.os.Bundle;
import android.os.Handler;

import butterknife.ButterKnife;
import huidu.com.voicecall.R;
import huidu.com.voicecall.base.BaseActivity;
import huidu.com.voicecall.http.BaseModel;
import huidu.com.voicecall.http.OkHttpUtils;
import huidu.com.voicecall.http.RequestFinish;
import huidu.com.voicecall.main.MainActivity;
import huidu.com.voicecall.utils.SPUtils;

/**
 * 启动页
 */
public class LoadingActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_loading;
    }

    @Override
    protected void initView() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.getInstance().home(SPUtils.getValue("token"), "0", "", new RequestFinish() {
                    @Override
                    public void onSuccess(BaseModel result, String params) {
                        jumpTo(MainActivity.class);
                    }

                    @Override
                    public void onError(String result) {

                    }
                });
            }
        }, 1500);
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
