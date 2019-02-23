package huidu.com.voicecall.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import huidu.com.voicecall.R;
import huidu.com.voicecall.base.BaseFragment;
import huidu.com.voicecall.bean.Home;
import huidu.com.voicecall.bean.OrderList;
import huidu.com.voicecall.http.API;
import huidu.com.voicecall.http.BaseModel;
import huidu.com.voicecall.http.OkHttpUtils;
import huidu.com.voicecall.http.RequestFinish;
import huidu.com.voicecall.main.OrderDetailActivity;
import huidu.com.voicecall.utils.CustomLLManager;
import huidu.com.voicecall.utils.EmptyViewUtil;
import huidu.com.voicecall.utils.GlideImageLoader;
import huidu.com.voicecall.utils.SPUtils;
import huidu.com.voicecall.utils.ToastUtil;

/**
 * Description:
 * Data：2019/1/3-16:12
 * Author: lin
 */
public class MyOrderFragment extends BaseFragment implements RequestFinish{

    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    Unbinder unbinder;
    BaseQuickAdapter mAdapter;
    List<OrderList.ListBean> mList = new ArrayList<>();

    String type_id = "";
    int mPage = 1;
    CustomLLManager llManager;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_order;
    }

    @Override
    protected void initView(View view) {
        type_id = getArguments().getString("is_receive")+"";
        llManager = new CustomLLManager(getActivity());
        OkHttpUtils.getInstance().order_list(SPUtils.getValue("token"),type_id,mPage,this);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                llManager.setScrollEnabled(false);
                mPage = 1;
                mList.clear();
                OkHttpUtils.getInstance().order_list(SPUtils.getValue("token"), type_id,mPage, new RequestFinish() {
                    @Override
                    public void onSuccess(BaseModel result, String params) {
                        llManager.setScrollEnabled(true);
                        mPage ++;
                        refreshLayout.setRefreshing(false);
                        OrderList orderList = (OrderList)result.getData();
                        mList = orderList.getList();
                        mAdapter.setNewData(mList);
                    }

                    @Override
                    public void onError(String result) {
                        llManager.setScrollEnabled(true);
                        refreshLayout.setRefreshing(false);
                        ToastUtil.toastShow(result);
                    }
                });
            }
        });

    }

    @Override
    public void onSuccess(BaseModel result, String params) {
        refreshLayout.setRefreshing(false);
        OrderList orderList = (OrderList)result.getData();
        mPage++;
        if (mPage==1){
            mList = orderList.getList();
            mAdapter.setNewData(mList);
        }else {
            mAdapter.addData(orderList.getList());
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onError(String result) {
        refreshLayout.setRefreshing(false);
        ToastUtil.toastShow(result);
    }

    /**
     * fragment静态传值
     */
    public static MyOrderFragment newInstance(String is_receive) {
        MyOrderFragment fragment = new MyOrderFragment();
        Bundle bundle = new Bundle();
        bundle.putString("is_receive", is_receive);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initData() {
        recycleView.setLayoutManager(llManager);
        recycleView.setHasFixedSize(true);
        mAdapter = new BaseQuickAdapter<OrderList.ListBean,BaseViewHolder>(R.layout.item_order_list,mList) {
            @Override
            protected void convert(BaseViewHolder helper, OrderList.ListBean item) {
                ImageView iv_sex = helper.getView(R.id.iv_sex);
                CircleImageView iv_head = helper.getView(R.id.iv_head);
                helper.setText(R.id.tv_name,item.getNickname());
                helper.setText(R.id.tv_msg_type,item.getAnchor_type_name());
                helper.setText(R.id.tv_age,item.getAge());
                LinearLayout ll_sex_age = helper.getView(R.id.ll_sex_age);

                String status = item.getStatus();
                switch (item.getStatus()){
                    case "1":
                        status = "待确定";
                        break;
                    case "2":
                        status = "待服务";
                        break;
                    case "3":
                        status = "进行中";
                        break;
                    case "5":
                        status = "已完成";
                        break;
                    case "6":
                        status = "已取消";
                        break;
                    case "7":
                        status = "已拒绝";
                        break;
                    case "8":
                        status = "已关闭";
                        break;
                }

                helper.setText(R.id.tv_state,status);
                String coin_time = "";
                if (item.getAnchor_bus_type().equals("1"))
                    coin_time = item.getTotal()+"Y豆/"+item.getNum()+"分钟x"+item.getNum();
                else
                    coin_time = item.getTotal()+"Y豆/"+item.getNum()+"次x"+item.getNum();
                helper.setText(R.id.tv_coin_time,coin_time);
                if (item.getSex().equals("1")){
                    ll_sex_age.setBackgroundResource(R.drawable.shape_corner5_boy);
                    iv_sex.setImageResource(R.mipmap.boy);
                }else if (item.getSex().equals("2")){
                    ll_sex_age.setBackgroundResource(R.drawable.shape_corner5_red);
                    iv_sex.setImageResource(R.mipmap.girl);
                }

                Glide.with(getActivity()).load(item.getHead_image()).apply(new RequestOptions().error(R.mipmap.wd_tx_nor)).into(iv_head);
            }
        };

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                OkHttpUtils.getInstance().order_list(SPUtils.getValue("token"), type_id, mPage, new RequestFinish() {
                    @Override
                    public void onSuccess(BaseModel result, String params) {
                        mAdapter.loadMoreComplete();
                        OrderList orderList = (OrderList)result.getData();
                        if (orderList.getList().isEmpty()){
                            mAdapter.loadMoreEnd();
                            ToastUtil.toastShow("暂无更多数据");
                        }else {
                            mPage++;
                            mAdapter.addData(orderList.getList());
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                    @Override
                    public void onError(String result) {
                        mAdapter.loadMoreComplete();
                        ToastUtil.toastShow(result);
                    }
                });
            }
        },recycleView);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                intent.putExtra("order_no",mList.get(position).getOrder_no());
                intent.putExtra("order_type",type_id);
                startActivity(intent);
            }
        });
        mAdapter.setEmptyView(EmptyViewUtil.getEmptyView(getActivity(),2));
        recycleView.setAdapter(mAdapter);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }
}
