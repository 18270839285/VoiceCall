package huidu.com.voicecall.voice;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.avchat.AVChatCallback;
import com.netease.nimlib.sdk.avchat.AVChatManager;
import com.netease.nimlib.sdk.avchat.AVChatStateObserverLite;
import com.netease.nimlib.sdk.avchat.constant.AVChatChannelProfile;
import com.netease.nimlib.sdk.avchat.model.AVChatAudioFrame;
import com.netease.nimlib.sdk.avchat.model.AVChatCommonEvent;
import com.netease.nimlib.sdk.avchat.model.AVChatData;
import com.netease.nimlib.sdk.avchat.model.AVChatNetworkStats;
import com.netease.nimlib.sdk.avchat.model.AVChatParameters;
import com.netease.nimlib.sdk.avchat.model.AVChatSessionStats;
import com.netease.nimlib.sdk.avchat.model.AVChatVideoFrame;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import huidu.com.voicecall.R;
import huidu.com.voicecall.base.BaseActivity;
import huidu.com.voicecall.bean.ImInfo;
import huidu.com.voicecall.http.BaseModel;
import huidu.com.voicecall.http.OkHttpUtils;
import huidu.com.voicecall.http.RequestFinish;
import huidu.com.voicecall.utils.GlideBlurformation;
import huidu.com.voicecall.utils.ToastUtil;

/**
 * 接收来电
 */
