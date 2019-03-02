package huidu.com.voicecall.mine;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import huidu.com.voicecall.R;
import huidu.com.voicecall.base.BaseActivity;
import huidu.com.voicecall.bean.UserFans;
import huidu.com.voicecall.http.API;
import huidu.com.voicecall.http.BaseModel;
import huidu.com.voicecall.http.OkHttpUtils;
import huidu.com.voicecall.http.RequestFinish;
import huidu.com.voicecall.main.DynamicFragment;
import huidu.com.voicecall.utils.EmptyViewUtil;
import huidu.com.voicecall.utils.Loading;
import huidu.com.voicecall.utils.SPUtils;
import huidu.com.voicecall.utils.ToastUtil;

/**
 * 粉丝列表
 */
public class MyFansActivity extends BaseActivity implements RequestFinish {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    BaseQuickAdapter mAdapter;

    List<UserFans> mList = new ArrayList<>();
    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_fans;
    }

    @Override
    protected void initView() {
        tv_title.setText("我的粉丝");
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                OkHttpUtils.getInstance().user_fans_list(SPUtils.getValue("token"),MyFansActivity.this);
            }
        });
    }

    @Override
    protected void initData() {
        OkHttpUtils.getInstance().user_fans_list(SPUtils.getValue("token"),this);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.setHasFixedSize(true);
        mAdapter = new BaseQuickAdapter<UserFans,BaseViewHolder>(R.layout.item_fans,mList) {
            @Override
            protected void convert(BaseViewHolder helper, final UserFans item) {
                CircleImageView iv_head = helper.getView(R.id.iv_head);
                ImageView iv_sex = helper.getView(R.id.iv_sex);
                TextView tv_attention = helper.getView(R.id.tv_attention);
                LinearLayout ll_sex_age = helper.getView(R.id.ll_sex_age);
                helper.setText(R.id.tv_userId,item.getNickname());
                helper.setText(R.id.tv_age,item.getAge());
                if (item.getSex().equals("1")){
                    ll_sex_age.setBackgroundResource(R.drawable.shape_corner5_boy);
                    iv_sex.setImageResource(R.mipmap.boy);
                }else if (item.getSex().equals("2")){
                    ll_sex_age.setBackgroundResource(R.drawable.shape_corner5_red);
                    iv_sex.setImageResource(R.mipmap.girl);
                }
                if (item.getIs_attention().equals("1")){
                    tv_attention.setText("已关注");
                    tv_attention.setTextColor(getResources().getColor(R.color.textColor2));
                    tv_attention.setBackgroundResource(R.drawable.shape_bg_eb15);
                }else if (item.getIs_attention().equals("0")){
                    tv_attention.setText("关注");
                    tv_attention.setTextColor(getResources().getColor(R.color.white));
                    tv_attention.setBackgroundResource(R.mipmap.tc_button_pre);
                }
                Glide.with(MyFansActivity.this).load(item.getHead_image()).apply(new RequestOptions().error(R.mipmap.wd_tx_nor)).into(iv_head);
                tv_attention.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mLoading.show();
                        if (item.getIs_attention().equals("1")){
                            OkHttpUtils.getInstance().user_attention_cancel(SPUtils.getValue("token"),item.getUser_id()+"",MyFansActivity.this);
                        }else {
                            OkHttpUtils.getInstance().user_attention(SPUtils.getValue("token"),item.getUser_id()+"","",MyFansActivity.this);
                        }
                    }
                });
            }
        };
        mAdapter.setEmptyView(EmptyViewUtil.getEmptyView(this,3));
        recycleView.setAdapter(mAdapter);
    }

    @Override
    public void onSuccess(BaseModel result, String params) {
        switch (params){
            case API.USER_FANS_LIST:
                refreshLayout.setRefreshing(false);
                mList = (List<UserFans>)result.getData();
                mAdapter.setNewData(mList);
                break;
            case API.USER_ATTENTION:
                finishLoad();
                DynamicFragment.isRefresh = true;
                ToastUtil.toastShow("关注成功");
                OkHttpUtils.getInstance().user_fans_list(SPUtils.getValue("token"),this);
                break;
            case API.USER_ATTENTION_CANCEL:
                finishLoad();
                DynamicFragment.isRefresh = true;
                ToastUtil.toastShow("取消关注成功");
                OkHttpUtils.getInstance().user_fans_list(SPUtils.getValue("token"),this);
                break;
        }
    }

    @Override
    public void onError(String result) {
        refreshLayout.setRefreshing(false);
        finishLoad();
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
