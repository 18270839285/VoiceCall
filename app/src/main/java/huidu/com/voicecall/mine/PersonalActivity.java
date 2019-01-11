package huidu.com.voicecall.mine;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import huidu.com.voicecall.R;
import huidu.com.voicecall.base.BaseActivity;
import huidu.com.voicecall.bean.UserInfo;
import huidu.com.voicecall.http.API;
import huidu.com.voicecall.http.BaseModel;
import huidu.com.voicecall.http.OkHttpUtils;
import huidu.com.voicecall.http.RequestFinish;
import huidu.com.voicecall.utils.ToastUtil;
import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * 个人资料
 */
public class PersonalActivity extends BaseActivity implements RequestFinish{

    @BindView(R.id.iv_head)
    CircleImageView iv_head;
    @BindView(R.id.iv_head_big)
    ImageView iv_head_big;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_fans)
    TextView tv_fans;
    @BindView(R.id.tv_attention)
    TextView tv_attention;
    @BindView(R.id.tv_edit)
    TextView tv_edit;
    @BindView(R.id.tv_id)
    TextView tv_id;
    @BindView(R.id.tv_constellation)
    TextView tv_constellation;
    @BindView(R.id.tv_sign)
    TextView tv_sign;
    @BindView(R.id.tv_age)
    TextView tv_age;
    @BindView(R.id.iv_sex)
    ImageView iv_sex;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_personal;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        OkHttpUtils.getInstance().user_info(API.TOKEN_TEST,"1",this);
    }

    @OnClick({R.id.iv_back,R.id.tv_edit})
    public void onViewClick(View view){
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_edit:
                //编辑资料
                jumpTo(EditingMaterialsActivity.class);
                break;
        }
    }

    @Override
    public void onSuccess(BaseModel result, String params) {
        switch (params){
            case API.USER_INFO:
                UserInfo userInfo = (UserInfo)result.getData();
                tv_name.setText(userInfo.getNickname());
                tv_age.setText(userInfo.getAge());
                tv_id.setText(userInfo.getId());
                tv_constellation.setText(userInfo.getZodiac());
                tv_sign.setText(userInfo.getIntroduce());
                tv_attention.setText(userInfo.getAttention_count()+"关注 ");
                tv_fans.setText(" "+userInfo.getFans_count()+"粉丝");
                if(userInfo.getSex().equals("1")){
                    iv_sex.setImageResource(R.mipmap.boy);
                }else {
                    iv_sex.setImageResource(R.mipmap.girl);
                }
                Glide.with(this).load(userInfo.getHead_image()).into(iv_head);
                Glide.with(this)
                        .load(userInfo.getHead_image())
                        .crossFade()
                        // 设置高斯模糊
                        .bitmapTransform(new BlurTransformation(this, 5))
                        .into(iv_head_big);
                break;
        }
    }

    @Override
    public void onError(String result) {
        ToastUtil.toastShow(result);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }
}
