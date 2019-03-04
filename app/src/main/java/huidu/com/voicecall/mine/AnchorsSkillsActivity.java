package huidu.com.voicecall.mine;

import android.app.AlertDialog;
import android.content.Intent;
import android.media.MediaPlayer;
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

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import huidu.com.voicecall.R;
import huidu.com.voicecall.VoiceApp;
import huidu.com.voicecall.base.BaseActivity;
import huidu.com.voicecall.bean.AnchorInfo;
import huidu.com.voicecall.bean.AnchorPrice;
import huidu.com.voicecall.bean.SpareBean;
import huidu.com.voicecall.dynamic.ReportActivity;
import huidu.com.voicecall.http.API;
import huidu.com.voicecall.http.BaseModel;
import huidu.com.voicecall.http.OkHttpUtils;
import huidu.com.voicecall.http.RequestFinish;
import huidu.com.voicecall.main.OrderDetailActivity;
import huidu.com.voicecall.utils.DialogUtil;
import huidu.com.voicecall.utils.Loading;
import huidu.com.voicecall.utils.RefreshEvent;
import huidu.com.voicecall.utils.SPUtils;
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
    @BindView(R.id.iv_more2)
    ImageView iv_more2;
    //单价
    @BindView(R.id.tv_price)
    TextView tv_price;

    private String anchor_id;
    private String anchor_type_id;

    private MediaPlayer mediaPlayer;
    private String audioUrl = "";
    private boolean isRobot = false;
    private boolean isAttention = false;

    AnchorPrice anchorPrice;
    AnchorInfo anchorInfo;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_anchors_skills;
    }

    @Override
    protected void initView() {
        anchor_id = getIntent().getStringExtra("anchor_id");
        anchor_type_id = getIntent().getStringExtra("anchor_type_id");
        isRobot = getIntent().getBooleanExtra("isRobot",false);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                OkHttpUtils.getInstance().anchor_info(SPUtils.getValue("token"), anchor_id, anchor_type_id, isRobot, AnchorsSkillsActivity.this);
                OkHttpUtils.getInstance().anchor_price(SPUtils.getValue("token"), anchor_id, anchor_type_id, isRobot, AnchorsSkillsActivity.this);
            }
        });
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.iv_back, R.id.iv_audio, R.id.ll_attention, R.id.ll_chat,R.id.iv_more2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_audio:
                //试听语音
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                } else {
                    if (!audioUrl.isEmpty()) {
                        startAudio(audioUrl);
                    }
                }
                break;
            case R.id.ll_attention:
                if (isAttention){
                    //取消关注
                    mLoading.show();
                    OkHttpUtils.getInstance().user_attention_cancel(SPUtils.getValue("token"), anchor_id, this);
                }else {
                    //关注
                    mLoading.show();
                    OkHttpUtils.getInstance().user_attention(SPUtils.getValue("token"), anchor_id,isRobot?"1":"" ,this);
                }
                isAttention = !isAttention;
                break;
            case R.id.ll_chat:
                //约聊
                goChat(anchorPrice.getInfo());
                break;
            case R.id.iv_more2:
                Log.e(TAG, "onViewClicked: "+"onRefreshEvent" );
                EventBus.getDefault().post(new RefreshEvent("刷新"));
                //弹窗选择关注，屏蔽，举报
                DialogUtil.showDialogDynamic(this, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OkHttpUtils.getInstance().user_attention(SPUtils.getValue("token"), anchorInfo.getAnchor_id(), "", new RequestFinish() {
                            @Override
                            public void onSuccess(BaseModel result, String params) {
                                ToastUtil.toastShow("关注成功");
                                isAttention = !isAttention;
                                setAttention();
                            }

                            @Override
                            public void onError(String result) {
                                ToastUtil.toastShow(result);
                            }
                        });
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUtil.showDialogConfirm1(AnchorsSkillsActivity.this, "屏蔽后，将会在24小时内，看不到Ta的相关信息，是否继续？", "取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }, "屏蔽", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mLoading.show();
                               OkHttpUtils.getInstance().user_black(SPUtils.getValue("token"),anchorInfo.getAnchor_id(),"2",AnchorsSkillsActivity.this);
                            }
                        }, View.VISIBLE).show();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(AnchorsSkillsActivity.this, ReportActivity.class).putExtra("userId", anchorInfo.getAnchor_id()));
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUtil.showDialogConfirm1(AnchorsSkillsActivity.this, "确定要将Ta拉入黑名单吗？", "取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        }, "拉黑", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mLoading.show();
                                OkHttpUtils.getInstance().user_black(SPUtils.getValue("token"),anchorInfo.getAnchor_id(),"1",AnchorsSkillsActivity.this);
                            }
                        }, View.VISIBLE).show();
                    }
                }, isAttention ? "1" : "2").show();
                break;
        }
    }

    @Override
    public void onSuccess(BaseModel result, String params) {
        refresh.setRefreshing(false);
        switch (params) {
            case API.ANCHOR_INFO:
                finishLoad();
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
                    isAttention = true;
                } else {
                    isAttention = false;
                }
                setAttention();
                audioUrl = anchorInfo.getVoice();
                if (SPUtils.getValue("user_id").equals(anchorInfo.getAnchor_id())) {
                    iv_more2.setVisibility(View.GONE);
                }
                break;
            case API.ANCHOR_PRICE:
                anchorPrice = (AnchorPrice) result.getData();
                break;
            case API.ORDER_VOICE:
                //提交订单跳转订单详情页
                ToastUtil.toastShow("下单成功");
                SpareBean spareBean = (SpareBean) result.getData();
                startActivity(new Intent(this, OrderDetailActivity.class)
                        .putExtra("order_no", "" + spareBean.getOrder_no())
                        .putExtra("order_type", "2"));
                if (alertDialog != null && alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }
//                jumpTo(OrderDetailActivity.class);
                break;
            case API.USER_ATTENTION:
                finishLoad();
                ToastUtil.toastBottom(this,"关注成功");
                setAttention();
                break;
            case API.USER_ATTENTION_CANCEL:
                finishLoad();
                ToastUtil.toastBottom(this,"取消关注成功");
                setAttention();
                break;
            case API.USER_BLACK:
                finishLoad();
                ToastUtil.toastShow("操作成功");
                finish();
                break;
        }

    }

    private void setAttention(){
        if (isAttention) {
            tv_attention.setText("已关注");
            tv_attention.setTextColor(getResources().getColor(R.color.textColor2));
            iv_attention.setImageResource(R.mipmap.zbjn_ygz);
        } else {
            tv_attention.setText("关注");
            tv_attention.setTextColor(getResources().getColor(R.color.textColor));
            iv_attention.setImageResource(R.mipmap.zbjn_wgz);
        }
    }


    @Override
    public void onError(String result) {
        if (refresh!=null&&refresh.isRefreshing()){
            refresh.setRefreshing(false);
        }
        finishLoad();
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
        OkHttpUtils.getInstance().anchor_info(SPUtils.getValue("token"), anchor_id, anchor_type_id, isRobot, this);
        OkHttpUtils.getInstance().anchor_price(SPUtils.getValue("token"), anchor_id, anchor_type_id, isRobot, this);
    }

    private void showMessage() {
        DialogUtil.showDialogConfirm(this, "Y豆不足", "你的余额不足，请充值后再和主播约聊哦~", "取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }, "立即充值", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alertDialog != null && alertDialog.isShowing()) {
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
                if (Integer.parseInt(apInfo.getBalance()) >= FEE_PRICE) {
                    OkHttpUtils.getInstance().order_voice(SPUtils.getValue("token"), anchor_id, anchor_type_id, apInfo.getTimes().get(TIMES_POSITION) + "", tv_num.getText().toString(), AnchorsSkillsActivity.this);
                } else {
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

    private void startAudio(String audioUrl) {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioUrl);
            //3 准备播放
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer media) {
                    if (mediaPlayer!=null){
                        mediaPlayer.start();
                    }
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer media) {
                    if (mediaPlayer!=null&&mediaPlayer.isPlaying()){
                        mediaPlayer.stop();
                    }
                    mediaPlayer = null;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer = null;
        }
    }
}
