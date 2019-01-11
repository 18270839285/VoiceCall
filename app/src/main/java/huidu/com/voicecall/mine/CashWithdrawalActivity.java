package huidu.com.voicecall.mine;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import huidu.com.voicecall.R;
import huidu.com.voicecall.base.BaseActivity;
import huidu.com.voicecall.bean.WithdrawalLog;
import huidu.com.voicecall.http.API;
import huidu.com.voicecall.http.BaseModel;
import huidu.com.voicecall.http.OkHttpUtils;
import huidu.com.voicecall.http.RequestFinish;
import huidu.com.voicecall.utils.DateUtil;
import huidu.com.voicecall.utils.ToastUtil;

/**
 * 提现
 */
public class CashWithdrawalActivity extends BaseActivity implements RequestFinish {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    BaseQuickAdapter mAdapter;
    List<WithdrawalLog.ListBean> mList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cash_withdrawal;
    }

    @Override
    protected void initView() {
        tv_title.setText("提现");
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                OkHttpUtils.getInstance().order_withdrawal_log(API.TOKEN_TEST, CashWithdrawalActivity.this);
            }
        });
    }

    @Override
    protected void initData() {
        OkHttpUtils.getInstance().order_withdrawal_log(API.TOKEN_TEST, this);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.setHasFixedSize(true);
        mAdapter = new BaseQuickAdapter<WithdrawalLog.ListBean, BaseViewHolder>(R.layout.item_withdrawal, mList) {
            @Override
            protected void convert(BaseViewHolder helper, WithdrawalLog.ListBean item) {
//                ¥
                TextView tv_status = helper.getView(R.id.tv_status);
                helper.setText(R.id.tv_money, "¥ " + item.getMoney() + "元");
                helper.setText(R.id.tv_time1, DateUtil.getTime1(item.getCreate_time()));
                helper.setText(R.id.tv_time2, DateUtil.getTime(item.getCreate_time()));
                switch (item.getStatus()) {
                    case "1":
                        tv_status.setText("提现中");
                        break;
                    case "2":
                        tv_status.setText("提现失败");
                        break;
                    case "3":
                        tv_status.setText("待转账");
                        break;
                    case "4":
                        tv_status.setText("提现成功");
                        break;
                }

            }
        };
        recycleView.setAdapter(mAdapter);
    }

    @Override
    public void onSuccess(BaseModel result, String params) {
        refreshLayout.setRefreshing(false);
        switch (params) {
            case API.ORDER_WITHDRAWAL_LOG:
                WithdrawalLog data = (WithdrawalLog) result.getData();
                mList = data.getList();
                mAdapter.setNewData(mList);
                break;
        }
    }

    @Override
    public void onError(String result) {
        refreshLayout.setRefreshing(false);
        ToastUtil.toastShow(result);
    }

    @OnClick({R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }
}
