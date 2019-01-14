package huidu.com.voicecall.utils;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

import java.io.File;

import huidu.com.voicecall.R;

public class PicturePickerFragment {
    public static final int PICK_SYSTEM_PHOTO = 124;
    public static final int PICK_TACK_PHOTO = 135;
    public static final int PHOTO_REQUEST_CUT = 125;// 结果
    public static final int NONE = 0x3;
    //相机权限返回CODE
    int MY_PERMISSIONS_REQUEST_CAMERA = 188;

    private Uri fileUri;
    private String path;
    public static final String ICON_TYPE = ".png";

    public static final String INTENT_IMAGE_PATH = "INTENT_IMAGE_PATH";
    public static final String INTENT_IMAGE_URI = "INTENT_IMAGE_URI";
    public static final String INTENT_CROP_X = "INTENT_CROP_X";
    public static final String INTENT_CROP_Y = "INTENT_CROP_Y";
    public static final String IMG_PATH = "/punchcard/image/";
    public static final String CACHE_PATH = "/punchcard/cache/";
    public static final String IMAGE_SUBFIX = ".png";
    private Fragment act;

    private PicturePickerFragment(Fragment act) {
        this.act = act;
    }

    public static PicturePickerFragment init(Fragment act) {
        return new PicturePickerFragment(act);
    }

    ///
    public void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        act.startActivityForResult(intent, PICK_TACK_PHOTO);

    }

    private String fileName = "";

    public void initCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri fileUri = Uri
                .fromFile(new File(Environment.getExternalStorageDirectory(),
                        "head.jpg"));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        act.startActivityForResult(intent, PICK_SYSTEM_PHOTO);
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = new CursorLoader(act.getActivity(), uri, projection, null, null, null)
                .loadInBackground();
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            path = cursor.getString(column_index);
            cursor.close();
        }
        if (path == null) {
            path = uri.toString().substring(7, uri.toString().length());
        }
        return path;
    }

    private int px2dp(int px) {
        float scale = act.getResources().getDisplayMetrics().scaledDensity;
        return (int) (px * scale + 0.5f);
    }

    /**
     * 选择图片
     *
     * @param context
     * @return
     */
    public AlertDialog showPickDialog(final Context context) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context,
                R.style.DialogStyleBottom).create();
        alertDialog.show();
        final Window window = alertDialog.getWindow();
        window.setContentView(R.layout.item_popupwindow);
        window.setLayout(
                window.getContext().getResources().getDisplayMetrics().widthPixels,
                LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.dialog_up_down_animation);
        window.clearFlags(LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        alertDialog.setCanceledOnTouchOutside(true);

        window.findViewById(R.id.cancel_textview).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        alertDialog.dismiss();
                    }
                });

        window.findViewById(R.id.frome_photo_textview).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectImage();
                        alertDialog.dismiss();
                    }
                });

        window.findViewById(R.id.take_pic_textview).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {//判断是否为6.0以上系统
                            String[] perms = {Manifest.permission.CAMERA};
                            //如果未授权
                            if (ContextCompat.checkSelfPermission(act.getActivity(),
                                    Manifest.permission.CAMERA)
                                    != PackageManager.PERMISSION_GRANTED) {
                                //请求权限
                                ActivityCompat.requestPermissions(act.getActivity(), perms, MY_PERMISSIONS_REQUEST_CAMERA);
                                //判断是否需要 向用户解释，为什么要申请该权限
                                ActivityCompat.shouldShowRequestPermissionRationale(act.getActivity(),
                                        Manifest.permission.CAMERA);
                            } else {
                                initCamera();
                            }
                        } else
                            initCamera();
                        alertDialog.dismiss();
                    }
                });

        return alertDialog;
    }
}
