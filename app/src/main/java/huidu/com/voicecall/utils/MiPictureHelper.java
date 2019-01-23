package huidu.com.voicecall.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import huidu.com.voicecall.http.API;

import static huidu.com.voicecall.utils.PicturePicker.PHOTO_REQUEST_CUT;

/**
 * 小米手机获取本地相册图片出现空指针异常处理类
 *
 * @author dec
 */

public class MiPictureHelper {

    private static int mWidth;//图片宽
    private static int mHeight;//图片高

    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;

    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;

    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

//    /**
//     * 剪切头像
//     *
//     * @param uri
//     * @function:
//     * @author:Jerry
//     * @date:2013-12-30
//     */
//    public static String cropHeadImg(Activity activity, Uri uri) {
//        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");//获取当前时间，进一步转化为字符串
//        String str = format.format(new Date());
//        String path = str + API.temp_filename;
//        File tempFile = new File(Environment.getExternalStorageDirectory(), path);
//        // 裁剪图片意图
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uri, "image/*");
//        intent.putExtra("crop", "true");
//        // 裁剪框的比例，1：1
//        intent.putExtra("aspectX", 0.1);
//        intent.putExtra("aspectY", 0.1);
//        // 图片格式
//        intent.putExtra("noFaceDetection", true);// 取消人脸识别
//        // intent.putExtra("return-data", true);// true:不返回uri，false：返回uri
//        intent.putExtra("return-data", false);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
//        activity.startActivityForResult(intent, PicturePickerFragment.PHOTO_REQUEST_CUT);
//        return tempFile.getAbsolutePath();
//    }


    public static String cropSquareCard(Activity activity, Uri uri) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");//获取当前时间，进一步转化为字符串
        String str = format.format(new Date());
        String path = str + API.temp_filename;
        File tempFile = new File(API.FILE_DIR, path);
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 0.1);
        intent.putExtra("aspectY", 0.1);
        intent.putExtra("return-data", false);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        activity.startActivityForResult(intent, PHOTO_REQUEST_CUT);
        return tempFile.getAbsolutePath();
    }

    private static final String TAG = "MiPictureHelper";

    /**
     * 手持身份证
     *
     * @param activity
     * @param uri
     * @return
     */
    public static String cropHandCard(Activity activity, Uri uri) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");//获取当前时间，进一步转化为字符串
        String str = format.format(new Date());
        String path = str + API.temp_filename;
        File tempFile = new File(API.FILE_DIR, path);
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 0.1);
        intent.putExtra("aspectY", 0.1);
        intent.putExtra("return-data", false);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        activity.startActivityForResult(intent, PHOTO_REQUEST_CUT);
        return tempFile.getAbsolutePath();
    }
    /**
     * 获取头像
     *
     * @param activity
     * @param uri
     * @return
     */
    public static File cropHandCard1(Activity activity, Uri uri) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");//获取当前时间，进一步转化为字符串
        String str = format.format(new Date());
        String path = str + API.temp_filename;
        File tempFile = new File(Environment.getExternalStorageDirectory(), path);
//        File tempFile = new File(API.FILE_DIR, path);
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("return-data", true);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        activity.startActivityForResult(intent, PHOTO_REQUEST_CUT);
        return tempFile;
    }
    /**
     * 手持身份证
     *
     * @param activity
     * @param uri
     * @return
     */
    public static File cropHandCard2(Activity activity, Uri uri) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");//获取当前时间，进一步转化为字符串
        String str = format.format(new Date());
        String path = str + API.temp_filename;
        File tempFile = new File(Environment.getExternalStorageDirectory(), path);
