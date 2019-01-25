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
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.avchat.AVChatCallback;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.AVChatManagerLite;
import com.netease.nimlib.sdk.avchat.AVChatStateObserverLite;
import com.netease.nimlib.sdk.avchat.constant.AVChatChannelProfile;
import com.netease.nimlib.sdk.avchat.constant.AVChatEventType;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.netease.nimlib.sdk.avchat.model.AVChatAudioFrame;
import com.netease.nimlib.sdk.avchat.model.AVChatCalleeAckEvent;
import com.netease.nimlib.sdk.avchat.model.AVChatCommonEvent;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.netease.nimlib.sdk.avchat.model.AVChatNetworkStats;
import com.netease.nimlib.sdk.avchat.model.AVChatNotifyOption;
import com.netease.nimlib.sdk.avchat.model.AVChatParameters;
import com.netease.nimlib.sdk.avchat.model.AVChatSessionStats;
import com.netease.nimlib.sdk.avchat.model.AVChatVideoFrame;

import java.util.Map;

import huidu.com.voicecall.R;
import huidu.com.voicecall.utils.MD5;
import huidu.com.voicecall.utils.SPUtils;
import huidu.com.voicecall.utils.ToastUtil;

/**
 * 测试网易云信
 */
public class TestActivity extends AppCompatActivity {

    Button start, login,stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        start = findViewById(R.id.start);
        stop = findViewById(R.id.stop);
        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin();
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hangUp();
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
                        SPUtils.putValue("account1", param.getAccount());
                        SPUtils.putValue( "password1", param.getToken());
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

    AVChatType callingState = AVChatType.AUDIO;
    AVChatData avChatData;
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
        AVChatManager.getInstance().setChannelProfile(AVChatChannelProfile.CHANNEL_PROFILE_HIGH_QUALITY_MUSIC_ADAPTIVE);
        AVChatParameters avChatParameters = new AVChatParameters();
        avChatParameters.set(AVChatParameters.KEY_AUDIO_HIGH_QUALITY, true);
        AVChatManager.getInstance().setParameters(avChatParameters);

        //呼叫
        AVChatManager.getInstance().call2(account, callingState, notifyOption, new AVChatCallback<AVChatData>() {
            @Override
            public void onSuccess(AVChatData data) {
                avChatData = data;
                Log.e("onSuccess: ", "Account : " + data.getAccount());
                //发起会话成功
            }

            @Override
            public void onFailed(int code) {
//                closeRtc();
                AVChatManagerLite.getInstance().disableRtc();
//                closeSessions(-1);
            }

            @Override
            public void onException(Throwable exception) {
//                closeRtc();
                AVChatManagerLite.getInstance().disableRtc();
//                closeSessions(-1);
            }
        });

        /**
         * 主叫收到被叫响应回调
         */
        Observer<AVChatCalleeAckEvent> callAckObserver = new Observer<AVChatCalleeAckEvent>() {
            @Override
            public void onEvent(AVChatCalleeAckEvent ackInfo) {
                if (ackInfo.getEvent() == AVChatEventType.CALLEE_ACK_BUSY) {
                    // 对方正在忙
                    ToastUtil.toastShow("对方正在忙");
                    Log.e("callAckObserver", "onEvent: 对方正在忙" );
                } else if (ackInfo.getEvent() == AVChatEventType.CALLEE_ACK_REJECT) {
                    // 对方拒绝接听
                    ToastUtil.toastShow("对方拒绝接听");
                    Log.e("callAckObserver", "onEvent: 对方拒绝接听" );
                } else if (ackInfo.getEvent() == AVChatEventType.CALLEE_ACK_AGREE) {
                    // 对方同意接听
                    ToastUtil.toastShow("对方同意接听");
                    Log.e("callAckObserver", "onEvent: 对方同意接听" );
                }
            }
        };
        AVChatManager.getInstance().observeCalleeAckNotification(callAckObserver, true);
        /**
         * 收到对方结束通话回调
         */
        Observer<AVChatCommonEvent> callHangupObserver = new Observer<AVChatCommonEvent>() {
            @Override
            public void onEvent(AVChatCommonEvent hangUpInfo) {
                // 结束通话
                Log.e("callAckObserver", "onEvent: 结束通话" );
            }
        };
        AVChatManager.getInstance().observeHangUpNotification(callHangupObserver, true);

