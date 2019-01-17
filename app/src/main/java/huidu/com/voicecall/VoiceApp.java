package huidu.com.voicecall;

import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.auth.LoginInfo;

import huidu.com.voicecall.utils.SPUtils;

/**
 * Description:
 * Data：2019/1/7-16:12
 * Author: lin
 */
public class VoiceApp extends MultiDexApplication {
    public static Context mContext;
    private static VoiceApp instance;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mContext = getApplicationContext();
        initConfig();
        // SDK初始化（启动后台服务，若已经存在用户登录信息， SDK 将完成自动登录）
        NIMClient.init(this, loginInfo(), new SDKOptions());
    }

    private LoginInfo loginInfo() {
//         从本地读取上次登录成功时保存的用户登录信息
        String account = SPUtils.getValue(this, "account1");
        String token = SPUtils.getValue(this, "password1");

        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)) {
            return new LoginInfo(account, token);
        } else {
            return null;
        }
    }

    private void initConfig() {
        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }

    public static VoiceApp instance() {
        return instance;
    }
}
