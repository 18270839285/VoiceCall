package huidu.com.voicecall.dynamic;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.devio.takephoto.app.TakePhoto;
import org.devio.takephoto.app.TakePhotoImpl;
import org.devio.takephoto.compress.CompressConfig;
import org.devio.takephoto.model.InvokeParam;
import org.devio.takephoto.model.TContextWrap;
import org.devio.takephoto.model.TResult;
import org.devio.takephoto.permission.InvokeListener;
import org.devio.takephoto.permission.PermissionManager;
import org.devio.takephoto.permission.TakePhotoInvocationHandler;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import huidu.com.voicecall.R;
import huidu.com.voicecall.audio.RecordingService;
import huidu.com.voicecall.base.BaseActivity;
import huidu.com.voicecall.bean.SpareBean;
import huidu.com.voicecall.http.API;
import huidu.com.voicecall.http.BaseModel;
import huidu.com.voicecall.http.OkHttpUtils;
import huidu.com.voicecall.http.RequestFinish;
import huidu.com.voicecall.main.DynamicFragment;
import huidu.com.voicecall.utils.DialogUtil;
import huidu.com.voicecall.utils.FileUtils;
import huidu.com.voicecall.utils.KeyBoardUtil;
import huidu.com.voicecall.utils.SPUtils;
import huidu.com.voicecall.utils.TimeCountUtil2;
import huidu.com.voicecall.utils.TimeCountUtil3;
import huidu.com.voicecall.utils.ToastUtil;

/**
 * 发布动态
 */
