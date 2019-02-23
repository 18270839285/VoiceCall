package huidu.com.voicecall.main;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import huidu.com.voicecall.R;
import huidu.com.voicecall.base.BaseFragment;
import huidu.com.voicecall.bean.DynamicData;
import huidu.com.voicecall.dynamic.PhotoViewActivity;
import huidu.com.voicecall.dynamic.PictureActivity;
import huidu.com.voicecall.dynamic.PublishActivity;
import huidu.com.voicecall.http.BaseModel;
import huidu.com.voicecall.http.OkHttpUtils;
import huidu.com.voicecall.http.RequestFinish;
import huidu.com.voicecall.utils.CustomLLManager;
import huidu.com.voicecall.utils.EmptyViewUtil;
import huidu.com.voicecall.utils.Loading;
import huidu.com.voicecall.utils.SPUtils;
import huidu.com.voicecall.utils.ToastUtil;

/**
 * Description:
 * Data：2019/2/21-9:37
 * Author: lin
 */
public class DynamicFragment extends BaseFragment implements RequestFinish {

    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    Unbinder unbinder;
    BaseQuickAdapter mAdapter;
    List<DynamicData.DynamicList> mList;

    int mPage = 1;

    private Loading mLoad;

    public static boolean isRefresh = false;

    CustomLLManager llManager;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_dynamic;
    }

    @Override
    protected void initView(View view) {
        mLoad = new Loading(getActivity());
        mList = new ArrayList<>();
        llManager = new CustomLLManager(getActivity());
        OkHttpUtils.getInstance().dynamic_index(SPUtils.getValue("token"), mPage + "", "", this);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                llManager.setScrollEnabled(false);
                mPage = 1;
                if (!mList.isEmpty())
                    mList.clear();
                OkHttpUtils.getInstance().dynamic_index(SPUtils.getValue("token"), mPage + "", "", new RequestFinish() {
                    @Override
                    public void onSuccess(BaseModel result, String params) {
                        refreshLayout.setRefreshing(false);
                        llManager.setScrollEnabled(true);
                        mPage++;
                        DynamicData orderList = (DynamicData) result.getData();
                        mList = orderList.getList();
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
    protected void initData() {
        recycleView.setLayoutManager(llManager);
        recycleView.setHasFixedSize(true);
        mAdapter = new BaseQuickAdapter<DynamicData.DynamicList, BaseViewHolder>(R.layout.item_dynamic, mList) {
            @Override
            protected void convert(BaseViewHolder helper, final DynamicData.DynamicList item) {
                CircleImageView iv_head = helper.getView(R.id.iv_head);
                final ImageView iv_zan = helper.getView(R.id.iv_zan);
                final TextView tv_num = helper.getView(R.id.tv_num);
                final TextView tv_content = helper.getView(R.id.tv_content);
                final TextView tv_more = helper.getView(R.id.tv_more);
                ImageView iv_sex = helper.getView(R.id.iv_sex);
                helper.setText(R.id.tv_nickName, item.getNickname());
                helper.setText(R.id.tv_time, item.getCreated_at());
                tv_content.setText(item.getContent());
                tv_num.setText(item.getLike_count() + "");
                if (item.getIs_my().equals("1")) {
                    iv_zan.setImageResource(R.mipmap.zan_pre);
                } else {
                    iv_zan.setImageResource(R.mipmap.zan);
                }
                if (item.getSex().equals("1")) {
                    iv_sex.setImageResource(R.mipmap.dt_boy);
                } else {
                    iv_sex.setImageResource(R.mipmap.girl1);
                }
                Glide.with(getActivity()).load(item.getHead_image()).into(iv_head);
                tv_content.post(new Runnable() {
                    @Override
                    public void run() {
                        if (tv_content.getLineCount() > 5) {
                            tv_content.setMaxLines(5);
                            tv_more.setVisibility(View.VISIBLE);
                        } else {
                            tv_more.setVisibility(View.GONE);
                        }
                    }
                });

                tv_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (tv_content.getMaxLines() > 5) {
                            tv_content.setMaxLines(5);
                            tv_more.setText("全文");
                        } else {
                            tv_content.setMaxLines(Integer.MAX_VALUE);
                            tv_more.setText("收起");
                        }
                    }
                });

                iv_zan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mLoad.show();
                        OkHttpUtils.getInstance().dynamic_like(SPUtils.getValue("token"), item.getDynamic_id(), new RequestFinish() {
                            @Override
                            public void onSuccess(BaseModel result, String params) {
                                mLoad.dismiss();
                                if (item.getIs_my().equals("1")) {
                                    ToastUtil.toastShow("取消点赞成功");
                                    item.setIs_my("2");
                                    iv_zan.setImageResource(R.mipmap.zan);
                                    item.setLike_count((Integer.parseInt(item.getLike_count()) - 1) + "");
                                } else {
                                    ToastUtil.toastShow("点赞成功");
                                    item.setIs_my("1");
                                    iv_zan.setImageResource(R.mipmap.zan_pre);
                                    item.setLike_count((Integer.parseInt(item.getLike_count()) + 1) + "");
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
                item_recycleView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                item_recycleView.setHasFixedSize(true);
                final List<String> imageList = item.getImage();
                BaseQuickAdapter adapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_dynamic_photo, imageList) {
                    @Override
                    protected void convert(final BaseViewHolder helper, final String item) {
                        final ImageView imageView = helper.getView(R.id.iv_photo);
                        Glide.with(getActivity()).load(item).into(imageView);
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (Build.VERSION.SDK_INT >= 21) {
                                    Intent intent = new Intent(getActivity(), PhotoViewActivity.class);
//                                    Intent intent = new Intent(getActivity(), PictureActivity.class);
                                    intent.putExtra("imgUrlList", (Serializable) imageList);
                                    intent.putExtra("position", helper.getAdapterPosition());
                                    intent.putExtra("imageUrl", item);
                                    ActivityOptionsCompat options = ActivityOptionsCompat.
                                            makeSceneTransitionAnimation(getActivity(), imageView, "voicecall");
                                    startActivity(intent, options.toBundle());
                                }
                            }
                        });
                    }
                };
                item_recycleView.setAdapter(adapter);
            }
        };
        mAdapter.setEmptyView(EmptyViewUtil.getEmptyView(getActivity(), 1));
        recycleView.setAdapter(mAdapter);

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                OkHttpUtils.getInstance().dynamic_index(SPUtils.getValue("token"), mPage + "", "", new RequestFinish() {
                    @Override
                    public void onSuccess(BaseModel result, String params) {
                        mAdapter.loadMoreComplete();
                        DynamicData orderList = (DynamicData) result.getData();
                        if (orderList.getList().isEmpty()) {
                            mAdapter.loadMoreEnd();
//                            ToastUtil.toastShow("暂无更多数据");
                        } else {
                            mPage++;
                            mAdapter.addData(orderList.getList());
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(String result) {
                        ToastUtil.toastShow(result);
                        mAdapter.loadMoreComplete();
                    }
                });
            }
        }, recycleView);

        recycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                refreshLayout.setEnabled(topRowVerticalPosition >= 0 && recyclerView != null && !recyclerView.canScrollVertically(-1));

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    @Override
    public void onSuccess(BaseModel result, String params) {
        if (refreshLayout != null && refreshLayout.isRefreshing())
            refreshLayout.setRefreshing(false);
        if (mLoad != null && mLoad.isShowing())
            mLoad.dismiss();
        mPage++;
        DynamicData orderList = (DynamicData) result.getData();
        if (mPage == 2) {
            mList = orderList.getList();
            mAdapter.setNewData(mList);
        } else {
            mAdapter.addData(orderList.getList());
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onError(String result) {
        if (refreshLayout != null && refreshLayout.isRefreshing())
            refreshLayout.setRefreshing(false);
        if (mLoad != null && mLoad.isShowing())
            mLoad.dismiss();
        ToastUtil.toastShow(result);
        mAdapter.loadMoreComplete();
    }

    @OnClick({R.id.tv_release})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_release:
                //发布
                jump(PublishActivity.class);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isRefresh) {
            mPage = 1;
            mLoad.show();
            OkHttpUtils.getInstance().dynamic_index(SPUtils.getValue("token"), mPage + "", "", this);
            isRefresh = !isRefresh;
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
}
