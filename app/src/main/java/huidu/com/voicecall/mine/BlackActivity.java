package huidu.com.voicecall.mine;

import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import huidu.com.voicecall.R;
import huidu.com.voicecall.base.BaseActivity;
import huidu.com.voicecall.bean.UserBlack;
import huidu.com.voicecall.http.API;
import huidu.com.voicecall.http.BaseModel;
import huidu.com.voicecall.http.OkHttpUtils;
import huidu.com.voicecall.http.RequestFinish;
import huidu.com.voicecall.main.DynamicFragment;
import huidu.com.voicecall.utils.EmptyViewUtil;
import huidu.com.voicecall.utils.SPUtils;
import huidu.com.voicecall.utils.SwipeItemLayout;
import huidu.com.voicecall.utils.ToastUtil;

/**
 * 黑名單
 */
public class BlackActivity extends BaseActivity implements RequestFinish {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    BaseQuickAdapter mAdapter;

    int mPage = 1;
    List<UserBlack.BlackList> mList = new ArrayList<>();
    @Override
    protected int getLayoutId() {
        return R.layout.activity_black;
    }

    @Override
    protected void initView() {
        tv_title.setText("黑名單");
        mPage = 1;
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                OkHttpUtils.getInstance().user_my_black(SPUtils.getValue("token"),mPage+"",BlackActivity.this);
            }
        });
    }

    @Override
    protected void initData() {
        mLoading.show();
        OkHttpUtils.getInstance().user_my_black(SPUtils.getValue("token"),mPage+"",BlackActivity.this);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(this));
        recycleView.setHasFixedSize(true);
        mAdapter = new BaseQuickAdapter<UserBlack.BlackList,BaseViewHolder>(R.layout.item_black2,mList) {
            @Override
            protected void convert(final BaseViewHolder helper, final UserBlack.BlackList item) {
                CircleImageView iv_head = helper.getView(R.id.iv_head);
                ImageView iv_sex = helper.getView(R.id.iv_sex);
                LinearLayout ll_sex_age = helper.getView(R.id.ll_sex_age);
                helper.setText(R.id.tv_userId,item.getNickname());
                helper.setText(R.id.tv_age,item.getAge());
                Button delete = helper.getView(R.id.bt_delete);
                if (item.getSex().equals("1")){
                    ll_sex_age.setBackgroundResource(R.drawable.shape_corner5_boy);
                    iv_sex.setImageResource(R.mipmap.boy);
                }else if (item.getSex().equals("2")){
                    ll_sex_age.setBackgroundResource(R.drawable.shape_corner5_red);
                    iv_sex.setImageResource(R.mipmap.girl);
                }

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mLoading.show();
                        OkHttpUtils.getInstance().user_remove_black(SPUtils.getValue("token"), mList.get(helper.getAdapterPosition()).getBlack_id(), new RequestFinish() {
                            @Override
                            public void onSuccess(BaseModel result, String params) {
                                finishLoad();
                                mList.remove(helper.getAdapterPosition());
                                ToastUtil.toastShow("移出成功");
                                sendRefresh();
                                DynamicFragment.isRefresh = true;
                                mAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onError(String result) {
                                finishLoad();
                            }
                        });
                    }
                });
                Glide.with(BlackActivity.this).load(item.getHead_image()).apply(new RequestOptions().error(R.mipmap.wd_tx_nor)).into(iv_head);
            }
        };
//        mAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, final int position) {
//                DialogUtil.showDialogConfirm1(BlackActivity.this, "确定移出黑名单？", "取消", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                }, "确定", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mLoading.show();
//                        OkHttpUtils.getInstance().user_remove_black(SPUtils.getValue("token"), mList.get(position).getBlack_id(), new RequestFinish() {
//                            @Override
//                            public void onSuccess(BaseModel result, String params) {
//                                finishLoad();
//                                mList.remove(position);
//                                ToastUtil.toastShow("移出成功");
//                                sendRefresh();
//                                DynamicFragment.isRefresh = true;
//                                mAdapter.notifyDataSetChanged();
//                            }
//
//                            @Override
//                            public void onError(String result) {
//                                finishLoad();
//                            }
//                        });
//                    }
//                },View.VISIBLE).show();
//                return false;
//            }
//        });
        mAdapter.setEmptyView(EmptyViewUtil.getEmptyView(this,7));
        recycleView.setAdapter(mAdapter);
    }

    @Override
    public void onSuccess(BaseModel result, String params) {
        finishLoad();
        switch (params){
            case API.USER_MY_BLACK:
                //我的黑名單
                if (refreshLayout!=null&&refreshLayout.isRefreshing()){
                    refreshLayout.setRefreshing(false);
                }
                UserBlack userBlack = (UserBlack) result.getData();
                mList = userBlack.getList();
                mAdapter.setNewData(mList);
                break;
        }
    }

    @Override
    public void onError(String result) {
        finishLoad();
        if (refreshLayout!=null&&refreshLayout.isRefreshing()){
            refreshLayout.setRefreshing(false);
        }
        ToastUtil.toastShow(result);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }
}
