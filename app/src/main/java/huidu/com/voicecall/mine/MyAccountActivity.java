package huidu.com.voicecall.mine;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

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
import huidu.com.voicecall.bean.PackageRecharge;
import huidu.com.voicecall.http.API;
import huidu.com.voicecall.http.BaseModel;
import huidu.com.voicecall.http.OkHttpUtils;
import huidu.com.voicecall.http.RequestFinish;
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
    List<PackageRecharge.ListBean> mList = new ArrayList<>();

    int payType = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_account;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        OkHttpUtils.getInstance().package_recharge(API.TOKEN_TEST, this);
        recycleView.setLayoutManager(new GridLayoutManager(this, 3));
        recycleView.setHasFixedSize(true);
        mAdapter = new BaseQuickAdapter<PackageRecharge.ListBean, BaseViewHolder>(R.layout.item_package_recharge, mList) {
            @Override
            protected void convert(final BaseViewHolder helper, final PackageRecharge.ListBean item) {
//                ¥
                ImageView check = helper.getView(R.id.check);
                TextView tv_coin_num = helper.getView(R.id.tv_coin_num);
                TextView tv_money = helper.getView(R.id.tv_money);
                tv_coin_num.setText(item.getCoin() + "虚拟币");
                tv_money.setText("¥ " + item.getSale_price());
                Log.e(TAG, "convert: position = " + helper.getAdapterPosition() + "  isCheck = " + item.isCheck());
                check.setSelected(item.isCheck());
                if (item.isCheck()) {
                    tv_coin_num.setTextColor(getResources().getColor(R.color.textSelectColor));
                    tv_money.setTextColor(getResources().getColor(R.color.textSelectColor));
                } else {
                    tv_coin_num.setTextColor(getResources().getColor(R.color.textColor));
                    tv_money.setTextColor(getResources().getColor(R.color.textColor2));
                }
//                helper.getConvertView().setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        List<PackageRecharge.ListBean> mAdapterData = mAdapter.getData();
//                        for (PackageRecharge.ListBean bean : mAdapterData) {
//                            bean.setCheck(false);
//                        }
//                        item.setCheck(true);
//                        mAdapter.notifyDataSetChanged();
//                    }
//                });
            }
        };

        recycleView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.e(TAG, "onClick: setOnItemClickListener position = "+position);
//                List<PackageRecharge.ListBean> mAdapterData = mAdapter.getData();
                for (PackageRecharge.ListBean bean : mList) {
                    bean.setCheck(false);
                }
//                for (int i = 0; i < mAdapterData.size(); i++) {
//                    mAdapterData.get(i).setCheck(false);
//                }
                mList.get(position).setCheck(true);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onSuccess(BaseModel result, String params) {
        switch (params) {
            case API.PACKAGE_RECHARGE:
                PackageRecharge data = (PackageRecharge) result.getData();
                mList = data.getList();
                mList.addAll(mList);
                mList.addAll(mList);
                mAdapter.setNewData(mList);
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
            case R.id.tv_protocol:
                //服务协议
            case R.id.tv_right:
                //虚拟币记录
                jumpTo(VirtualCoinRecordActivity.class);
            case R.id.tv_sure:
                //确认充值
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
