package huidu.com.voicecall.message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import huidu.com.voicecall.R;
import huidu.com.voicecall.base.BaseFragment;
import huidu.com.voicecall.bean.SystemNotice;
import huidu.com.voicecall.http.API;
import huidu.com.voicecall.http.BaseModel;
import huidu.com.voicecall.http.OkHttpUtils;
import huidu.com.voicecall.http.RequestFinish;
import huidu.com.voicecall.utils.CustomLLManager;
import huidu.com.voicecall.utils.DateUtil;
import huidu.com.voicecall.utils.EmptyViewUtil;
import huidu.com.voicecall.utils.LLManager;
import huidu.com.voicecall.utils.Loading;
import huidu.com.voicecall.utils.SPUtils;
import huidu.com.voicecall.utils.ToastUtil;

/**
 * Description:系统通知
 * Data：2019/1/3-16:12
 * Author: lin
 */
public class SystemNotificationFragment extends BaseFragment implements RequestFinish {

    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    Unbinder unbinder;
    BaseQuickAdapter mAdapter;
    List<SystemNotice.Notice> mList = new ArrayList<>();

    int mPage = 1;
    CustomLLManager llManager;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_order;
    }

    @Override
    protected void initView(View view) {
        llManager = new CustomLLManager(getActivity());
        mLoading.show();
        OkHttpUtils.getInstance().notice_system(SPUtils.getValue("token"), mPage + "", this);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                llManager.setScrollEnabled(false);
                mPage = 1;
                mList.clear();
                OkHttpUtils.getInstance().notice_system(SPUtils.getValue("token"), mPage+"", new RequestFinish() {
                    @Override
                    public void onSuccess(BaseModel result, String params) {
                        refreshLayout.setRefreshing(false);
                        llManager.setScrollEnabled(true);
                        mPage++;
                        SystemNotice orderList = (SystemNotice) result.getData();
                        mList = orderList.getNotice();
                        mAdapter.setNewData(mList);
                    }

                    @Override
                    public void onError(String result) {
                        refreshLayout.setRefreshing(false);
                        llManager.setScrollEnabled(true);
                        ToastUtil.toastShow(result);
                    }
                });
            }
        });

    }

    @Override
    public void onSuccess(BaseModel result, String params) {
        refreshLayout.setRefreshing(false);
        finishLoad();
        SystemNotice orderList = (SystemNotice) result.getData();
        mList = orderList.getNotice();
        mAdapter.setNewData(mList);
    }

    @Override
    public void onError(String result) {
        refreshLayout.setRefreshing(false);
        finishLoad();
        ToastUtil.toastShow(result);
    }

    @Override
    protected void initData() {
        recycleView.setLayoutManager(llManager);
        recycleView.setHasFixedSize(true);
        mAdapter = new BaseQuickAdapter<SystemNotice.Notice, BaseViewHolder>(R.layout.item_system_notice, mList) {
            @Override
            protected void convert(BaseViewHolder helper, SystemNotice.Notice item) {
                ImageView iv_head = helper.getView(R.id.iv_head);
                helper.setText(R.id.tv_title, item.getTitle());
                helper.setText(R.id.tv_time, item.getSendTime());
//                helper.setText(R.id.tv_time, DateUtil.getTime5(item.getSendTime()));
                helper.setText(R.id.tv_content, item.getContent());
                Glide.with(getActivity()).load(item.getIcon()).into(iv_head);
            }
        };
        mAdapter.setEmptyView(EmptyViewUtil.getEmptyView(getActivity(), 6));
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
