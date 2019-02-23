package huidu.com.voicecall.dynamic;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import huidu.com.voicecall.R;
import huidu.com.voicecall.base.BaseActivity;
import huidu.com.voicecall.http.BaseModel;
import huidu.com.voicecall.http.OkHttpUtils;
import huidu.com.voicecall.http.RequestFinish;
import huidu.com.voicecall.main.DynamicFragment;
import huidu.com.voicecall.utils.DialogUtil;
import huidu.com.voicecall.utils.FileUtils;
import huidu.com.voicecall.utils.SPUtils;
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

    BaseQuickAdapter mAdapter;
    List<String> imageList;

    private TakePhoto takePhoto;
    private InvokeParam invokeParam;

    int photoType = 1;//1为照相，2为相册

    @Override
    protected int getLayoutId() {
        return R.layout.activity_publish;
    }

    @Override
    protected void initView() {
        tv_title.setText("发动态");
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText("发表");
        imageList = new ArrayList<>();
        imageList.add("add");

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
                boolean isAdd = item.equals("add");
                if (isAdd) {
                    iv_photo.setImageResource(R.mipmap.fdt_tj);
                    iv_delete.setVisibility(View.GONE);
                    iv_photo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //选择图片
                            if (imageList.size()>=10){
                                ToastUtil.toastShow("最多选择9张");
                            }else {
                                takePhoto();
                            }
                        }
                    });
                } else {
                    Glide.with(PublishActivity.this).load(item).into(iv_photo);
                    iv_delete.setVisibility(View.VISIBLE);
                    iv_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //删除此图
                            imageList.remove(helper.getAdapterPosition());
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                    iv_photo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                }
            }
        };
        recycleView.setAdapter(mAdapter);
    }

    @Override
    public void onSuccess(BaseModel result, String params) {
        finishLoad();
        DynamicFragment.isRefresh = true;
        ToastUtil.toastShow("发布成功");
        finish();
    }

    @Override
    public void onError(String result) {
        finishLoad();
        ToastUtil.toastShow(result);
    }

    @OnClick({R.id.iv_back, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:
                //发表
                if (TextUtils.isEmpty(et_content.getText().toString())){
                    ToastUtil.toastShow("内容不能为空");
                    return;
                }
                if (imageList.size()>=2){
                    mLoading.show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            StringBuilder base64 = new StringBuilder();
                            for (int i = 0;i<imageList.size()-1;i++){
                                File f = new File(imageList.get(i));
                                base64.append(FileUtils.fileToBase64(f)+",");
                            }
                            base64.delete(base64.length()-1,base64.length());
                            OkHttpUtils.getInstance().dynamic_publish(SPUtils.getValue("token"),base64.toString(),et_content.getText().toString(),PublishActivity.this);
                        }
                    }).start();
                }else {
                    ToastUtil.toastShow("至少上传一张照片");
                }
                break;
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
                takePhoto.onPickMultiple(10 - imageList.size());
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
        if (photoType == 1){
            imageList.add(imageList.size() - 1, result.getImage().getCompressPath());
        }else {
            for (int i = 0; i < result.getImages().size(); i++) {
                imageList.add(imageList.size() - 1, result.getImages().get(i).getCompressPath());
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
}