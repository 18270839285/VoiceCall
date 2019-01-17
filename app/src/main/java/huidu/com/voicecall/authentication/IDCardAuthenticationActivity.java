package huidu.com.voicecall.authentication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
import huidu.com.voicecall.utils.ToastUtil;

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
        if (face_img.isEmpty()) {
            ToastUtil.toastShow("请先上传身份证正面照");
            return;
        }
        if (back_img.isEmpty()) {
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
        OkHttpUtils.getInstance().auth_identity(API.TOKEN_TEST, face_img, back_img, realname, id_card, this);
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
                PicturePicker picker1 = PicturePicker.init(this);
                picker1.showPickDialog(this);
                break;
            case R.id.iv_back_face:
                IMG_TYPE = 2;
                PicturePicker picker2 = PicturePicker.init(this);
                picker2.showPickDialog(this);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    private String cropPath;
    private File tempFile;
    private File newFile;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PicturePickerFragment.PICK_TACK_PHOTO && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                MiPictureHelper.cropImg(this, uri);
//                MiPictureHelper.cropHandCard2(this, uri);
            }
        } else if (requestCode == PicturePickerFragment.PICK_SYSTEM_PHOTO && resultCode == Activity.RESULT_OK) {//PICK_TACK_PHOTO
            if (MiPictureHelper.hasSdcard()) {
//                 Bitmap bitmap = decodeUriAsBitmap(data.getData());
                tempFile = new File(Environment.getExternalStorageDirectory(), API.temp_filename);
                String path = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "voiceCall";//新文件地址
                newFile = new File(Environment.getExternalStorageDirectory(), path);
                tempFile.renameTo(newFile);
                cropPath = Uri.fromFile(newFile).getPath();
                File newFile1 = CompressHelper.getDefault(this).compressToFile(new File(cropPath));
                Bitmap photo = BitmapFactory.decodeFile(newFile1.getPath());
                if (IMG_TYPE ==1){
                    iv_face.setImageBitmap(photo);
                    face_img = FileUtils.fileToBase64(newFile1);
                }else {
                    iv_back_face.setImageBitmap(photo);
                    back_img = FileUtils.fileToBase64(newFile1);
                }
            } else {
                ToastUtil.toastShow("未找到存储卡，无法存储照片！");
                return;
            }
        } else if (requestCode == PicturePicker.PHOTO_REQUEST_CUT) {
            if (data == null)
                return;
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap photo = extras.getParcelable("data");
                File newFile = saveBitmapFile(photo);
                if (IMG_TYPE ==1){
                    iv_face.setImageBitmap(photo);
                    face_img = FileUtils.fileToBase64(newFile);
                }else {
                    iv_back_face.setImageBitmap(photo);
                    back_img = FileUtils.fileToBase64(newFile);
                }
            } else {
                Uri uri = data.getData();
                if (uri != null) {
                    Bitmap photo = BitmapFactory.decodeFile(uri.getPath());
                    File newFile = saveBitmapFile(photo);
                    if (IMG_TYPE ==1){
                        iv_face.setImageBitmap(photo);
                        face_img = FileUtils.fileToBase64(newFile);
                    }else {
                        iv_back_face.setImageBitmap(photo);
                        back_img = FileUtils.fileToBase64(newFile);
                    }
                }
            }
        }
    }

    public File saveBitmapFile(Bitmap bitmap) {
        String path = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "voiceCall";
        File file = new File(Environment.getExternalStorageDirectory(), path);//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    private Bitmap decodeUriAsBitmap(Uri uri){
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }
}
