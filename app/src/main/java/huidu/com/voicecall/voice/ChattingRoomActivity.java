package huidu.com.voicecall.voice;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import huidu.com.voicecall.R;
import huidu.com.voicecall.base.BaseActivity;
import huidu.com.voicecall.bean.ImInfo;
import huidu.com.voicecall.http.API;
import huidu.com.voicecall.http.BaseModel;
import huidu.com.voicecall.http.OkHttpUtils;
import huidu.com.voicecall.http.RequestFinish;
import huidu.com.voicecall.utils.GlideBlurformation;
import huidu.com.voicecall.utils.SPUtils;
import huidu.com.voicecall.utils.ToastUtil;

/**
 * 聊天室
 */
public class ChattingRoomActivity extends BaseActivity implements RequestFinish {

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
    @BindView(R.id.ll_cancel)
    LinearLayout ll_cancel;
    @BindView(R.id.tv_timer)
    Chronometer tv_timer;
    @BindView(R.id.ll_type2)
    LinearLayout ll_type2;
    @BindView(R.id.ll_type3)
    LinearLayout ll_type3;

    int IDENTITY_TYPE = 1;//2.主播1.用户
    //    int RECEIVE_TYPE = 1; //1.拨号2.来电
    String head_image = "";
    String nickname = "";
    String order_no = "";

    boolean isMute = false;
    boolean isHandsFree = false;
    boolean isBegin = false;
    String accid1;//自己的accid
    String accid2;//对方的accid

