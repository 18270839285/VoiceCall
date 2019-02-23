package huidu.com.voicecall;

import android.app.Notification;
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
import com.netease.nimlib.sdk.util.NIMUtil;
import com.netease.nimlib.service.NimService;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
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

    public static boolean needRefresh1 = false;
    public static boolean needRefresh2 = false;

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
        enableAVChat();
        JPushInterface.init(this);
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
        if (NIMUtil.isMainProcess(this)) {
            // 注意：以下操作必须在主进程中进行
            // 1、UI相关初始化操作
            // 2、相关Service调用
            registerAVChatIncomingCallObserver(true);
//            NimService service = NimService.Aux.
        }
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
                Intent intent = new Intent(mContext, ReceiveRoomActivity.class);
                intent.putExtra("AVChatData",data);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//FLAG_ACTIVITY_NEW_TASK
                startActivity(intent);
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
