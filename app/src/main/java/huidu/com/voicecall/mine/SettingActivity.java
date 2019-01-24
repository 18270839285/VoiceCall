package huidu.com.voicecall.mine;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import huidu.com.voicecall.R;
import huidu.com.voicecall.base.BaseActivity;
import huidu.com.voicecall.base.WebActivity;
import huidu.com.voicecall.http.BaseModel;
import huidu.com.voicecall.http.RequestFinish;
import huidu.com.voicecall.login.ChangePwdActivity;
import huidu.com.voicecall.login.LoginActivity;
import huidu.com.voicecall.login.ResetPwdActivity;
import huidu.com.voicecall.utils.AtyContainer;
import huidu.com.voicecall.utils.DataCleanManager;
import huidu.com.voicecall.utils.DialogUtil;
import huidu.com.voicecall.utils.PointUtils;
import huidu.com.voicecall.utils.SPUtils;

/**
 * 设置
 */
public class SettingActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.clear_cache)
    TextView clear_cache;
    @BindView(R.id.version)
    TextView version;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        tv_title.setText("设置");
        version.setText("版本号" + getVersionName(this));
        try {
            clear_cache.setText(DataCleanManager.getTotalCacheSize(getApplicationContext())+"");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.iv_back, R.id.rl_pwd, R.id.bt_login,R.id.rl2,R.id.rl3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_pwd:
                jumpTo(ChangePwdActivity.class);
                break;
            case R.id.rl2:
                /**
                 * 清理缓存
                 */
                if (PointUtils.isFastClick()){
                    DialogUtil.showDialogConfirm(this, "清除缓存？", "", "取消", null, "确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DataCleanManager.clearAllCache(getApplicationContext());
                            clear_cache.setText("0K");
                        }
                    }, View.VISIBLE).show();}
                break;
            case R.id.rl3:
                /**
                 * 隐私策略
                 */
                Bundle bundle = new Bundle();
                bundle.putInt("web_type", 4);
                jumpTo(WebActivity.class, bundle);
                break;
            case R.id.bt_login://退出登录
                SPUtils.putValue("token", "");
                NIMClient.getService(AuthService.class).logout();
                AtyContainer.getInstance().finishAllActivity();
                jumpTo(LoginActivity.class);
                finish();
                break;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }
    public  String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
