package huidu.com.voicecall.mine;

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
import huidu.com.voicecall.bean.UserAttention;
import huidu.com.voicecall.bean.UserFans;
import huidu.com.voicecall.http.API;
import huidu.com.voicecall.http.BaseModel;
import huidu.com.voicecall.http.OkHttpUtils;
import huidu.com.voicecall.http.RequestFinish;
import huidu.com.voicecall.utils.EmptyViewUtil;
import huidu.com.voicecall.utils.ToastUtil;

/**
 * 我的关注
 */
public class MyAttentionActivity extends BaseActivity implements RequestFinish{
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    BaseQuickAdapter mAdapter;
    List<UserAttention> mList = new ArrayList<>();
    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_attention;
    }

    @Override
    protected void initView() {
        tv_title.setText("我的关注");

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                OkHttpUtils.getInstance().user_attention_list(API.TOKEN_TEST,MyAttentionActivity.this);
            }
        });
    }

    @Override
    protected void initData() {
        OkHttpUtils.getInstance().user_attention_list(API.TOKEN_TEST,this);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.setHasFixedSize(true);
        mAdapter = new BaseQuickAdapter<UserAttention,BaseViewHolder>(R.layout.item_attention,mList) {
            @Override
            protected void convert(BaseViewHolder helper,final UserAttention item) {
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
                Glide.with(MyAttentionActivity.this).load(item.getHead_image()).apply(new RequestOptions().error(R.mipmap.wd_tx_nor)).into(iv_head);
                tv_attention.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OkHttpUtils.getInstance().user_attention_cancel(API.TOKEN_TEST,item.getUser_id()+"",MyAttentionActivity.this);
                    }
                });
            }
        };
        mAdapter.setEmptyView(EmptyViewUtil.getEmptyView(this,4));
        recycleView.setAdapter(mAdapter);
    }

    @Override
    public void onSuccess(BaseModel result, String params) {
        switch (params){
            case API.USER_ATTENTION_LIST:
                refreshLayout.setRefreshing(false);
                mList = (List<UserAttention>)result.getData();
                mAdapter.setNewData(mList);
                break;
            case API.USER_ATTENTION_CANCEL:
                OkHttpUtils.getInstance().user_attention_list(API.TOKEN_TEST,this);
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
