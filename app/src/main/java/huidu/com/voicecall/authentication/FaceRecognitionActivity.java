package huidu.com.voicecall.authentication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import huidu.com.voicecall.R;
import huidu.com.voicecall.base.BaseActivity;
import huidu.com.voicecall.http.BaseModel;
import huidu.com.voicecall.http.RequestFinish;

/**
 * 身份证认证
 */
public class FaceRecognitionActivity extends BaseActivity implements RequestFinish {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_face_recognition;
    }

    @Override
    protected void initView() {
        tv_title.setText("身份证认证");
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.iv_back,R.id.tv_start})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_start:
                //去人脸识别
                jumpTo(IdentifyBaiDuFaceActivity.class);
                break;
        }
    }

    @Override
    public void onSuccess(BaseModel result, String params) {

    }

    @Override
    public void onError(String result) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }
}
