package huidu.com.voicecall.mine;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import huidu.com.voicecall.bean.AnchorInfo;
import huidu.com.voicecall.bean.AnchorPrice;
import huidu.com.voicecall.bean.SpareBean;
import huidu.com.voicecall.http.API;
import huidu.com.voicecall.http.BaseModel;
import huidu.com.voicecall.http.OkHttpUtils;
import huidu.com.voicecall.http.RequestFinish;
import huidu.com.voicecall.main.OrderDetailActivity;
import huidu.com.voicecall.utils.DialogUtil;
import huidu.com.voicecall.utils.Loading;
import huidu.com.voicecall.utils.ToastUtil;

/**
 * 主播技能
 */
public class AnchorsSkillsActivity extends BaseActivity implements RequestFinish {
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.iv_head)
    CircleImageView iv_head;
    @BindView(R.id.iv_vip_type)
    ImageView iv_vip_type;
    @BindView(R.id.iv_background)
    ImageView iv_background;
    @BindView(R.id.iv_audio)
    ImageView iv_audio;
    @BindView(R.id.tv_author_name)
    TextView tv_author_name;
    @BindView(R.id.tv_author_message)
    TextView tv_author_message;
    @BindView(R.id.tv_attention)
    TextView tv_attention;
    @BindView(R.id.tv_chat)
    TextView tv_chat;
    @BindView(R.id.tv_type)
    TextView tv_type;
    @BindView(R.id.iv_attention)
    ImageView iv_attention;
    @BindView(R.id.iv_chat)
    ImageView iv_chat;
    //单价
    @BindView(R.id.tv_price)
    TextView tv_price;

    String anchor_id;
    String anchor_type_id;

    Loading mLoading;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_anchors_skills;
    }

    @Override
    protected void initView() {
        mLoading = new Loading(this);
        anchor_id = getIntent().getStringExtra("anchor_id");
        anchor_type_id = getIntent().getStringExtra("anchor_type_id");
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                OkHttpUtils.getInstance().anchor_info(API.TOKEN_TEST, anchor_id, anchor_type_id, AnchorsSkillsActivity.this);
                OkHttpUtils.getInstance().anchor_price(API.TOKEN_TEST, anchor_id, anchor_type_id, AnchorsSkillsActivity.this);
            }
        });
    }

    @Override
    protected void initData() {
        OkHttpUtils.getInstance().anchor_info(API.TOKEN_TEST, anchor_id, anchor_type_id, this);
        OkHttpUtils.getInstance().anchor_price(API.TOKEN_TEST, anchor_id, anchor_type_id, this);
    }

    @OnClick({R.id.iv_back, R.id.iv_audio, R.id.ll_attention, R.id.ll_chat})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_audio:
                //试听语音
                break;
            case R.id.ll_attention:
                if(anchorInfo.getIs_attention().equals("1")){
                    //取消关注
                    OkHttpUtils.getInstance().user_attention_cancel(API.TOKEN_TEST,anchor_id,this);
                }else {
                    //关注
                    OkHttpUtils.getInstance().user_attention(API.TOKEN_TEST,anchor_id,this);
                }
                break;
            case R.id.ll_chat:
                //约聊
                goChat(anchorPrice.getInfo());

                break;
        }
    }

    AnchorPrice anchorPrice;
    AnchorInfo anchorInfo;
    @Override
    public void onSuccess(BaseModel result, String params) {
        refresh.setRefreshing(false);
        mLoading.dismiss();
        switch (params) {
            case API.ANCHOR_INFO:
                anchorInfo = (AnchorInfo) result.getData();
                Glide.with(this).load(anchorInfo.getHead_image()).into(iv_head);
                if (anchorInfo.getCover().size() > 0) {
                    Glide.with(this).load(anchorInfo.getCover().get(0)).into(iv_background);
                }
                tv_author_name.setText(anchorInfo.getNickname());
                tv_author_message.setText(anchorInfo.getAnchor_type_text() + " | " + anchorInfo.getAge() + "岁 " + (anchorInfo.getSex().equals("1") ? "男" : "女"));
                tv_price.setText(anchorInfo.getPrice());
                String type = anchorInfo.getType();
                tv_type.setText(type.equals("1") ? "Y豆/分钟" : "Y豆/次");

                if (anchorInfo.getIs_attention().equals("1")) {
                    tv_attention.setText("已关注");
                    tv_attention.setTextColor(getResources().getColor(R.color.textColor2));
                } else {
                    tv_attention.setText("关注");
                    tv_attention.setTextColor(getResources().getColor(R.color.textColor));
                }
                break;
            case API.ANCHOR_PRICE:
                anchorPrice = (AnchorPrice) result.getData();
                break;
            case API.ORDER_VOICE:
                //提交订单跳转订单详情页
                ToastUtil.toastShow("下单成功");
                SpareBean spareBean = (SpareBean)result.getData();
                startActivity(new Intent(this,OrderDetailActivity.class)
                        .putExtra("order_no",""+spareBean.getOrder_no())
                        .putExtra("order_type","2"));
                if (alertDialog!=null&&alertDialog.isShowing()){
                    alertDialog.dismiss();
                }
//                jumpTo(OrderDetailActivity.class);
                break;
            case API.USER_ATTENTION:
                initData();
                break;
            case API.USER_ATTENTION_CANCEL:
                initData();
                break;
        }

    }

    @Override
    public void onError(String result) {
        refresh.setRefreshing(false);
        mLoading.dismiss();
        ToastUtil.toastShow(result);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLoading.show();
        initData();
    }

    private void showMessage() {
        DialogUtil.showDialogConfirm(this, "Y豆不足", "你的余额不足，请充值后再和主播约聊哦~", "取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }, "立即充值", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alertDialog!=null&&alertDialog.isShowing()){
                    alertDialog.dismiss();
                }
                jumpTo(MyAccountActivity.class);
            }
        }, View.VISIBLE).show();
    }

    /**
     * 套餐选择的位置
     */
    int TIMES_POSITION = 0;
    int FEE_PRICE = 0;
    AlertDialog alertDialog;
    private void goChat(final AnchorPrice.Info apInfo) {
       alertDialog = new AlertDialog.Builder(this,
                R.style.dialog).create();
        alertDialog.show();
        final Window window = alertDialog.getWindow();
        window.setContentView(R.layout.item_service);
        window.setLayout(
                window.getContext().getResources().getDisplayMetrics().widthPixels,
                WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM);
//        window.setWindowAnimations(R.style.dialog_up_down_animation);
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        alertDialog.setCanceledOnTouchOutside(true);
        RecyclerView recyclerView = (RecyclerView) window.findViewById(R.id.recycleView);
        final ImageView iv_delete = window.findViewById(R.id.iv_delete);
        final ImageView iv_add = window.findViewById(R.id.iv_add);
        final TextView tv_num = window.findViewById(R.id.tv_num);
        final TextView tv_pay = window.findViewById(R.id.tv_pay);
        final TextView tv_total_price = window.findViewById(R.id.tv_total_price);
        final TextView tv_pay_one = window.findViewById(R.id.tv_pay_one);
        if (apInfo.getType().equals("1")) {
            tv_pay_one.setText(apInfo.getPrice() + "Y豆/分钟");
        } else {
            tv_pay_one.setText(apInfo.getPrice() + "Y豆/次");
        }

        tv_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(apInfo.getBalance())>=FEE_PRICE){
                     OkHttpUtils.getInstance().order_voice(API.TOKEN_TEST,anchor_id,anchor_type_id,apInfo.getTimes().get(TIMES_POSITION)+"",tv_num.getText().toString(),AnchorsSkillsActivity.this);
                }else {
                    showMessage();
                }
            }
        });

        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_num.setText((Integer.parseInt(tv_num.getText().toString()) + 1) + "");
                FEE_PRICE = Integer.parseInt(apInfo.getTimes().get(TIMES_POSITION)) * Integer.parseInt(apInfo.getPrice()) * Integer.parseInt(tv_num.getText().toString());
                tv_total_price.setText(FEE_PRICE + "点");
            }
        });
        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_num.getText().toString().equals("1")) {
                    ToastUtil.toastShow("数量不能小于1");
                } else {
                    tv_num.setText((Integer.parseInt(tv_num.getText().toString()) - 1) + "");
                    FEE_PRICE = Integer.parseInt(apInfo.getTimes().get(TIMES_POSITION)) * Integer.parseInt(apInfo.getPrice()) * Integer.parseInt(tv_num.getText().toString());
                    tv_total_price.setText(FEE_PRICE + "点");
                }
            }
        });

        List<String> mList = new ArrayList<>();
        mList.addAll(apInfo.getTimes());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        BaseQuickAdapter adapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_select_service, mList) {
            @Override
            protected void convert(final BaseViewHolder helper, final String item) {
                final TextView tv_time = helper.getView(R.id.tv_time);
                tv_time.setText(item);
                final TextView tv_type = helper.getView(R.id.tv_type);
                final CheckBox check = helper.getView(R.id.check);
                if (TIMES_POSITION == helper.getAdapterPosition()) {
                    FEE_PRICE = Integer.parseInt(apInfo.getTimes().get(TIMES_POSITION)) * Integer.parseInt(apInfo.getPrice()) * Integer.parseInt(tv_num.getText().toString());
                    tv_total_price.setText(FEE_PRICE + "点");
                    check.setChecked(true);
                    tv_time.setTextColor(getResources().getColor(R.color.textSelectColor));
                    tv_type.setTextColor(getResources().getColor(R.color.textSelectColor));
                } else {
                    check.setChecked(false);
                    tv_time.setTextColor(getResources().getColor(R.color.textColor));
                    tv_type.setTextColor(getResources().getColor(R.color.textColor2));
                }

                check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            TIMES_POSITION = helper.getAdapterPosition();
                            notifyDataSetChanged();
                        } else {
                            if (TIMES_POSITION == helper.getAdapterPosition()) {
                                check.setChecked(true);
                            } else {
                                check.setChecked(false);
                            }

                        }
                    }
                });


            }
        };

        recyclerView.setAdapter(adapter);
    }
}
