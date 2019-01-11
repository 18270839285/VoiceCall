package huidu.com.voicecall.test;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.avchat.AVChatCallback;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.netease.nimlib.sdk.avchat.model.AVChatNotifyOption;

import huidu.com.voicecall.R;
import huidu.com.voicecall.utils.MD5;
import huidu.com.voicecall.utils.SPUtils;
import huidu.com.voicecall.utils.ToastUtil;

/**
 * 测试网易云信
 */
public class TestActivity extends AppCompatActivity {

    Button start, login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        start = findViewById(R.id.start);
        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin();
            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outGoingCalling("liyunwei");
            }
        });
    }

    //    DEMO中使用 username 作为 NIM 的account ，md5(password) 作为 token
//    开发者需要根据自己的实际情况配置自身用户系统和 NIM 用户系统的关系
    private String tokenFromPassword(String password) {
        String appKey = readAppKey(this);
        boolean isDemo = "45c6af3c98409b18a84451215d0bdd6e".equals(appKey) ||
                "fe416640c8e8a72734219e1847ad2547".equals(appKey);

//        return MD5.getStringMD5(password);
        return isDemo ? MD5.getStringMD5(password) : password;
    }

    public void doLogin() {
        LoginInfo info = new LoginInfo("voicecall", tokenFromPassword("qwe123")); // config...
        RequestCallback<LoginInfo> callback =
                new RequestCallback<LoginInfo>() {
                    @Override
                    public void onSuccess(LoginInfo param) {
                        ToastUtil.toastShow("登录成功!");

                        Log.e("RequestCallback", "onSuccess: account1 = " + param.getAccount() + "  password1 = " + param.getToken());
                        SPUtils.putValue(getApplicationContext(), "account1", param.getAccount());
                        SPUtils.putValue(getApplicationContext(), "password1", param.getToken());
                    }

                    @Override
                    public void onFailed(int code) {
                        Log.e("RequestCallback", "onFailed: code = " + code);
                        ToastUtil.toastShow("登录失败! code = " + code);
                    }

                    @Override
                    public void onException(Throwable exception) {
                        Log.e("RequestCallback", "onException: " + exception);
                    }
                    // 可以在此保存LoginInfo到本地，下次启动APP做自动登录用
                };
        NIMClient.getService(AuthService.class).login(info)
                .setCallback(callback);
    }

    private static String readAppKey(Context context) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo != null) {
                return appInfo.metaData.getString("com.netease.nim.appKey");
//                return appInfo.metaData.getString("huidu.com.voicecall");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    AVChatType callingState  = AVChatType.AUDIO;
    /**
     * 拨打音视频
     */
    public void outGoingCalling(String account) {
        AVChatNotifyOption notifyOption = new AVChatNotifyOption();
        //附加字段
        notifyOption.extendMessage = "extra_data";
        //是否兼容WebRTC模式
//        notifyOption.webRTCCompat = webrtcCompat;
        //默认forceKeepCalling为true，开发者如果不需要离线持续呼叫功能可以将forceKeepCalling设为false
        notifyOption.forceKeepCalling = false;
        //打开Rtc模块
        AVChatManager.getInstance().enableRtc();

        //设置自己需要的可选参数
//        AVChatManager.getInstance().setParameters(avChatParameters);

        //呼叫
        AVChatManager.getInstance().call2(account, callingState, notifyOption, new AVChatCallback<AVChatData>() {
            @Override
            public void onSuccess(AVChatData data) {
//                avChatData = data;
                //发起会话成功
            }

            @Override
            public void onFailed(int code) {
//                closeRtc();
//                closeSessions(-1);
            }

            @Override
            public void onException(Throwable exception) {
//                closeRtc();
//                closeSessions(-1);
            }
        });
    }
    /**
     * 注销按钮响应事件
     */
    private void logout() {
        NIMClient.getService(AuthService.class).logout();
//        MyCache.clear();
//        startActivity(new Intent(MessageActivity.this, LoginActivity.class));
    }
}