    private Handler mHandler;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chatting_room;
    }

    @Override
    protected void initView() {
        IDENTITY_TYPE = getIntent().getIntExtra("IDENTITY_TYPE", 1);
        nickname = getIntent().getStringExtra("nickname");
        head_image = getIntent().getStringExtra("head_image");
        order_no = getIntent().getStringExtra("order_no");
        accid2 = getIntent().getStringExtra("accid2");
        tv_name.setText(nickname);
        Glide.with(this).load(head_image).into(iv_head);
        Glide.with(this)
                .load(head_image)
                .apply(RequestOptions.bitmapTransform(new GlideBlurformation(this)))
                .into(iv_bg);
        OkHttpUtils.getInstance().sign_im_info(SPUtils.getValue("token"), this);
//        RECEIVE_TYPE = getIntent().getIntExtra("RECEIVE_TYPE",1);
    }

    @Override
    protected void initData() {

        outGoingCalling(accid2);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        if (ll_type3 == null) {
                            ll_type3 = findViewById(R.id.ll_type3);
                        }
                        if (ll_cancel == null) {
                            ll_cancel = findViewById(R.id.ll_cancel);
                        }
                        ll_type3.setVisibility(View.VISIBLE);
                        ll_cancel.setVisibility(View.GONE);
                        break;
                    case 2:
                        setStart();
                        break;
                    case 3:
                        setStop();
                        break;
                    case 6:
                        if (!isBegin) {
                            ToastUtil.toastShow(nickname + "正忙,请稍候在播！");
                            hangUp();
                            logOut(IDENTITY_TYPE == 1 ? 2 : 1);
                        }
                        break;
                }
            }
        };
        mHandler.sendEmptyMessageDelayed(6, 10000);
    }

    @Override
    public void onSuccess(BaseModel result, String params) {
        if (params.equals(API.SIGN_IM_INFO)) {
            ImInfo imInfo = (ImInfo) result.getData();
            accid1 = imInfo.getAccid();
        }
    }

    @Override
    public void onError(String result) {

    }

    @OnClick({R.id.iv_icon, R.id.ll_cancel, R.id.ll_mute, R.id.ll_hang_up, R.id.ll_hands_free})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_icon:
                break;
            case R.id.ll_cancel:
                hangUp();
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

    private void sendMsg(int what) {
        Message message = Message.obtain();
        message.what = what;
        mHandler.sendMessage(message);
    }

    private void setStart() {
        if (tv_timer == null) {
            tv_timer = findViewById(R.id.tv_timer);
        }
        if (tv_message == null) {
            tv_message = findViewById(R.id.tv_message);
        }
        tv_timer.setVisibility(View.VISIBLE);
        tv_message.setVisibility(View.GONE);
        tv_timer.setBase(SystemClock.elapsedRealtime());//计时器清零
        int hour = (int) ((SystemClock.elapsedRealtime() - tv_timer.getBase()) / 1000 / 60);
        tv_timer.setFormat("0" + String.valueOf(hour) + ":%s");
        tv_timer.start();
    }

    private void setStop() {
        if (tv_timer == null) {
            tv_timer = findViewById(R.id.tv_timer);
        }
        if (tv_message == null) {
            tv_message = findViewById(R.id.tv_message);
        }
        tv_timer.setVisibility(View.GONE);
        tv_message.setVisibility(View.VISIBLE);
        tv_timer.setBase(SystemClock.elapsedRealtime());//计时器清零
        tv_timer.stop();
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
                AVChatManagerLite.getInstance().disableRtc();
            }

            @Override
            public void onException(Throwable exception) {
                AVChatManagerLite.getInstance().disableRtc();
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
                    Log.e("callAckObserver", "onEvent: 对方正在忙");
                } else if (ackInfo.getEvent() == AVChatEventType.CALLEE_ACK_REJECT) {
                    // 对方拒绝接听
                    ToastUtil.toastShow("对方拒绝接听");
                    logOut(IDENTITY_TYPE == 1 ? 2 : 1);
                    Log.e("callAckObserver", "onEvent: 对方拒绝接听");
                } else if (ackInfo.getEvent() == AVChatEventType.CALLEE_ACK_AGREE) {
                    // 对方同意接听
                    Log.e("callAckObserver", "onEvent: 对方同意接听");
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
                Log.e("callAckObserver", "onEvent: 结束通话");
                sendMsg(3);
                logOut(IDENTITY_TYPE == 1 ? 2 : 1);
            }
        };
        AVChatManager.getInstance().observeHangUpNotification(callHangupObserver, true);

        /**
         * 通话建立结果回调
         */
        AVChatStateObserverLite state = new AVChatStateObserverLite() {
            @Override
            public void onCallEstablished() {
                sendMsg(1);
                sendMsg(2);
                ToastUtil.toastShow("已接通");
                OkHttpUtils.getInstance().order_begin(SPUtils.getValue("token"), order_no, IDENTITY_TYPE + "", new RequestFinish() {
                    @Override
                    public void onSuccess(BaseModel result, String params) {
                        Log.e("onSuccess: ", "开始通话");
                        isBegin = true;
                    }

                    @Override
                    public void onError(String result) {

                    }
                });
//                Log.e("onCallEstablished: ", "正在通话");
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

    /**
     * 挂断
     */
    private void hangUp() {

        AVChatManager.getInstance().hangUp2(avChatData.getChatId(), new AVChatCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e("hangUp", "onSuccess: 已挂断");
                logOut(IDENTITY_TYPE);
            }

            @Override
            public void onFailed(int code) {
                Log.e("hangUp", "onFailed: 挂断失败");
                AVChatManager.getInstance().disableRtc();
            }

            @Override
            public void onException(Throwable exception) {
                exception.printStackTrace();
                AVChatManager.getInstance().disableRtc();
                Log.e("hangUp", "onException: " + exception.getMessage());
            }
        });
        //销毁音视频引擎和释放资源
        AVChatManager.getInstance().disableRtc();
    }

    /**
     * 登出
     */
    private void logOut(int overMan) {
//        NIMClient.getService(AuthService.class).logout();
        AVChatManager.getInstance().disableRtc();
        if (isBegin) {
            OkHttpUtils.getInstance().order_over(SPUtils.getValue("token"), accid1, accid2, overMan + "", new RequestFinish() {
                @Override
                public void onSuccess(BaseModel result, String params) {
                    isBegin = false;
                    finish();
                }

                @Override
                public void onError(String result) {

                }
            });
        } else {
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
