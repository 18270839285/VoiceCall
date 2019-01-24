package huidu.com.voicecall;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.baidu.idl.face.platform.LivenessTypeEnum;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.constant.AVChatControlCommand;
import com.netease.nimlib.sdk.avchat.model.AVChatData;

import java.util.ArrayList;
import java.util.List;

import huidu.com.voicecall.test.PhoneCallStateObserver;
import huidu.com.voicecall.utils.SPUtils;
import huidu.com.voicecall.voice.ChattingRoomActivity;
import huidu.com.voicecall.voice.ReceiveRoomActivity;

/**
 * Description:
 * Data：2019/1/7-16:12
 * Author: lin
 */
public class VoiceApp extends MultiDexApplication {
    public static Context mContext;
    private static VoiceApp instance;
    public static List<LivenessTypeEnum> livenessList = new ArrayList<LivenessTypeEnum>();
    public static boolean isLivenessRandom = false;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mContext = getApplicationContext();
        initConfig();
        // SDK初始化（启动后台服务，若已经存在用户登录信息， SDK 将完成自动登录）
        SDKOptions options = new SDKOptions();
        options.appKey = "f0c2e363ab9ac4693c0994fd2e3bceaa";
        NIMClient.init(this, loginInfo(), options);
//        NIMClient.init(this, loginInfo(), options);
        enableAVChat();
    }

    private LoginInfo loginInfo() {
//         从本地读取上次登录成功时保存的用户登录信息
        String account = SPUtils.getValue("account1");
        String token = SPUtils.getValue("token");

        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)) {
            return new LoginInfo(account, token);
        } else {
            return null;
        }
    }

    private void enableAVChat() {
        registerAVChatIncomingCallObserver(true);
    }

    private void registerAVChatIncomingCallObserver(boolean register) {
        AVChatManager.getInstance().observeIncomingCall(new Observer<AVChatData>() {
            @Override
            public void onEvent(AVChatData data) {
                String extra = data.getExtra();
                if (PhoneCallStateObserver.getInstance().getPhoneCallState() != PhoneCallStateObserver.PhoneCallStateEnum.IDLE
//                        || AVChatProfile.getInstance().isAVChatting()
                        || AVChatManager.getInstance().getCurrentChatId() != 0) {
                    AVChatManager.getInstance().sendControlCommand(data.getChatId(), AVChatControlCommand.BUSY, null);
                    return;
                }
                // 有网络来电打开AVChatActivity
//                AVChatProfile.getInstance().setAVChatting(true);
//                AVChatActivity.launch(DemoCache.getContext(), data, AVChatActivity.FROM_BROADCASTRECEIVER);
                startActivity(new Intent(mContext, ReceiveRoomActivity.class).putExtra("AVChatData",data));
            }
        }, register);
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