        /**
         * 通话建立结果回调
         */
        AVChatStateObserverLite state = new AVChatStateObserverLite(){
            @Override
            public void onCallEstablished() {
                Log.e( "onCallEstablished: ","正在通话" );
            }

            @Override
            public void onJoinedChannel(int code, String audioFile, String videoFile, int elapsed) {

            }

            @Override
            public void onUserJoined(String account) {

            }

            @Override
            public void onUserLeave(String account, int event) {

            }

            @Override
            public void onLeaveChannel() {

            }

            @Override
            public void onProtocolIncompatible(int status) {

            }

            @Override
            public void onDisconnectServer() {

            }

            @Override
            public void onNetworkQuality(String account, int quality, AVChatNetworkStats stats) {

            }

            @Override
            public void onDeviceEvent(int code, String desc) {

            }

            @Override
            public void onConnectionTypeChanged(int netType) {

            }

            @Override
            public void onFirstVideoFrameAvailable(String account) {

            }

            @Override
            public void onFirstVideoFrameRendered(String account) {

            }

            @Override
            public void onVideoFrameResolutionChanged(String account, int width, int height, int rotate) {

            }

            @Override
            public void onVideoFpsReported(String account, int fps) {

            }

            @Override
            public boolean onVideoFrameFilter(AVChatVideoFrame frame, boolean maybeDualInput) {
                return false;
            }

            @Override
            public boolean onAudioFrameFilter(AVChatAudioFrame frame) {
                return false;
            }

            @Override
            public void onAudioDeviceChanged(int device) {

            }

            @Override
            public void onReportSpeaker(Map<String, Integer> speakers, int mixedEnergy) {

            }

            @Override
            public void onSessionStats(AVChatSessionStats sessionStats) {

            }

            @Override
            public void onLiveEvent(int event) {

            }
        };
        AVChatManager.getInstance().observeAVChatState(state,true);
    }

    /**
     * 注销按钮响应事件
     */
    private void logout() {
        NIMClient.getService(AuthService.class).logout();
//        MyCache.clear();
//        startActivity(new Intent(MessageActivity.this, LoginActivity.class));
    }

    private void hangUp(){
        //挂断
        AVChatManager.getInstance().hangUp2(avChatData.getChatId(), new AVChatCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e("hangUp", "onSuccess: 已挂断");
            }

            @Override
            public void onFailed(int code) {
                Log.e("hangUp", "onFailed: 挂断失败");
            }

            @Override
            public void onException(Throwable exception) {
                exception.printStackTrace();
                Log.e("hangUp", "onException: "+exception.getMessage());
            }
        });
        //销毁音视频引擎和释放资源
        AVChatManager.getInstance().disableRtc();
    }
}

//    DEMO中使用 username 作为 NIM 的account ，md5(password) 作为 token
////    开发者需要根据自己的实际情况配置自身用户系统和 NIM 用户系统的关系
//    private String tokenFromPassword(String password) {
//        String appKey = readAppKey(this);
//        boolean isDemo = "45c6af3c98409b18a84451215d0bdd6e".equals(appKey) ||
//                "fe416640c8e8a72734219e1847ad2547".equals(appKey);
//
////        return MD5.getStringMD5(password);
//        return isDemo ? MD5.getStringMD5(password) : password;
//    }
//
//    public void doLogin() {
////        LoginInfo info = new LoginInfo("liyunwei", tokenFromPassword("123456")); // config...
//        LoginInfo info = new LoginInfo("voicecall", tokenFromPassword("qwe123")); // config...
//        RequestCallback<LoginInfo> callback =
//                new RequestCallback<LoginInfo>() {
//                    @Override
//                    public void onSuccess(LoginInfo param) {
////                        ToastUtil.toastShow("登录成功!");
////                        outGoingCalling("voicecall");
//                        outGoingCalling("liyunwei");
//                        Log.e("RequestCallback", "onSuccess: account1 = " + param.getAccount() + "  password1 = " + param.getToken());
//                        // 可以在此保存LoginInfo到本地，下次启动APP做自动登录用
//                        SPUtils.putValue("account1", param.getAccount());
//                        SPUtils.putValue("password1", param.getToken());
//                    }
//
//                    @Override
//                    public void onFailed(int code) {
//                        Log.e("RequestCallback", "onFailed: code = " + code);
//                    }
//
//                    @Override
//                    public void onException(Throwable exception) {
//                        Log.e("RequestCallback", "onException: " + exception);
//                    }
//
//                };
//        NIMClient.getService(AuthService.class).login(info)
//                .setCallback(callback);
//    }

//    private static String readAppKey(Context context) {
//        try {
//            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
//            if (appInfo != null) {
//                return appInfo.metaData.getString("com.netease.nim.appKey");
////                return appInfo.metaData.getString("huidu.com.voicecall");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }