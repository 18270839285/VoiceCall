package huidu.com.voicecall.mine;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
import huidu.com.voicecall.bean.CoinLog;
import huidu.com.voicecall.bean.UserAttention;
import huidu.com.voicecall.http.API;
import huidu.com.voicecall.http.BaseModel;
import huidu.com.voicecall.http.OkHttpUtils;
import huidu.com.voicecall.http.RequestFinish;
import huidu.com.voicecall.utils.DateUtil;
import huidu.com.voicecall.utils.EmptyViewUtil;
import huidu.com.voicecall.utils.SPUtils;
import huidu.com.voicecall.utils.ToastUtil;

/**
 * Y豆记录
 */
public class VirtualCoinRecordActivity extends BaseActivity implements RequestFinish {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    BaseQuickAdapter mAdapter;
    List<CoinLog.ListBean> mList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_virtual_coin_record;
    }

    @Override
    protected void initView() {
        tv_title.setText("Y豆记录");
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                OkHttpUtils.getInstance().order_coin1_log(SPUtils.getValue("token"), VirtualCoinRecordActivity.this);
            }
        });
    }

    @Override
    protected void initData() {
        OkHttpUtils.getInstance().order_coin1_log(SPUtils.getValue("token"), this);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.setHasFixedSize(true);
        mAdapter = new BaseQuickAdapter<CoinLog.ListBean, BaseViewHolder>(R.layout.item_virtual_record, mList) {
            @Override
            protected void convert(BaseViewHolder helper, CoinLog.ListBean item) {
                helper.setText(R.id.tv_status,item.getMemo());
                if (item.getIs_add().equals("1")){
                    helper.setText(R.id.tv_money,"+"+item.getCoin()+" Y豆");
                }else {
                    helper.setText(R.id.tv_money,"-"+item.getCoin()+" Y豆");
                }
                helper.setText(R.id.tv_time1, DateUtil.getTime1(item.getCreate_time()));
                helper.setText(R.id.tv_time2, DateUtil.getTime(item.getCreate_time()));
            }
        };
        mAdapter.setEmptyView(EmptyViewUtil.getEmptyView(this,1));
        recycleView.setAdapter(mAdapter);
    }

    @Override
    public void onSuccess(BaseModel result, String params) {
        refreshLayout.setRefreshing(false);
        switch (params) {
            case API.ORDER_COIN1_LOG:
                CoinLog coinLog = (CoinLog) result.getData();
                mList = coinLog.getList();
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