public class ReceiveRoomActivity extends BaseActivity implements RequestFinish {
    @BindView(R.id.iv_bg)
    ImageView iv_bg;
    @BindView(R.id.iv_hands_free)
    ImageView iv_hands_free;//免提图片
    @BindView(R.id.iv_mute)
    ImageView iv_mute;//静音图片
    @BindView(R.id.iv_head)
    CircleImageView iv_head;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_message)
    TextView tv_message;
    @BindView(R.id.tv_timer)
    Chronometer tv_timer;
    @BindView(R.id.ll_type2)
    LinearLayout ll_type2;
    @BindView(R.id.ll_type3)
    LinearLayout ll_type3;

    String head_image = "";
    String nickname = "";

    boolean isMute = false;
    boolean isHandsFree = false;

    private AVChatData avChatData;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_receive_room;
    }

    @Override
    protected void initView() {
        avChatData = (AVChatData) getIntent().getSerializableExtra("AVChatData");
        OkHttpUtils.getInstance().accid_name(avChatData.getAccount(), this);
        Log.e(TAG, "initView: account = " + avChatData.getAccount() + "  chatId = " + avChatData.getChatId());
//        OkHttpUtils.getInstance().accid_name("yinyuan14",this);
    }

    @Override
    protected void initData() {
        //接听，告知服务器，以便通知其他端
        AVChatManager.getInstance().enableRtc();
//        AVChatManagerLite.getInstance().enableRtc();
        //设置自己需要的可选参数
        AVChatManager.getInstance().setChannelProfile(AVChatChannelProfile.CHANNEL_PROFILE_HIGH_QUALITY_MUSIC_ADAPTIVE);
        AVChatParameters avChatParameters = new AVChatParameters();
        avChatParameters.set(AVChatParameters.KEY_AUDIO_HIGH_QUALITY, true);
        AVChatManager.getInstance().setParameters(avChatParameters);

        /**
         * 收到对方结束通话回调
         */
        Observer<AVChatCommonEvent> callHangupObserver = new Observer<AVChatCommonEvent>() {
            @Override
            public void onEvent(AVChatCommonEvent hangUpInfo) {
                // 结束通话
                Log.e("callAckObserver", "onEvent: 结束通话");
                setStop();
                logOut();
            }
        };
        AVChatManager.getInstance().observeHangUpNotification(callHangupObserver, true);

        /**
         * 通话建立结果回调
         */
        AVChatStateObserverLite state = new AVChatStateObserverLite() {
            @Override
            public void onCallEstablished() {
                ll_type3.setVisibility(View.VISIBLE);
                ll_type2.setVisibility(View.GONE);
                setStart();
                Log.e("onCallEstablished: ", "正在通话");
                ToastUtil.toastShow("已接通");
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
        AVChatManager.getInstance().observeAVChatState(state, true);

    }

    @Override
    public void onSuccess(BaseModel result, String params) {
        ImInfo info = (ImInfo) result.getData();
        head_image = info.getIcon();
        nickname = info.getName();
        Log.e(TAG, "onSuccess: headImage : " + head_image + "  nickname : " + nickname);
        tv_name.setText(nickname);
        Glide.with(this).load(head_image).into(iv_head);
        Glide.with(this)
                .load(head_image)
                .apply(RequestOptions.bitmapTransform(new GlideBlurformation(this)))
                .into(iv_bg);
    }

    @Override
    public void onError(String result) {
        Log.e(TAG, "onError" + result);
    }

    @OnClick({R.id.iv_icon, R.id.ll_refuse, R.id.ll_agree, R.id.ll_mute, R.id.ll_hang_up, R.id.ll_hands_free})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_icon:
                break;
            case R.id.ll_refuse:
                hangUp();
                break;
            case R.id.ll_agree:
                receiveInComingCall();
                break;
            case R.id.ll_mute:
                //是否静音
                isMute = !isMute;
                AVChatManager.getInstance().setMicrophoneMute(isMute);
                if (isMute) {
                    iv_mute.setImageResource(R.mipmap.djt_jy_pre);
                } else {
                    iv_mute.setImageResource(R.mipmap.djt_jy);
                }
                break;
            case R.id.ll_hang_up:
                hangUp();
                break;
            case R.id.ll_hands_free:
                //是否免提
                isHandsFree = !isHandsFree;
                AVChatManager.getInstance().setSpeaker(isHandsFree);
                if (isHandsFree) {
                    iv_hands_free.setImageResource(R.mipmap.djt_mt_pre);
                } else {
                    iv_hands_free.setImageResource(R.mipmap.djt_mt);
                }
                break;
        }
    }

    private void setStart() {
        tv_timer.setVisibility(View.VISIBLE);
        tv_message.setVisibility(View.GONE);
        tv_timer.setBase(SystemClock.elapsedRealtime());//计时器清零
        int hour = (int) ((SystemClock.elapsedRealtime() - tv_timer.getBase()) / 1000 / 60);
        tv_timer.setFormat("0" + String.valueOf(hour) + ":%s");
        tv_timer.start();
    }

    private void setStop() {
        tv_timer.setVisibility(View.GONE);
        tv_message.setVisibility(View.VISIBLE);
        tv_timer.setBase(SystemClock.elapsedRealtime());//计时器清零
        tv_timer.stop();
    }

    /**
     * 接听来电
     */
    private void receiveInComingCall() {

        AVChatManager.getInstance().accept2(avChatData.getChatId(), new AVChatCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e("receiveInComingCall", "onSuccess: ");
                ll_type2.setVisibility(View.GONE);
                ll_type3.setVisibility(View.VISIBLE);
                setStart();
            }

            @Override
            public void onFailed(int code) {
                if (code == -1) {
                    ToastUtil.toastShow("本地音视频启动失败");
                } else {
                    ToastUtil.toastShow("建立连接失败");
                }
                handleAcceptFailed();
            }

            @Override
            public void onException(Throwable exception) {
                Log.e("receiveInComingCall", "onException: " + exception.getMessage());
                handleAcceptFailed();
            }
        });
    }

    private void handleAcceptFailed() {
        AVChatManager.getInstance().disableRtc();
//        AVChatManagerLite.getInstance().disableRtc();
    }

    /**
     * 挂断
     */
    private void hangUp() {

        AVChatManager.getInstance().hangUp2(avChatData.getChatId(), new AVChatCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e("hangUp", "onSuccess: 已挂断");
                logOut();
            }

            @Override
            public void onFailed(int code) {
                Log.e("hangUp", "onFailed: 挂断失败");
                handleAcceptFailed();
            }

            @Override
            public void onException(Throwable exception) {
                exception.printStackTrace();
                handleAcceptFailed();
                Log.e("hangUp", "onException: " + exception.getMessage());
            }
        });
    }

    /**
     * 登出
     */
    private void logOut() {
//        NIMClient.getService(AuthService.class).logout();
        //销毁音视频引擎和释放资源
        handleAcceptFailed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }
}
