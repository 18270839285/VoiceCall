package huidu.com.voicecall.authentication;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.devio.takephoto.app.TakePhoto;
import org.devio.takephoto.app.TakePhotoImpl;
import org.devio.takephoto.compress.CompressConfig;
import org.devio.takephoto.model.CropOptions;
import org.devio.takephoto.model.InvokeParam;
import org.devio.takephoto.model.TContextWrap;
import org.devio.takephoto.model.TResult;
import org.devio.takephoto.permission.InvokeListener;
import org.devio.takephoto.permission.PermissionManager;
import org.devio.takephoto.permission.TakePhotoInvocationHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import huidu.com.voicecall.R;
import huidu.com.voicecall.audio.RecordingService;
import huidu.com.voicecall.base.BaseActivity;
import huidu.com.voicecall.bean.AnchorType;
import huidu.com.voicecall.bean.SpareBean;
import huidu.com.voicecall.http.API;
import huidu.com.voicecall.http.BaseModel;
import huidu.com.voicecall.http.OkHttpUtils;
import huidu.com.voicecall.http.RequestFinish;
import huidu.com.voicecall.utils.DialogUtil;
import huidu.com.voicecall.utils.FileUtils;
import huidu.com.voicecall.utils.SPUtils;
import huidu.com.voicecall.utils.ToastUtil;

/**
 * 主播认证
 */
