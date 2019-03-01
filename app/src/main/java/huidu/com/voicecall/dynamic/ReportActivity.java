package huidu.com.voicecall.dynamic;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import huidu.com.voicecall.R;
import huidu.com.voicecall.base.BaseActivity;
import huidu.com.voicecall.bean.ViolationType;
import huidu.com.voicecall.http.API;
import huidu.com.voicecall.http.BaseModel;
import huidu.com.voicecall.http.OkHttpUtils;
import huidu.com.voicecall.http.RequestFinish;
import huidu.com.voicecall.utils.DialogUtil;
import huidu.com.voicecall.utils.FileUtils;
import huidu.com.voicecall.utils.SPUtils;
import huidu.com.voicecall.utils.ToastUtil;

/**
 * 举报
 */
public class ReportActivity extends BaseActivity implements RequestFinish, TakePhoto.TakeResultListener, InvokeListener {

    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.iv_icon)
    ImageView iv_icon;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_text_num)
    TextView tv_text_num;
    @BindView(R.id.et_reason)
    EditText et_reason;
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    @BindView(R.id.recycleView_photo)
    RecyclerView recycleView_photo;

    private boolean isShowType = false;

    BaseQuickAdapter typeAdapter;
    List<ViolationType> typeList;
    BaseQuickAdapter photoAdapter;
    List<String> imageList;

    int type_position = -1;

    String img = "";

    private TakePhoto takePhoto;
    private InvokeParam invokeParam;

    int photoType = 1;//1为照相，2为相册
    private final int IMG_MAX_NUM = 4;

    String userId = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_report;
    }

    @Override
    protected void initView() {
        tv_title.setText("举报");

        userId = getIntent().getStringExtra("userId");
        typeList = new ArrayList<>();
        imageList = new ArrayList<>();
        imageList.add("0000");
        et_reason.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && et_reason.getText() != null) {
                    tv_text_num.setText(et_reason.getText().toString().length() + "");
                } else {
                    tv_text_num.setText("0");
                }
            }
        });

        OkHttpUtils.getInstance().violation_type(SPUtils.getValue("token"), this);
    }

    @Override
    protected void initData() {
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.setHasFixedSize(true);
        typeAdapter = new BaseQuickAdapter<ViolationType, BaseViewHolder>(R.layout.item_violation_type, typeList) {
            @Override
            protected void convert(final BaseViewHolder helper, final ViolationType violationType) {
                final ImageView iv_check = helper.getView(R.id.iv_check);
                TextView tv_title = helper.getView(R.id.tv_title);
                RelativeLayout rl_type = helper.getView(R.id.rl_type);
                tv_title.setText(violationType.getName());
                Log.e(TAG, "convert: type_position = "+type_position );
                if (type_position == helper.getAdapterPosition()){
                    iv_check.setVisibility(View.VISIBLE);
                }else {
                    iv_check.setVisibility(View.GONE);
                }
                rl_type.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e(TAG, "onClick: type_position = "+type_position );
                        type_position = helper.getAdapterPosition();
                        typeAdapter.notifyDataSetChanged();
                    }
                });
            }
        };
        recycleView.setAdapter(typeAdapter);

        recycleView_photo.setLayoutManager(new GridLayoutManager(this, 3));
        recycleView_photo.setHasFixedSize(true);
        photoAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_dynamic_photo2, imageList) {
            @Override
            protected void convert(final BaseViewHolder helper, final String item) {
                ImageView iv_photo = helper.getView(R.id.iv_photo);
                ImageView iv_delete = helper.getView(R.id.iv_delete);
                if (item.equals("0000")){
                    iv_photo.setImageResource(R.mipmap.fdt_tj);
                    iv_delete.setVisibility(View.GONE);
                }else {
                    Glide.with(ReportActivity.this).load(item).into(iv_photo);
                    iv_delete.setVisibility(View.VISIBLE);
                }

                iv_photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.equals("0000")){
                            if (imageList.size() >= IMG_MAX_NUM) {
                                ToastUtil.toastShow("最多选择3张");
                            } else {
                                takePhoto();
                            }
                        }else {
                            Log.e(TAG, "onClick: 不能点击" );
                        }
                    }
                });

