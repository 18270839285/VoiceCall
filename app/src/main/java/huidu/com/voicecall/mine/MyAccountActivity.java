package huidu.com.voicecall.mine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import huidu.com.voicecall.R;
import huidu.com.voicecall.base.BaseActivity;
import huidu.com.voicecall.base.WebActivity;
import huidu.com.voicecall.bean.PackageRecharge;
import huidu.com.voicecall.bean.UserInfo;
import huidu.com.voicecall.http.API;
import huidu.com.voicecall.http.BaseModel;
import huidu.com.voicecall.http.OkHttpUtils;
import huidu.com.voicecall.http.RequestFinish;
import huidu.com.voicecall.utils.DialogUtil;
import huidu.com.voicecall.utils.SPUtils;
import huidu.com.voicecall.utils.ToastUtil;

/**
 * 我的账户
 */
public class MyAccountActivity extends BaseActivity implements RequestFinish {
    @BindView(R.id.tv_userName)
    TextView tv_userName;
    @BindView(R.id.tv_money)
    TextView tv_money;
    @BindView(R.id.tv_pay_ali)
    TextView tv_pay_ali;
    @BindView(R.id.tv_pay_weixin)
    TextView tv_pay_weixin;
    @BindView(R.id.iv_head)
    CircleImageView iv_head;
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    BaseQuickAdapter mAdapter;
    List<PackageRecharge.PackageListBean> mList = new ArrayList<>();

    int payType = 0;
    String custom_tel;//客服电话

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_account;
    }

    @Override
    protected void initView() {
        OkHttpUtils.getInstance().user_info(SPUtils.getValue("token"),SPUtils.getValue("user_id"),this);
    }

    /**
     * 充值套餐选择的位置
     */
    int COIN_POSITION = 0;

    @Override
    protected void initData() {
        OkHttpUtils.getInstance().package_recharge(SPUtils.getValue("token"), this);
        GridLayoutManager layoutManager = new GridLayoutManager(this,3);
        recycleView.setLayoutManager(layoutManager);
        recycleView.setHasFixedSize(true);
        mAdapter = new BaseQuickAdapter<PackageRecharge.PackageListBean, BaseViewHolder>(R.layout.item_package_recharge, mList) {
            @Override
            protected void convert(final BaseViewHolder helper, final PackageRecharge.PackageListBean item) {
//                ¥
                final ImageView check = helper.getView(R.id.check);
                final TextView tv_coin_num = helper.getView(R.id.tv_coin_num);
                final TextView tv_money = helper.getView(R.id.tv_money);
                tv_coin_num.setText(item.getCoin() + "Y豆");
                tv_money.setText("¥ " + item.getSale_price());
                if (COIN_POSITION == helper.getAdapterPosition()) {
                    check.setSelected(true);
                    tv_coin_num.setTextColor(getResources().getColor(R.color.textSelectColor));
                    tv_money.setTextColor(getResources().getColor(R.color.textSelectColor));
                } else {
                    check.setSelected(false);
                    tv_coin_num.setTextColor(getResources().getColor(R.color.textColor));
                    tv_money.setTextColor(getResources().getColor(R.color.textColor2));
                }
//                check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        if (isChecked) {
//                            COIN_POSITION = helper.getAdapterPosition();
////                            mAdapter.notifyDataSetChanged();
//                        } else {
//                            if (COIN_POSITION == helper.getAdapterPosition()) {
//                                check.setChecked(true);
//                            } else {
//                                check.setChecked(false);
//                            }
//                        }
//                    }
//                });
            }
        };
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                COIN_POSITION = position;
                Log.e(TAG, "onItemClick: COIN_POSITION = "+COIN_POSITION );
                mAdapter.notifyDataSetChanged();
            }
        });

        recycleView.setAdapter(mAdapter);
    }

    @Override
    public void onSuccess(BaseModel result, String params) {
        switch (params) {
            case API.PACKAGE_RECHARGE:
                PackageRecharge data = (PackageRecharge) result.getData();
                mList = data.getPackage_list();
                mAdapter.setNewData(mList);

                Glide.with(this).load(data.getUser_list().getHead_image()).apply(new RequestOptions().error(R.mipmap.wd_tx_nor)).into(iv_head);
                tv_userName.setText(data.getUser_list().getNickname());
                tv_money.setText(data.getUser_list().getConsume_coin()+"Y豆");
                break;
            case API.ORDER_RECHARGE:
                ToastUtil.toastShow("充值成功");
                finish();
                break;
            case API.USER_INFO:
                UserInfo userInfo = (UserInfo)result.getData();
                custom_tel = userInfo.getCustom_tel();
                break;
        }
    }

    @Override
    public void onError(String result) {
        ToastUtil.toastShow(result);
    }

    @OnClick({R.id.iv_back, R.id.tv_protocol, R.id.tv_sure, R.id.tv_right, R.id.tv_pay_weixin, R.id.tv_pay_ali})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_protocol:
                //服务协议
                startActivity(new Intent(this,WebActivity.class).putExtra("web_type",3));
                break;
            case R.id.tv_right:
                //Y豆记录
                jumpTo(VirtualCoinRecordActivity.class);
                break;
            case R.id.tv_sure:
                //确认充值
                DialogUtil.showServiceDialog(this,custom_tel , new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        Uri data = Uri.parse("tel:" + custom_tel);
                        intent.setData(data);
                        startActivity(intent);
                    }
                }).show();
//                OkHttpUtils.getInstance().order_recharge(SPUtils.getValue("token"),"1",payType+"",mList.get(COIN_POSITION).getIs_sale(),this);//mList.get(COIN_POSITION).getPackage_id()
                break;
            case R.id.tv_pay_ali:
                //微信
                payType = 2;
                tv_pay_weixin.setSelected(false);
                tv_pay_ali.setSelected(true);
                tv_pay_weixin.setTextColor(getResources().getColor(R.color.textColor666));
                tv_pay_ali.setTextColor(getResources().getColor(R.color.textSelectColor));
                break;
            case R.id.tv_pay_weixin:
                //支付宝
                payType = 1;
                tv_pay_weixin.setSelected(true);
                tv_pay_ali.setSelected(false);
                tv_pay_weixin.setTextColor(getResources().getColor(R.color.textSelectColor));
                tv_pay_ali.setTextColor(getResources().getColor(R.color.textColor666));
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }
}