public class AnchorAuthenticationActivity extends BaseActivity implements RequestFinish, TakePhoto.TakeResultListener, InvokeListener {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.et_price)
    EditText et_price;
    @BindView(R.id.et_introduce)
    EditText et_introduce;
    @BindView(R.id.iv_cover)
    ImageView iv_cover;
    @BindView(R.id.iv_record)
    ImageView iv_record;
    @BindView(R.id.tv_text_num)
    TextView tv_text_num;
    @BindView(R.id.tv_anchor_type)
    TextView tv_anchor_type;
    @BindView(R.id.tv_re_recording)
    TextView tv_re_recording;//重新录制

    private MediaPlayer mediaPlayer;

    private String AudioUrl = "";
    private String CoverUrl = "";
    private List<AnchorType> typeList = new ArrayList<>();

    private CountDownTimer timer;
    private boolean isSelfCancel = false;

    private static final int AUDIO_STATE1 = 1;//可录音
    private static final int AUDIO_STATE2 = 2;//录音中，点击暂停
    private static final int AUDIO_STATE3 = 3;//可播放
    private static final int AUDIO_STATE4 = 4;//播放中，点击停止
    private int audioState = AUDIO_STATE1;

    private AnimationDrawable animation = null;

    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    private File newFile;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_anchor_authentication;
    }

    @Override
    protected void initView() {
        tv_title.setText("主播认证");
        timer = new CountDownTimer(15000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (!isSelfCancel) {
                    onRecord(false);
                }
                isSelfCancel = false;
            }
        };
        et_introduce.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tv_text_num.setText(et_introduce.getText().toString().length() + "");

            }
        });
    }

    @Override
    protected void initData() {
        OkHttpUtils.getInstance().auth_anchor_type(SPUtils.getValue("token"), this);
    }

    @Override
    public void onSuccess(BaseModel result, String params) {
        switch (params) {
            case API.COMMON_IMAGE_UPLOAD:
                finishLoad();
                SpareBean spareBean = (SpareBean) result.getData();
                CoverUrl = spareBean.getImage_url();
                Glide.with(this).load(CoverUrl).apply(new RequestOptions().error(R.mipmap.wd_tx_nor)).into(iv_cover);
                break;
            case API.COMMON_AUDIO:
                finishLoad();
                SpareBean spareBean1 = (SpareBean) result.getData();
                AudioUrl = spareBean1.getAudioUrl();
                startAudio(AudioUrl);
                break;
            case API.AUTH_ANCHOR_TYPE:
                typeList = (List<AnchorType>) result.getData();
                if (!typeList.isEmpty())
                    tv_anchor_type.setText(typeList.get(0).getType_name());
                break;
            case API.AUTH_ANCHOR:
                finishLoad();
                finish();
                break;
        }
    }

    @Override
    public void onError(String result) {
        finishLoad();
        ToastUtil.toastShow(result);
    }

    @OnClick({R.id.iv_back, R.id.rl_anchor_type, R.id.tv_submit, R.id.iv_cover, R.id.iv_record, R.id.tv_re_recording})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_anchor_type:
                //选择主播类型
                DialogUtil.selectAnchorType(this, typeList, new DialogUtil.onItemClick() {
                    @Override
                    public void onClickItem(String string) {
                        tv_anchor_type.setText(string);
                    }
                });
                break;
            case R.id.tv_submit:
                //提交
                checkSubmit();
                break;
            case R.id.iv_cover:
                //封面
                File file = new File(getExternalCacheDir(), System.currentTimeMillis() + ".png");
                final Uri imageUri = Uri.fromFile(file);
                final CropOptions cropOptions = new CropOptions.Builder().setAspectX(45).setAspectY(29).setWithOwnCrop(true).create();
                DialogUtil.showTakePhoto(this, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        takePhoto.onPickFromCaptureWithCrop(imageUri, cropOptions);
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        takePhoto.onPickFromGalleryWithCrop(imageUri, cropOptions);
                    }
                }).show();
                break;
            case R.id.tv_re_recording:
                //重新录制
                onRecord(true);
                break;
            case R.id.iv_record:
                switch (audioState) {
                    case AUDIO_STATE1:
                        onRecord(true);
                        break;
                    case AUDIO_STATE2:
                        onRecord(false);
                        break;
                    case AUDIO_STATE3:
                        if (AudioUrl.isEmpty()) {
                            mLoading.show();
                            OkHttpUtils.getInstance().common_audio(SPUtils.getValue("audio_path"), this);
                        } else {
                            startAudio(AudioUrl);
                        }
                        break;
                    case AUDIO_STATE4:
                        audioState = AUDIO_STATE3;
                        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                            mediaPlayer.pause();
                            iv_record.setImageResource(R.mipmap.zbrz_bf);
                        }
                        break;
                }
                break;
        }
    }

    private void checkSubmit() {
        if (et_price.getText().toString().isEmpty()) {
            ToastUtil.toastShow("价格不能为空");
            return;
        }
        if (CoverUrl.isEmpty()) {
            ToastUtil.toastShow("封面图不能为空");
            return;
        }
        if (AudioUrl.isEmpty()) {
            ToastUtil.toastShow("声音介绍不能为空");
            return;
        }
        if (et_introduce.getText().toString().isEmpty()) {
            ToastUtil.toastShow("技能说明不能为空");
            return;
        }
        mLoading.show();
        String type = "";
        for (int i = 0; i < typeList.size(); i++) {
            if (typeList.get(i).getType_name().equals(tv_anchor_type.getText().toString())) {
                type = typeList.get(i).getId();
            }
        }
        OkHttpUtils.getInstance().auth_anchor(SPUtils.getValue("token"), type, et_price.getText().toString(),
                CoverUrl, AudioUrl, et_introduce.getText().toString(), this);
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
            timer.start();
            ToastUtil.toastShow("开始录音...");
            audioState = AUDIO_STATE2;
            iv_record.setImageResource(R.drawable.animlis_recording);
            animation = (AnimationDrawable) iv_record.getDrawable();
            animation.start();
            File folder = new File(Environment.getExternalStorageDirectory() + "/SoundRecorder");
            if (!folder.exists()) {
                folder.mkdir();
            }
            startService(intent);
        } else {
            ToastUtil.toastShow("录音结束...");
            tv_re_recording.setVisibility(View.VISIBLE);
            audioState = AUDIO_STATE3;
            timer.cancel();
            if (animation != null && animation.isRunning()) {
                animation.stop();
                animation = null;
            }
            iv_record.setImageResource(R.mipmap.zbrz_bf);
            stopService(intent);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
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
        takePhoto.onEnableCompress(new CompressConfig.Builder().setMaxSize(1000 * 1024).create(), true);
        return takePhoto;
    }

    @Override
    public void takeSuccess(TResult result) {
        newFile = new File(result.getImage().getCompressPath());
        mLoading.show();
        OkHttpUtils.getInstance().common_image_upload(FileUtils.fileToBase64(newFile), this);
    }

    @Override
    public void takeFail(TResult result, String msg) {
        Log.e(TAG, "takeFail: "+msg );
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

    /**
     * 播放录音
     *
     * @param audioUrl
     */
    private void startAudio(String audioUrl) {
        audioState = AUDIO_STATE4;
        mediaPlayer = new MediaPlayer();
        iv_record.setImageResource(R.mipmap.zbrz_zt);
        try {
            mediaPlayer.setDataSource(audioUrl);
            //3 准备播放
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer media) {
                    mediaPlayer.start();
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer media) {
                    mediaPlayer.stop();
                    audioState = AUDIO_STATE3;
                    iv_record.setImageResource(R.mipmap.zbrz_bf);
                    mediaPlayer = null;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer = null;
        }
        if (timer != null) {
            timer.cancel();
        }
    }
}