public class PublishActivity extends BaseActivity implements RequestFinish, TakePhoto.TakeResultListener, InvokeListener {

    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_right)
    TextView tv_right;
    @BindView(R.id.et_content)
    EditText et_content;
    @BindView(R.id.ll_record)
    LinearLayout ll_record;
    @BindView(R.id.iv_sure)
    ImageView iv_sure;
    @BindView(R.id.iv_add_pics)
    ImageView iv_add_pics;
    @BindView(R.id.iv_add_voice)
    ImageView iv_add_voice;
    @BindView(R.id.iv_withdraw)
    ImageView iv_withdraw;
    @BindView(R.id.iv_start_record)
    ImageView iv_start_record;
    @BindView(R.id.tv_record_state)
    TextView tv_record_state;
    @BindView(R.id.tv_record_time)
    TextView tv_record_time;


    @BindView(R.id.ll_voice)
    LinearLayout ll_voice;
    @BindView(R.id.iv_image_gif)
    ImageView iv_image_gif;
    @BindView(R.id.tv_music_time)
    TextView tv_music_time;

    @BindView(R.id.tv_time_task)
    TextView tv_time_task;
    @BindView(R.id.iv_icon)
    ImageView iv_icon;


    @BindView(R.id.rl_recording)
    RelativeLayout rl_recording;

    BaseQuickAdapter mAdapter;
    List<String> imageList;

    private TakePhoto takePhoto;
    private InvokeParam invokeParam;

    int photoType = 1;//1为照相，2为相册

    private final int IMG_MAX_NUM = 9;

    private String AudioUrl = "";
    private static final int AUDIO_STATE1 = 1;//可录音
    private static final int AUDIO_STATE2 = 2;//录音中，点击暂停
    private static final int AUDIO_STATE3 = 3;//可播放
    private static final int AUDIO_STATE4 = 4;//播放中，点击停止
    private int audioState = AUDIO_STATE1;
    private MediaPlayer mediaPlayer;//确定后的播放器
    private MediaPlayer mediaPlayer2;//录制时的播放器

    private TimeCountUtil2 mTimeCount;
    private TimeCountUtil3 mTimeCount3;
    boolean isPlay = false;

    AnimationDrawable animationDrawable;

    String audio_time = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_publish_new;
    }

    @Override
    protected void initView() {
        tv_title.setText("发动态");
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText("发表");
        imageList = new ArrayList<>();

        et_content.setOnTouchListener(new View.OnTouchListener() {
            int touch_flag = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touch_flag++;
                if (touch_flag == 2) {
                    ll_record.setVisibility(View.GONE);
                    iv_add_voice.setImageResource(R.mipmap.fdt_yy);
                    touch_flag = 0;
                }
                return false;
            }
        });

    }

    @Override
    protected void initData() {
        recycleView.setLayoutManager(new GridLayoutManager(this, 3));
        recycleView.setHasFixedSize(true);
        mAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_dynamic_photo2, imageList) {
            @Override
            protected void convert(final BaseViewHolder helper, final String item) {
                ImageView iv_photo = helper.getView(R.id.iv_photo);
                ImageView iv_delete = helper.getView(R.id.iv_delete);

                Glide.with(PublishActivity.this).load(item).into(iv_photo);
                iv_delete.setVisibility(View.VISIBLE);
                iv_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //删除此图
                        imageList.remove(helper.getAdapterPosition());
                        if (imageList.isEmpty()) {
                            iv_add_voice.setImageResource(R.mipmap.fdt_yy);
                            iv_add_voice.setEnabled(true);
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        };
        recycleView.setAdapter(mAdapter);
    }

    @Override
    public void onSuccess(BaseModel result, String params) {
        finishLoad();
        switch (params) {
            case API.COMMON_AUDIO:
                SpareBean spareBean1 = (SpareBean) result.getData();
                OkHttpUtils.getInstance().dynamic_publish(SPUtils.getValue("token"), "", spareBean1.getAudioUrl() + "",audio_time, et_content.getText().toString(), PublishActivity.this);
                break;
            case API.DYNAMIC_PUBLISH:
                DynamicFragment.isRefresh = true;
                ToastUtil.toastShow("发布成功");
                finish();
                break;
        }
    }

    @Override
    public void onError(String result) {
        finishLoad();
        ToastUtil.toastShow(result);
    }

    @OnClick({R.id.iv_back, R.id.tv_right, R.id.iv_add_pics, R.id.iv_add_voice, R.id.iv_start_record, R.id.iv_withdraw, R.id.iv_sure,
            R.id.iv_del_voice, R.id.rl_voice})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_add_pics:
                //添加图片
                if (!AudioUrl.isEmpty()) {
                    ToastUtil.toastShow("不能同时上传音频和图片");
                    return;
                }
                ll_record.setVisibility(View.GONE);

                if (imageList.size() >= IMG_MAX_NUM) {
                    ToastUtil.toastShow("最多选择9张");
                } else {
                    takePhoto();
                }
                break;
            case R.id.iv_add_voice:
                //添加音频
                ll_record.setVisibility(View.VISIBLE);
                iv_add_voice.setImageResource(R.mipmap.yy_pre);
                if (imageList.size() >= 1) {
                    ToastUtil.toastShow("不能同时上传音频和图片");
                    return;
                }
                KeyBoardUtil.closeKeybord(et_content, this);
                break;
            case R.id.tv_right:
                //发表
                if (TextUtils.isEmpty(et_content.getText().toString())) {
                    ToastUtil.toastShow("内容不能为空");
                    return;
                }
                mLoading.show();
                if (imageList.size() >= (IMG_MAX_NUM - 8)) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            StringBuilder base64 = new StringBuilder();
                            for (int i = 0; i < imageList.size() - 1; i++) {
                                File f = new File(imageList.get(i));
                                base64.append(FileUtils.fileToBase64(f) + ",");
                            }
                            base64.delete(base64.length() - 1, base64.length());
                            OkHttpUtils.getInstance().dynamic_publish(SPUtils.getValue("token"), base64.toString(), "",audio_time, et_content.getText().toString(), PublishActivity.this);
                        }
                    }).start();
                } else if (!AudioUrl.isEmpty()) {
                    OkHttpUtils.getInstance().common_audio(AudioUrl, this);
                } else {
                    OkHttpUtils.getInstance().dynamic_publish(SPUtils.getValue("token"), "", "",audio_time, et_content.getText().toString(), PublishActivity.this);
                }
                break;
            case R.id.iv_start_record:
                switch (audioState) {
                    case AUDIO_STATE1:
                        if (mTimeCount3 == null) {
                            iv_icon.setImageResource(R.drawable.animlis_record);
                            animationDrawable = (AnimationDrawable) iv_icon.getDrawable();
                            animationDrawable.start();
                            mTimeCount3 = new TimeCountUtil3(1800 * 1000, 1000, tv_time_task, new TimeCountUtil3.OnFinish() {
                                @Override
                                public void finish() {
                                    recordingFinish();
                                    rl_recording.setVisibility(View.GONE);
                                    animationDrawable.stop();
                                    mTimeCount3 = null;
                                }
                            });
                            mTimeCount3.start();
                        }
                        rl_recording.setVisibility(View.VISIBLE);
                        iv_start_record.setImageResource(R.mipmap.tzly);
                        onRecord(true);
                        break;
                    case AUDIO_STATE2:
                        animationDrawable.stop();
                        rl_recording.setVisibility(View.GONE);
                        mTimeCount3.cancel();
                        mTimeCount3.onFinish();
                        mTimeCount3 = null;
//                        ToastUtil.toastShow("暂停录音");
                        onRecord(false);
                        break;
                    case AUDIO_STATE3:
//                        ToastUtil.toastShow("播放录音");
                        startAudio(SPUtils.getValue("audio_path"));
                        iv_start_record.setImageResource(R.mipmap.tzly);
                        break;
                    case AUDIO_STATE4:
//                        ToastUtil.toastShow("暂停播放");
                        iv_start_record.setImageResource(R.mipmap.zt);
                        audioState = AUDIO_STATE3;
                        if (mediaPlayer2.isPlaying()) {
                            mediaPlayer2.pause();
                        }
                        break;
                }
                break;
            case R.id.iv_withdraw:
                //撤回
                withdraw();
                break;
            case R.id.iv_sure:
                //确认
                AudioUrl = SPUtils.getValue("audio_path");
                //确认获取语音后，照相功能禁止使用
                iv_add_pics.setImageResource(R.mipmap.to_nor);
                iv_add_pics.setEnabled(false);

                ll_record.setVisibility(View.GONE);
                iv_add_voice.setImageResource(R.mipmap.fdt_yy);
                recycleView.setVisibility(View.GONE);
                ll_voice.setVisibility(View.VISIBLE);
                if (mediaPlayer2 != null && mediaPlayer2.isPlaying())
                    mediaPlayer2.pause();
                isPlay = false;
                play();
                break;
            case R.id.iv_del_voice:
                iv_add_pics.setEnabled(true);
                iv_add_pics.setImageResource(R.mipmap.fdt_tp);
                recycleView.setVisibility(View.VISIBLE);
                ll_voice.setVisibility(View.GONE);
                withdraw();
                SPUtils.putValue("audio_path", "");
                if (mediaPlayer.isPlaying())
                    mediaPlayer.stop();
                if (mediaPlayer2 != null)
                    mediaPlayer2.stop();
                break;
            case R.id.rl_voice:
                //播放
                Log.e(TAG, "onViewClicked: isPlay = " + isPlay);
                if (isPlay) {
                    Glide.with(this).load(R.mipmap.yystop).into(iv_image_gif);
                    if (mTimeCount != null) {
                        mTimeCount.cancel();
                        mTimeCount.onFinish();
                    }
                    mediaPlayer.pause();
                } else {
                    Glide.with(this).load(R.mipmap.bofangdh).into(iv_image_gif);
                    play();
                    mTimeCount = new TimeCountUtil2(mediaPlayer.getDuration() + 500, 1000, tv_music_time);
                    mTimeCount.start();
                    mediaPlayer.start();
                }
                isPlay = !isPlay;
                break;
        }
    }

    //撤回
    private void withdraw() {
        tv_record_state.setText("开始录音");
        iv_withdraw.setVisibility(View.GONE);
        iv_sure.setVisibility(View.GONE);
        tv_record_time.setVisibility(View.GONE);
        AudioUrl = "";
        audioState = AUDIO_STATE1;
    }

    /**
     * 录音结束，可试听和选中
     */
    private void recordingFinish() {
        tv_record_state.setText("试听");
        iv_withdraw.setVisibility(View.VISIBLE);
        iv_sure.setVisibility(View.VISIBLE);
        audioState = AUDIO_STATE3;
        iv_start_record.setImageResource(R.mipmap.zt);
    }

    private void play() {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.reset();
            } else {
                mediaPlayer = new MediaPlayer();
            }
            // 设置音乐播放源
            mediaPlayer.setDataSource(SPUtils.getValue("audio_path"));
            // 准备
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer media) {
                    mediaPlayer.stop();
                    Glide.with(PublishActivity.this).load(R.mipmap.yystop).into(iv_image_gif);
                    isPlay = false;
                }
            });
            tv_record_time.setVisibility(View.VISIBLE);
            audio_time = mediaPlayer.getDuration()+"";
            tv_record_time.setText((mediaPlayer.getDuration() + 500) / 1000 + "s");
        } catch (IOException e) {
            e.printStackTrace();
        }
        tv_music_time.setText((mediaPlayer.getDuration() + 500) / 1000 + "s");
    }

    //时间转换类，将得到的音乐时间毫秒转换为时分秒格式
    private String formatTime(int length) {
        Date date = new Date(length);
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        String totalTime = sdf.format(date);
        return totalTime;
    }

    /**
     * 开始、结束录音
     *
     * @param start true：开始 false：停止
     */
    private void onRecord(boolean start) {
        Intent intent = new Intent(this, RecordingService.class);
        if (start) {
            AudioUrl = "";
            tv_record_state.setText("停止录音");
            audioState = AUDIO_STATE2;
//            tv_record_state.setImageResource(R.drawable.animlis_recording);
//            animation = (AnimationDrawable) tv_record_state.getDrawable();
//            animation.start();
            File folder = new File(Environment.getExternalStorageDirectory() + "/SoundRecorder");
            if (!folder.exists()) {
                folder.mkdir();
            }
            startService(intent);
        } else {
            stopService(intent);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            recordingFinish();
        }
    }

    /**
     * 播放录音
     *
     * @param audioUrl
     */
    private void startAudio(String audioUrl) {
        audioState = AUDIO_STATE4;
        if (mediaPlayer2 != null) {
            mediaPlayer2.reset();
        } else {
            mediaPlayer2 = new MediaPlayer();
        }
        try {
            mediaPlayer2.setDataSource(audioUrl);
            mediaPlayer2.prepare();
            mediaPlayer2.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer media) {
                    audioState = AUDIO_STATE3;
                    mediaPlayer2.stop();
                    iv_start_record.setImageResource(R.mipmap.zt);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mediaPlayer2.start();
            tv_record_time.setVisibility(View.VISIBLE);
            tv_record_time.setText(formatTime(mediaPlayer2.getDuration()));
        }
    }

    private void takePhoto() {
//        final CropOptions cropOptions = new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create();
        DialogUtil.showTakePhoto(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(getExternalCacheDir(), System.currentTimeMillis() + ".png");
                final Uri imageUri = Uri.fromFile(file);
                photoType = 1;
                takePhoto.onPickFromCapture(imageUri);
//                takePhoto.onPickFromCaptureWithCrop(imageUri, cropOptions);
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoType = 2;
//                takePhoto.onPickMultipleWithCrop(10 - imageList.size(), cropOptions);
                takePhoto.onPickMultiple(IMG_MAX_NUM - imageList.size());
            }
        }).show();
    }

    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        takePhoto.onEnableCompress(new CompressConfig.Builder().setMaxSize(800 * 1024).create(), true);
        return takePhoto;
    }

    @Override
    public void takeSuccess(TResult result) {
        iv_add_voice.setImageResource(R.mipmap.yy_nor);
        iv_add_voice.setEnabled(false);
        if (photoType == 1) {
            imageList.add(imageList.size(), result.getImage().getCompressPath());
        } else {
            for (int i = 0; i < result.getImages().size(); i++) {
                imageList.add(imageList.size(), result.getImages().get(i).getCompressPath());
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        Log.e(TAG, "takeFail: " + msg);
    }

    @Override
    public void takeCancel() {
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //以下代码为处理Android6.0、7.0动态权限所需
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (mediaPlayer2 != null) {
            mediaPlayer2.stop();
            mediaPlayer2.release();
            mediaPlayer2 = null;
        }
    }
}