//                if (helper.getAdapterPosition()==3){
//                    imageList.remove(3);
//                    notifyDataSetChanged();
//                }
                iv_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //删除此图
                        imageList.remove(helper.getAdapterPosition());
                        if (!imageList.contains("0000")){
                            Log.e(TAG, "onClick: 0000");
                            imageList.add("0000");
                        }
                        notifyDataSetChanged();
                    }
                });
            }
        };
        recycleView_photo.setAdapter(photoAdapter);
    }


    private void takePhoto() {
        DialogUtil.showTakePhoto(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(getExternalCacheDir(), System.currentTimeMillis() + ".png");
                final Uri imageUri = Uri.fromFile(file);
                photoType = 1;
                takePhoto.onPickFromCapture(imageUri);
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoType = 2;
                takePhoto.onPickMultiple(IMG_MAX_NUM - imageList.size());
            }
        }).show();
    }

    @Override
    public void onSuccess(BaseModel result, String params) {
        switch (params) {
            case API.VIOLATION_TYPE:
                typeList = (List<ViolationType>)result.getData();
                typeAdapter.setNewData(typeList);
                break;
            case API.INFORM_SEND:
                finishLoad();
                ToastUtil.toastShow("举报成功");
                finish();
                break;
        }
    }

    @Override
    public void onError(String result) {
        finishLoad();
        ToastUtil.toastShow(result);
    }

    @OnClick({R.id.iv_back,R.id.tv_commit,R.id.ll_report_type})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_report_type:
                if (isShowType){
                    iv_icon.setImageResource(R.mipmap.jb_zk);
                    recycleView.setVisibility(View.GONE);
                }else {
                    iv_icon.setImageResource(R.mipmap.jb_sq);
                    recycleView.setVisibility(View.VISIBLE);
                }
                isShowType = !isShowType;
                break;
            case R.id.tv_commit:
                checkCommit();
                break;
        }
    }

    private void checkCommit(){
        if (type_position<0){
            ToastUtil.toastShow("请选择举报类型");
            return;
        }
        if (et_reason.getText().toString().isEmpty()&&imageList.size()==1) {
            ToastUtil.toastShow("请输入补充原因或者添加图片");
            return;
        }
        mLoading.show();
        if (imageList.size() > 1) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    StringBuilder base64 = new StringBuilder();
                    if (imageList.size()==3){
                        for (int i = 0; i < imageList.size(); i++) {
                            File f = new File(imageList.get(i));
                            base64.append(FileUtils.fileToBase64(f) + ",");
                        }
                    }else {
                        for (int i = 0; i < imageList.size() - 1; i++) {
                            File f = new File(imageList.get(i));
                            base64.append(FileUtils.fileToBase64(f) + ",");
                        }
                    }
                    base64.delete(base64.length() - 1, base64.length());
                    OkHttpUtils.getInstance().inform_send(SPUtils.getValue("token"),base64.toString(),userId+"",typeList.get(type_position).getViolation_id()+"",et_reason.getText().toString(),ReportActivity.this);
                }
            }).start();
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
        takePhoto.onEnableCompress(new CompressConfig.Builder().setMaxSize(800 * 1024).create(), true);
        return takePhoto;
    }

    @Override
    public void takeSuccess(TResult result) {
        if (photoType == 1) {
            imageList.add(imageList.size()-1, result.getImage().getCompressPath());
        } else {
            for (int i = 0; i < result.getImages().size(); i++) {
                imageList.add(imageList.size()-1, result.getImages().get(i).getCompressPath());
            }
        }

        if (imageList.size()==4){imageList.remove("0000");}
        Log.e(TAG, "takeSuccess: "+imageList.size() );

        photoAdapter.notifyDataSetChanged();
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
