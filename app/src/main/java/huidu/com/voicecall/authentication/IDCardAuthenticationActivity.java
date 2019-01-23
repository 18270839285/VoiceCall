package huidu.com.voicecall.authentication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.ocr.ui.camera.CameraActivity;
import com.bumptech.glide.Glide;
import com.nanchen.compresshelper.CompressHelper;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import huidu.com.voicecall.R;
import huidu.com.voicecall.base.BaseActivity;
import huidu.com.voicecall.http.API;
import huidu.com.voicecall.http.BaseModel;
import huidu.com.voicecall.http.OkHttpUtils;
import huidu.com.voicecall.http.RequestFinish;
import huidu.com.voicecall.utils.FileUtils;
import huidu.com.voicecall.utils.Loading;
import huidu.com.voicecall.utils.MiPictureHelper;
import huidu.com.voicecall.utils.PicturePicker;
import huidu.com.voicecall.utils.PicturePickerFragment;
import huidu.com.voicecall.utils.SPUtils;
import huidu.com.voicecall.utils.ToastUtil;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.baidu.ocr.sdk.OCR;

/**
 * 身份认证
 */
public class IDCardAuthenticationActivity extends BaseActivity implements RequestFinish {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.et_real_name)
    EditText et_real_name;
    @BindView(R.id.et_id_card)
    EditText et_id_card;
    @BindView(R.id.iv_face)
    ImageView iv_face;
    @BindView(R.id.iv_back_face)
    ImageView iv_back_face;

    String face_img = "";
    String back_img = "";
    String realname = "";
    String id_card = "";
    String FACE_IMGBASE64 = "";
    String BACK_IMGBASE64 = "";
    Loading loading;

    int IMG_TYPE =1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_idcard_authentication;
    }

    @Override
    protected void initView() {
        tv_title.setText("身份证认证");
        loading = new Loading(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onSuccess(BaseModel result, String params) {
        loading.dismiss();
        switch (params) {
            case API.AUTH_IDENTITY:
                jumpTo(FaceRecognitionActivity.class);
                break;
        }
    }

    @Override
    public void onError(String result) {
        loading.dismiss();
        ToastUtil.toastShow(result);
    }

    private void checkIDCard() {
        if (FACE_IMGBASE64.isEmpty()) {
            ToastUtil.toastShow("请先上传身份证正面照");
            return;
        }
        if (BACK_IMGBASE64.isEmpty()) {
            ToastUtil.toastShow("请先上传身份证反面照");
            return;
        }
        realname = et_real_name.getText().toString();
        if (realname.isEmpty()) {
            ToastUtil.toastShow("请输入姓名");
            return;
        }
        id_card = et_id_card.getText().toString();
        if (id_card.isEmpty()) {
            ToastUtil.toastShow("请输入身份证号");
            return;
        }
        loading.show();
        OkHttpUtils.getInstance().auth_identity(SPUtils.getValue("token"), FACE_IMGBASE64, BACK_IMGBASE64, realname, id_card, this);
    }

    @OnClick({R.id.iv_back, R.id.tv_next, R.id.iv_face, R.id.iv_back_face})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_next:
                checkIDCard();

                break;
            case R.id.iv_face:
                IMG_TYPE = 1;
                scanFrontWithNativeQuality();
//                PicturePicker picker1 = PicturePicker.init(this);
//                picker1.showPickDialog(this);
                break;
            case R.id.iv_back_face:
                IMG_TYPE = 2;
                scanBackWithNativeQuality();
//                PicturePicker picker2 = PicturePicker.init(this);
//                picker2.showPickDialog(this);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    private static final int REQUEST_CODE_CAMERA = 102; //照相机扫描的请求码
    // 调用拍摄身份证正面（带本地质量控制）activity
    private void scanFrontWithNativeQuality() {
        face_img = FileUtils.getSaveFile(getApplication()).getAbsolutePath() + SystemClock.currentThreadTimeMillis();
        Intent intent = new Intent(this, CameraActivity.class);
        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, face_img);
        //使用本地质量控制能力需要授权
        intent.putExtra(CameraActivity.KEY_NATIVE_TOKEN, OCR.getInstance().getLicense());
        //设置本地质量使用开启
        intent.putExtra(CameraActivity.KEY_NATIVE_ENABLE, true);
        //设置扫描的身份证的类型（正面front还是反面back）
        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    //调用拍摄身份证反面（带本地质量控制）activity
    private void scanBackWithNativeQuality() {
        back_img = FileUtils.getSaveFile(getApplication()).getAbsolutePath() + SystemClock.currentThreadTimeMillis();
        Intent intent = new Intent(this, CameraActivity.class);
        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH, back_img);
        intent.putExtra(CameraActivity.KEY_NATIVE_TOKEN,
                OCR.getInstance().getLicense());
        intent.putExtra(CameraActivity.KEY_NATIVE_ENABLE,
                true);
        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_BACK);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }
    private String cropPath;
    private File tempFile;
    private File newFile;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PicturePickerFragment.PICK_TACK_PHOTO && resultCode == Activity.RESULT_OK) {
//            if (data != null) {
//                // 得到图片的全路径
//                Uri uri = data.getData();
////                MiPictureHelper.cropImg(this, uri);
//                MiPictureHelper.cropHandCard2(this, uri);
//            }
//        } else if (requestCode == PicturePickerFragment.PICK_SYSTEM_PHOTO && resultCode == Activity.RESULT_OK) {//PICK_TACK_PHOTO
//            if (MiPictureHelper.hasSdcard()) {
////                 Bitmap bitmap = decodeUriAsBitmap(data.getData());
//                tempFile = new File(API.FILE_DIR, API.temp_filename);
//                String path = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "voiceCall";//新文件地址
//                newFile = new File(API.FILE_DIR, path);
//                tempFile.renameTo(newFile);
//                cropPath = Uri.fromFile(newFile).getPath();
//                File newFile1 = CompressHelper.getDefault(this).compressToFile(new File(cropPath));
//                Bitmap photo = BitmapFactory.decodeFile(newFile1.getPath());
//                if (IMG_TYPE ==1){
//                    iv_face.setImageBitmap(photo);
//                    face_img = FileUtils.fileToBase64(newFile1);
//                }else {
//                    iv_back_face.setImageBitmap(photo);
//                    back_img = FileUtils.fileToBase64(newFile1);
//                }
//            } else {
//                ToastUtil.toastShow("未找到存储卡，无法存储照片！");
//                return;
//            }
//        } else if (requestCode == PicturePicker.PHOTO_REQUEST_CUT) {
//            if (data == null)
//                return;
//            Bundle extras = data.getExtras();
//            if (extras != null) {
//                Bitmap photo = extras.getParcelable("data");
//                File newFile = FileUtils.saveBitmapFile(photo);
//                if (IMG_TYPE ==1){
//                    iv_face.setImageBitmap(photo);
//                    face_img = FileUtils.fileToBase64(newFile);
//                }else {
//                    iv_back_face.setImageBitmap(photo);
//                    back_img = FileUtils.fileToBase64(newFile);
//                }
//            } else {
//                Uri uri = data.getData();
//                if (uri != null) {
//                    Bitmap photo = BitmapFactory.decodeFile(uri.getPath());
//                    File newFile = FileUtils.saveBitmapFile(photo);
//                    if (IMG_TYPE ==1){
//                        iv_face.setImageBitmap(photo);
//                        face_img = FileUtils.fileToBase64(newFile);
//                    }else {
//                        iv_back_face.setImageBitmap(photo);
//                        back_img = FileUtils.fileToBase64(newFile);
//                    }
//                }
//            }
//        }
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String filePath = null;
                String contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE);
                if (contentType.equals(CameraActivity.CONTENT_TYPE_ID_CARD_FRONT)) {
                    filePath = face_img;
                } else {
                    filePath = back_img;
                }
                Log.i(TAG, "onActivityResult: filePath:" + filePath);
                if (!TextUtils.isEmpty(contentType)) {
                    newFile = CompressHelper.getDefault(this).compressToFile(new File(filePath));
//                    base64Str = FileUtils.fileToBase64(newFile);
                    if (IMG_TYPE == 1) {
                        Glide.with(this).load(filePath).into(iv_face);
                        FACE_IMGBASE64 = FileUtils.fileToBase64(newFile).trim();
                    } else if (IMG_TYPE == 2) {
                        BACK_IMGBASE64 = FileUtils.fileToBase64(newFile).trim();
                        Glide.with(this).load(filePath).into(iv_back_face);
                    }
                }
            }
        }
    }

}
