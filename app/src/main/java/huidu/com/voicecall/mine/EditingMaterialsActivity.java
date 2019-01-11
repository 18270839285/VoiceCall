package huidu.com.voicecall.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import huidu.com.voicecall.R;
import huidu.com.voicecall.base.BaseActivity;

/**
 * 编辑资料
 */
public class EditingMaterialsActivity extends BaseActivity {

    @BindView(R.id.iv_head)
    ImageView iv_head;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_right)
    TextView tv_right;
    @BindView(R.id.tv_userId)
    TextView tv_userId;
    @BindView(R.id.tv_sex)
    TextView tv_sex;
    @BindView(R.id.tv_birthday)
    TextView tv_birthday;
    @BindView(R.id.tv_constellation)
    TextView tv_constellation;
    @BindView(R.id.tv_area)
    TextView tv_area;
    @BindView(R.id.et_sign)
    EditText et_sign;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_editing_materials;
    }

    @Override
    protected void initView() {
        tv_title.setText("编辑资料");
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText("保存");
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.iv_back,R.id.tv_right,R.id.ll_head,R.id.ll_sex,R.id.ll_birthday,R.id.ll_constellation,R.id.ll_area})
    public void onViewClick(View view){
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right:
                //保存
                break;
            case R.id.ll_head:
                //头像
                break;
            case R.id.ll_sex:
                //性别
                break;
            case R.id.ll_birthday:
                //生日
                break;
            case R.id.ll_constellation:
                //星座
                break;
            case R.id.ll_area:
                //地址
                break;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }
}
