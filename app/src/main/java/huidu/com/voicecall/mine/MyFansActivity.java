package huidu.com.voicecall.mine;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import huidu.com.voicecall.bean.UserFans;
import huidu.com.voicecall.http.API;
import huidu.com.voicecall.http.BaseModel;
import huidu.com.voicecall.http.OkHttpUtils;
import huidu.com.voicecall.http.RequestFinish;
import huidu.com.voicecall.utils.ToastUtil;

/**
 * 粉丝列表
 */
public class MyFansActivity extends BaseActivity implements RequestFinish {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    BaseQuickAdapter mAdapter;

    List<UserFans> mList = new ArrayList<>();
    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_fans;
    }

    @Override
    protected void initView() {
        tv_title.setText("我的粉丝");
    }

    @Override
    protected void initData() {
        OkHttpUtils.getInstance().user_fans_list(API.TOKEN_TEST,this);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.setHasFixedSize(true);
        mAdapter = new BaseQuickAdapter<UserFans,BaseViewHolder>(R.layout.item_fans,mList) {
            @Override
            protected void convert(BaseViewHolder helper, UserFans item) {
                CircleImageView iv_head = helper.getView(R.id.iv_head);
                ImageView iv_sex = helper.getView(R.id.iv_sex);
                ImageView iv_attention = helper.getView(R.id.iv_attention);
                helper.setText(R.id.tv_userId,item.getUser_id());
                helper.setText(R.id.tv_age,item.getAge());
                if (item.getSex().equals("1")){
                    iv_sex.setImageResource(R.mipmap.boy);
                }else if (item.getSex().equals("2")){
                    iv_sex.setImageResource(R.mipmap.girl);
                }
                Glide.with(MyFansActivity.this).load(item.getHead_image()).into(iv_head);
                iv_attention.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e(TAG, "onClick: "+"已关注" );
                    }
                });
            }
        };
        recycleView.setAdapter(mAdapter);
    }

    @Override
    public void onSuccess(BaseModel result, String params) {
        switch (params){
            case API.USER_FANS_LIST:
                mList = (List<UserFans>)result.getData();
                mAdapter.setNewData(mList);
                break;
        }
    }

    @Override
    public void onError(String result) {
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
