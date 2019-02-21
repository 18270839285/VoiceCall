package huidu.com.voicecall.dynamic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import huidu.com.voicecall.R;
import huidu.com.voicecall.base.BaseFragment;
import huidu.com.voicecall.bean.DynamicData;
import huidu.com.voicecall.http.BaseModel;
import huidu.com.voicecall.http.OkHttpUtils;
import huidu.com.voicecall.http.RequestFinish;
import huidu.com.voicecall.utils.EmptyViewUtil;
import huidu.com.voicecall.utils.Loading;
import huidu.com.voicecall.utils.SPUtils;
import huidu.com.voicecall.utils.ToastUtil;

/**
 * Description:
 * Data：2019/2/21-16:52
 * Author: lin
 */
public class MyZanFragment extends BaseFragment implements RequestFinish {
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    Unbinder unbinder;
    BaseQuickAdapter mAdapter;
    List<DynamicData.DynamicList> mList = new ArrayList<>();

    int mPage = 1;

    private Loading mLoad;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_my_dynamic;
    }

    @Override
    protected void initView(View view) {
        mLoad = new Loading(getActivity());
        OkHttpUtils.getInstance().dynamic_my_like(SPUtils.getValue("token"), this);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                mList.clear();
                OkHttpUtils.getInstance().dynamic_my_like(SPUtils.getValue("token"), new RequestFinish() {
                    @Override
                    public void onSuccess(BaseModel result, String params) {
                        refreshLayout.setRefreshing(false);
                        mPage++;
                        DynamicData orderList = (DynamicData) result.getData();
                        mList = orderList.getList();
                        mAdapter.setNewData(mList);
                    }

                    @Override
                    public void onError(String result) {
                        refreshLayout.setRefreshing(false);
                        ToastUtil.toastShow(result);
                    }
                });
            }
        });
    }

    @Override
    protected void initData() {
        recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycleView.setHasFixedSize(true);
        mAdapter = new BaseQuickAdapter<DynamicData.DynamicList, BaseViewHolder>(R.layout.item_dynamic, mList) {
            @Override
            protected void convert(BaseViewHolder helper,final DynamicData.DynamicList item) {
                CircleImageView iv_head = helper.getView(R.id.iv_head);
                final ImageView iv_zan = helper.getView(R.id.iv_zan);
                final TextView tv_num = helper.getView(R.id.tv_num);
                ImageView iv_sex = helper.getView(R.id.iv_sex);
                helper.setText(R.id.tv_nickName, item.getNickname());
                helper.setText(R.id.tv_time, item.getCreated_at());
                helper.setText(R.id.tv_content, item.getContent());
                tv_num.setText(item.getLike_count()+"");
                if (item.getIs_my().equals("1")){
                    iv_zan.setImageResource(R.mipmap.zan_pre);
                }else {
                    iv_zan.setImageResource(R.mipmap.zan);
                }
                if (item.getSex().equals("1")){
                    iv_sex.setImageResource(R.mipmap.dt_boy);
                }else {
                    iv_sex.setImageResource(R.mipmap.girl1);
                }
                Glide.with(getActivity()).load(item.getHead_image()).into(iv_head);

                iv_zan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mLoad.show();
                        OkHttpUtils.getInstance().dynamic_like(SPUtils.getValue("token"), item.getDynamic_id(), new RequestFinish() {
                            @Override
                            public void onSuccess(BaseModel result, String params) {
                                mLoad.dismiss();
                                if (item.getIs_my().equals("1")){
                                    ToastUtil.toastShow("取消点赞成功");
                                    item.setIs_my("2");
                                    iv_zan.setImageResource(R.mipmap.zan);
                                    item.setLike_count((Integer.parseInt(item.getLike_count())-1)+"");
                                }else {
                                    ToastUtil.toastShow("点赞成功");
                                    item.setIs_my("1");
                                    iv_zan.setImageResource(R.mipmap.zan_pre);
                                    item.setLike_count((Integer.parseInt(item.getLike_count())+1)+"");
                                }
                                tv_num.setText(item.getLike_count());
                            }

                            @Override
                            public void onError(String result) {
                                mLoad.dismiss();
                            }
                        });
                    }
                });

                RecyclerView item_recycleView = helper.getView(R.id.item_recycleView);
                item_recycleView.setLayoutManager(new GridLayoutManager(getActivity(),3));
                item_recycleView.setHasFixedSize(true);
                List<String> imageList = item.getImage();
                BaseQuickAdapter adapter = new BaseQuickAdapter<String ,BaseViewHolder>(R.layout.item_dynamic_photo,imageList) {
                    @Override
                    protected void convert(BaseViewHolder helper, String item) {
                        ImageView imageView = helper.getView(R.id.iv_photo);
                        Glide.with(getActivity()).load(item).into(imageView);
                    }
                };
                item_recycleView.setAdapter(adapter);
            }
        };
        mAdapter.setEmptyView(EmptyViewUtil.getEmptyView(getActivity(), 1));
        recycleView.setAdapter(mAdapter);
    }

    @Override
    public void onSuccess(BaseModel result, String params) {
        if (refreshLayout != null && refreshLayout.isRefreshing())
            refreshLayout.setRefreshing(false);
        DynamicData orderList = (DynamicData) result.getData();
        mList = orderList.getList();
        mAdapter.setNewData(mList);
    }

    @Override
    public void onError(String result) {
        if (refreshLayout != null && refreshLayout.isRefreshing())
            refreshLayout.setRefreshing(false);
        ToastUtil.toastShow(result);
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
}
