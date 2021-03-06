package huidu.com.voicecall.mine;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

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
import huidu.com.voicecall.utils.GlideBlurformation;
import huidu.com.voicecall.utils.SPUtils;
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
    @BindView(R.id.ll_sex_age)
    LinearLayout ll_sex_age;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_personal;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

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
                if (userInfo.getZodiac().isEmpty()){
                    tv_constellation.setText("待设置");
                }else {
                    tv_constellation.setText(userInfo.getZodiac());
                }
                if (userInfo.getZodiac().isEmpty()){
                    tv_sign.setText("待设置");
                }else {
                    tv_sign.setText(userInfo.getIntroduce());
                }
                tv_attention.setText(userInfo.getAttention_count()+"关注 ");
                tv_fans.setText(" "+userInfo.getFans_count()+"粉丝");
                iv_sex.setVisibility(View.VISIBLE);
                if(userInfo.getSex().equals("1")){
                    ll_sex_age.setBackgroundResource(R.drawable.shape_corner5_boy);
                    iv_sex.setImageResource(R.mipmap.boy);
                }else if (userInfo.getSex().equals("2")){
                    ll_sex_age.setBackgroundResource(R.drawable.shape_corner5_red);
                    iv_sex.setImageResource(R.mipmap.girl);
                }else {
                    ll_sex_age.setBackgroundResource(R.drawable.shape_corner5_red);
                    iv_sex.setVisibility(View.GONE);
                }
                Glide.with(this).load(userInfo.getHead_image()).into(iv_head);
                // 设置高斯模糊
                Glide.with(this)
                        .load(userInfo.getHead_image())
                        .apply(RequestOptions.bitmapTransform(new GlideBlurformation(this)))
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

    @Override
    protected void onResume() {
        super.onResume();
        OkHttpUtils.getInstance().user_info(SPUtils.getValue("token"),SPUtils.getValue("user_id"),this);
    }
}
