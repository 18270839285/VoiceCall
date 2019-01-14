package huidu.com.voicecall.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import huidu.com.voicecall.R;
import huidu.com.voicecall.bean.UserInfo;
import huidu.com.voicecall.http.API;
import huidu.com.voicecall.http.BaseModel;
import huidu.com.voicecall.http.OkHttpUtils;
import huidu.com.voicecall.http.RequestFinish;
import huidu.com.voicecall.login.LoginActivity;
import huidu.com.voicecall.mine.MyAccountActivity;
import huidu.com.voicecall.mine.MyAttentionActivity;
import huidu.com.voicecall.mine.MyFansActivity;
import huidu.com.voicecall.mine.MyOrderActivity;
import huidu.com.voicecall.mine.MyWealthActivity;
import huidu.com.voicecall.mine.PersonalActivity;
import huidu.com.voicecall.test.TestActivity;
import huidu.com.voicecall.base.BaseFragment;
import huidu.com.voicecall.utils.ToastUtil;

/**
 * Description:
 * Data：2019/1/3-11:29
 * Author: lin
 */
public class MineFragment extends BaseFragment implements RequestFinish{

    @BindView(R.id.iv_head)
    CircleImageView iv_head;
    @BindView(R.id.userName)
    TextView userName;
    @BindView(R.id.userId)
    TextView userId;
    @BindView(R.id.tv_follow_num)
    TextView tv_follow_num;
    @BindView(R.id.tv_fans_num)
    TextView tv_fans_num;
    @BindView(R.id.tv_order_num)
    TextView tv_order_num;

    Unbinder unbinder;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void initData() {
        OkHttpUtils.getInstance().user_info(API.TOKEN_TEST,"1",this);
    }

    @OnClick({R.id.iv_setting, R.id.rl_personal, R.id.ll_recharge, R.id.ll_income,R.id.ll_anchor,
            R.id.ll_idCard, R.id.ll_about_us,R.id.ll_customer,R.id.ll_follow,R.id.ll_fans,R.id.ll_order})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_setting:
                //设置
                jump(LoginActivity.class);
                break;
            case R.id.rl_personal:
                //个人资料
                jump(PersonalActivity.class);
                break;
            case R.id.ll_fans:
                //粉丝列表 MyFansActivity
                jump(MyFansActivity.class);
                break;
            case R.id.ll_follow:
                //关注列表
                jump(MyAttentionActivity.class);
                break;
            case R.id.ll_order:
                //订单
                jump(MyOrderActivity.class);
                break;
            case R.id.ll_recharge:
                //充值
                jump(MyAccountActivity.class);
                break;
            case R.id.ll_income:
                //收入
                jump(MyWealthActivity.class);
                break;
            case R.id.ll_anchor:
                //主播认证
                break;
            case R.id.ll_idCard:
                //身份认证
                break;
            case R.id.ll_about_us:
                //关于我们
                break;
            case R.id.ll_customer:
                //联系客服
                startActivity(new Intent(getActivity(), TestActivity.class));
                break;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        OkHttpUtils.getInstance().user_info(API.TOKEN_TEST,"1",this);
    }

    @Override
    public void onSuccess(BaseModel result, String params) {
        UserInfo userInfo = (UserInfo)result.getData();
        userName.setText(userInfo.getNickname());
        userId.setText("ID:"+userInfo.getId());
        Glide.with(getActivity()).load(userInfo.getHead_image()).into(iv_head);
        tv_follow_num.setText(userInfo.getAttention_count());
        tv_fans_num.setText(userInfo.getFans_count());
        tv_order_num.setText(userInfo.getOrder_sum());
    }

    @Override
    public void onError(String result) {
        ToastUtil.toastShow(result);
    }
}