//        File tempFile = new File(API.FILE_DIR, path);
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
//        intent.putExtra("outputX", 200);
//        intent.putExtra("outputY", 200);
        intent.putExtra("aspectX", 16);
        intent.putExtra("aspectY", 11);
        intent.putExtra("return-data", true);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        activity.startActivityForResult(intent, PicturePicker.PHOTO_REQUEST_CUT);
        return tempFile;
    }
    /**
     * 获取封面
     *
     * @param activity
     * @param uri
     * @return
     */
    public static File cropHandCard4(Activity activity, Uri uri) {
//        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");//获取当前时间，进一步转化为字符串
//        String str = format.format(new Date());
//        String path = str + API.temp_filename;
//        String path = API.temp_filename;
        File tempFile = new File(Environment.getExternalStorageDirectory(), API.temp_filename);
        if (!tempFile.exists()){
            try {
                tempFile.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
//        intent.putExtra("outputX", 640);
//        intent.putExtra("outputY", 440);
        intent.putExtra("aspectX", 45);
        intent.putExtra("aspectY", 29);
        intent.putExtra("return-data", true);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        activity.startActivityForResult(intent, PicturePicker.PHOTO_REQUEST_CUT);
        return tempFile;
    }

    public static void cropImg(Activity activity,Uri uri) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");//获取当前时间，进一步转化为字符串
        String str = format.format(new Date());
        String path = str + API.temp_filename;
        File tempFile = new File(Environment.getExternalStorageDirectory(), path);
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
//        intent.putExtra("outputX", 640);
//        intent.putExtra("outputY", 440);
        intent.putExtra("aspectX", 16);
        intent.putExtra("aspectY", 11);
        intent.putExtra("return-data", true);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        activity.startActivityForResult(intent, PicturePicker.PHOTO_REQUEST_CUT);
    }
    /**
     * 手持身份证
     *
     * @param activity
     * @param uri
     * @return
     */
    public static File cropHandCard3(Activity activity, Uri uri) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");//获取当前时间，进一步转化为字符串
        String str = format.format(new Date());
        String path = str + API.temp_filename;
        File tempFile = new File(API.FILE_DIR, path);
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 16) ;   //X方向上的比例
        intent.putExtra("aspectY", 11) ;   //Y方向上的比例
        intent.putExtra("outputX", 640);  //裁剪区的宽
        intent.putExtra("outputY", 440); //裁剪区的高
        intent.putExtra("scale ", true) ; //是否保留比例
        intent.putExtra("return-data", false); //是否在Intent中返回图片
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString()); //设置输出图片的格式

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        activity.startActivityForResult(intent, PHOTO_REQUEST_CUT);
        return tempFile;
    }

    public static String cropIDCard(Activity activity, Uri uri) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");//获取当前时间，进一步转化为字符串
        String str = format.format(new Date());
        String path = str + API.temp_filename;
        File tempFile = new File(API.FILE_DIR, path);
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 0.1);
        intent.putExtra("aspectY", 0.1);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        activity.startActivityForResult(intent, PHOTO_REQUEST_CUT);
        return tempFile.getAbsolutePath();
    }


    public static String cropAddDataPic(Activity activity, Uri uri) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");//获取当前时间，进一步转化为字符串
        String str = format.format(new Date());
        String path = str + API.temp_filename;
        File tempFile = new File(API.FILE_DIR, path);
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 0.1);
        intent.putExtra("aspectY", 0.1);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        activity.startActivityForResult(intent, PHOTO_REQUEST_CUT);
        return tempFile.getAbsolutePath();
    }

    private static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 30, baos);
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        return bitmap;
    }

    /**
     * 裁剪图片宽高
     */
    public static String corpImageWH(Activity activity, String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//这个参数设置为true才有效，
        Bitmap bmp = BitmapFactory.decodeFile(path, options);//这里的bitmap是个空
        if (bmp == null) {
            Log.e("通过options获取到的bitmap为空", "===");
        }
        mWidth = 1500;
        mHeight = 1500;
        Log.i(TAG, "getBitmapWH: mWidth----" + mWidth);
        Log.i(TAG, "getBitmapWH: mHeight----" + mHeight);
        return path;
    }

    public static boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }
}  