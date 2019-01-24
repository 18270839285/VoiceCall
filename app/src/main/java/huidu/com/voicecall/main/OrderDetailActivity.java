package huidu.com.voicecall.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import huidu.com.voicecall.R;
import huidu.com.voicecall.base.BaseActivity;
import huidu.com.voicecall.bean.OrderInfo;
import huidu.com.voicecall.http.API;
import huidu.com.voicecall.http.BaseModel;
import huidu.com.voicecall.http.OkHttpUtils;
import huidu.com.voicecall.http.RequestFinish;
import huidu.com.voicecall.utils.DateUtil;
import huidu.com.voicecall.utils.DialogUtil;
import huidu.com.voicecall.utils.SPUtils;
import huidu.com.voicecall.utils.ToastUtil;
import huidu.com.voicecall.voice.ChattingRoomActivity;

/**
 * 订单详情
 */
public class OrderDetailActivity extends BaseActivity implements RequestFinish {
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_right)
    ImageView iv_right;
    @BindView(R.id.iv_head)
    ImageView iv_head;
    @BindView(R.id.iv_sex)
    ImageView iv_sex;
    @BindView(R.id.iv_icon)
    ImageView iv_icon;
    @BindView(R.id.tv_status)
    TextView tv_status;
    @BindView(R.id.tv_nickName)
    TextView tv_nickName;
    @BindView(R.id.tv_age)
    TextView tv_age;
    @BindView(R.id.tv_time_status)
    TextView tv_time_status;
    @BindView(R.id.tv_order_num)
    TextView tv_order_num;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.tv_order_type)
    TextView tv_order_type;
    @BindView(R.id.tv_order_time)
    TextView tv_order_time;
    @BindView(R.id.tv_pay_type)
    TextView tv_pay_type;
    @BindView(R.id.tv_pay)
    TextView tv_pay;
    @BindView(R.id.tv_message)
    TextView tv_message;
    @BindView(R.id.tv_start)
    TextView tv_start;
    @BindView(R.id.tv_refuse)
    TextView tv_refuse;
    @BindView(R.id.tv_agree)
    TextView tv_agree;
    @BindView(R.id.ll_sure)
    LinearLayout ll_sure;
    @BindView(R.id.ll_times_over)
    LinearLayout ll_times_over;

    private String order_no;
    private String order_type = "2";//1：已接单 2：已下单

    private CountDownTimer mTimer;
    private int ORDER_STATUS = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_detail;
    }

    @Override
    protected void initView() {
        tv_title.setText("订单详情");
        iv_right.setVisibility(View.VISIBLE);
        order_no = getIntent().getStringExtra("order_no");
        order_type = getIntent().getStringExtra("order_type");

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
    }

    @Override
    protected void initData() {
        OkHttpUtils.getInstance().order_info(SPUtils.getValue("token"), order_no, this);
    }

    private void initTimer(int times) {
        if (mTimer == null) {
            mTimer = new CountDownTimer((long) (times*1000), 1000) {
                @Override
                public void onTick(long millisUntilFinished) {//记时过程显示
                    if (!OrderDetailActivity.this.isFinishing()) {
                        int remainTime = (int) (millisUntilFinished / 1000L);
                        Log.e("initTimer", "======remainTime=====" + remainTime);
                        long hour = (remainTime / (60 * 60 ));
                        long min = ((remainTime / 60) - hour * 60);
                        long s = (remainTime - hour * 60 * 60 - min * 60);
                        String time = "";
                        if (hour>0){
                            if (min<10)
                                time = hour+":0"+min+":"+s;
                            else
                                time = hour+":"+min+":"+s;

                        }else if (min>0){
                            if (s<10)
                                time = min+":0"+s;
                            else
                                time = min+":"+s;
                        }else {
                            time = s+"s";
                        }
                        tv_time.setText(time);
                    }
                }

                @Override
                public void onFinish() {
                    Log.e("initTimer", "onFinish: ");
                    OkHttpUtils.getInstance().order_info(SPUtils.getValue("token"), order_no, OrderDetailActivity.this);
                }
            };
            mTimer.start();
        }
    }

    private OrderInfo.InfoBean infoBean;
    @Override
    public void onSuccess(BaseModel result, String params) {
        switch (params) {
            case API.ORDER_BEGIN:
                //订单开始
                break;
            case API.ORDER_CANCEL:
                //取消订单
                initData();
                break;
            case API.ORDER_FINISH:
                //订单完成
                initData();
                break;
            case API.ORDER_OVER:
                //通话结束
                break;
            case API.ORDER_REFUSE:
                //拒绝订单
                initData();
                break;
            case API.ORDER_RECEIVE:
                //主播接单
                initData();
                break;
            case API.ORDER_INFO:
                refreshLayout.setRefreshing(false);
                if (mTimer != null) {
                    mTimer.cancel();
                    mTimer = null;
                }
                //订单开始
                OrderInfo orderInfo = (OrderInfo) result.getData();
                infoBean = orderInfo.getInfo();
                RequestOptions mRequestOptions = RequestOptions.circleCropTransform()
                        .error(R.mipmap.wd_tx_nor)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)//不做磁盘缓存
                        .skipMemoryCache(true);//不做内存缓存
                Glide.with(this).load(infoBean.getHead_image()).apply(mRequestOptions).into(iv_head);

                tv_nickName.setText(infoBean.getNickname());
                tv_order_type.setText(infoBean.getAnchor_type_text());
                tv_age.setText(infoBean.getAge());
                tv_order_time.setText(DateUtil.getTime2(infoBean.getCreate_time()));

                if (infoBean.getAnchor_bus_type().equals("1")) {
                    tv_order_num.setText(infoBean.getTimes() + "分钟x" + infoBean.getNum());
                    tv_pay_type.setText(infoBean.getAnchor_price() + "Y豆/分钟");
                } else {
                    tv_order_num.setText(infoBean.getTimes() + "次x" + infoBean.getNum());
                    tv_pay_type.setText(infoBean.getAnchor_price() + "Y豆/次");
                }
                if (infoBean.getSex().equals("1")) {
                    iv_sex.setImageResource(R.mipmap.boy);
                }else {
                    iv_sex.setImageResource(R.mipmap.girl);
                }
                tv_pay.setText(infoBean.getTotal() + "Y豆");
                tv_message.setVisibility(View.GONE);
                ll_sure.setVisibility(View.GONE);
                tv_start.setVisibility(View.GONE);
                ll_times_over.setVisibility(View.VISIBLE);
                tv_time_status.setVisibility(View.VISIBLE);
                tv_time.setVisibility(View.VISIBLE);
                iv_right.setVisibility(View.GONE);
                switch (infoBean.getStatus()) {
                    case "1":
//                      "待确定";\
                        ORDER_STATUS = 1;
                        tv_status.setText("用户下单");
//                        tv_status.setText("待确定");
                        tv_message.setVisibility(View.VISIBLE);
                        iv_icon.setImageResource(R.mipmap.wddd_dfw);
                        tv_message.setText("若主播在15分钟内未确定订单，订单将会自动取消");
                        tv_time_status.setText("接单倒计时：");
                        if (Integer.parseInt(infoBean.getExpire_time())>0){
                            initTimer(Integer.parseInt(infoBean.getExpire_time()));
                        }
                        if (order_type.equals("1")) {
                            ll_sure.setVisibility(View.VISIBLE);
                            tv_agree.setText("同意");
                            tv_refuse.setText("拒绝");
                        } else {
                            iv_right.setVisibility(View.VISIBLE);
                            tv_start.setVisibility(View.VISIBLE);
                            tv_start.setText("提醒主播");
                        }

                        break;
                    case "2":
//                      "待服务";
                        ORDER_STATUS = 2;
                        tv_status.setText("主播接单");
//                        tv_status.setText("待服务");
                        tv_message.setVisibility(View.VISIBLE);
                        iv_icon.setImageResource(R.mipmap.wddd_dfw);
                        tv_time_status.setText("开始服务倒计时：");
                        if (Integer.parseInt(infoBean.getExpire_time())>0){
                            initTimer(Integer.parseInt(infoBean.getExpire_time()));
                        }
                        if (order_type.equals("1")) {
                            tv_start.setVisibility(View.VISIBLE);
                            tv_start.setText("立即开始");
                            tv_message.setText("主播接单后，有5分钟的准备时间!");
                        } else {
                            iv_right.setVisibility(View.VISIBLE);
                            tv_message.setText("主播接单后，您有5分钟的准备时间!");

                        }
                        break;
                    case "3":
//                      "进行中";
                        ORDER_STATUS = 3;
                        tv_status.setText("交易进行中");
                        tv_message.setVisibility(View.VISIBLE);
                        iv_icon.setImageResource(R.mipmap.wddd_dfw);
                        if (Integer.parseInt(infoBean.getExpire_time())>0){
                            initTimer(Integer.parseInt(infoBean.getExpire_time()));
                        }
                        if (order_type.equals("1")) {
                            tv_message.setText("主播需要在5分钟内主动联系客户哦，否则订单会自动取消的哦!");
                            tv_start.setVisibility(View.VISIBLE);
                            tv_time_status.setText("开始服务倒计时：");
                            tv_start.setText("联系Ta");
                        } else {
                            tv_time_status.setText("自动完成倒计时：");
                            tv_message.setText("服务已经开始，赶紧联系主播吧!");
                            ll_sure.setVisibility(View.VISIBLE);
                            tv_refuse.setText("联系Ta");
                            tv_agree.setText("完成订单");
                        }

                        break;
                    case "5":
//                     "已完成";
                        ORDER_STATUS = 5;
                        tv_status.setText("交易完成");
                        ll_times_over.setVisibility(View.GONE);
                        iv_icon.setImageResource(R.mipmap.wddd_ywc);
//                        tv_time_status.setVisibility(View.GONE);
//                        tv_time.setVisibility(View.GONE);
                        break;
                    case "6":
//                     "已取消";
                        ORDER_STATUS = 6;
                        tv_status.setText("取消订单 ");
                        iv_icon.setImageResource(R.mipmap.wddd_yqx);
                        ll_times_over.setVisibility(View.GONE);
//                        tv_time_status.setVisibility(View.GONE);
//                        tv_time.setVisibility(View.GONE);
                        break;
                    case "7":
//                      "已拒绝";
                        ORDER_STATUS = 7;
                        tv_status.setText("拒绝订单");
                        ll_times_over.setVisibility(View.GONE);
                        iv_icon.setImageResource(R.mipmap.wddd_yqx);
//                        tv_time_status.setVisibility(View.GONE);
//                        tv_time.setVisibility(View.GONE);
                        break;
                    case "8":
//                     "已关闭";
                        ORDER_STATUS = 8;
                        tv_status.setText("自动关闭");
                        ll_times_over.setVisibility(View.GONE);
                        iv_icon.setImageResource(R.mipmap.wddd_yqx);
//                        tv_time_status.setVisibility(View.GONE);
//                        tv_time.setVisibility(View.GONE);
                        break;
                }
                break;
        }
    }

    @Override
    public void onError(String result) {
        if (refreshLayout!=null&&refreshLayout.isRefreshing()){
            refreshLayout.setRefreshing(false);
        }
        ToastUtil.toastShow(result);
    }

    @OnClick({R.id.iv_back,R.id.tv_refuse,R.id.tv_agree,R.id.tv_start,R.id.iv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_start:
                if (order_type.equals("1")){
                    if (ORDER_STATUS==2){
                        //立即开始
                        startActivity(new Intent(this,ChattingRoomActivity.class)
                                .putExtra("IDENTITY_TYPE",2)
                                .putExtra("nickname",infoBean.getNickname())
                                .putExtra("order_no",order_no)
                                .putExtra("accid2",infoBean.getAccid())
                                .putExtra("head_image",infoBean.getHead_image()));
                    }else if (ORDER_STATUS==3){
                        //联系Ta
                        startActivity(new Intent(this,ChattingRoomActivity.class)
                                .putExtra("IDENTITY_TYPE",2)
                                .putExtra("nickname",infoBean.getNickname())
                                .putExtra("order_no",order_no)
                                .putExtra("accid2",infoBean.getAccid())
                                .putExtra("head_image",infoBean.getHead_image()));
                    }
                }else {
                    //提醒主播
                    ToastUtil.toastShow("提醒成功，请耐心等待！");
                }

                break;
            case R.id.tv_refuse:
                if (order_type.equals("1")){
                    //拒绝
                    OkHttpUtils.getInstance().order_refuse(SPUtils.getValue("token"),order_no,this);
                }else {
                    //联系Ta
                    startActivity(new Intent(this,ChattingRoomActivity.class)
                            .putExtra("IDENTITY_TYPE",1)
                            .putExtra("nickname",infoBean.getNickname())
                            .putExtra("accid2",infoBean.getAccid())
                            .putExtra("head_image",infoBean.getHead_image())
                            .putExtra("order_no",order_no));
                }
                break;
            case R.id.tv_agree:
                if (order_type.equals("1")){
                    //同意
                    OkHttpUtils.getInstance().order_receive(SPUtils.getValue("token"),order_no,this);
                }else {
                    //完成订单
                    DialogUtil.showDialogConfirm1(this, "是否确定完成订单?", "取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }, "确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            OkHttpUtils.getInstance().order_finish(SPUtils.getValue("token"),order_no,OrderDetailActivity.this);
                        }
                    },View.VISIBLE).show();

                }
                break;
            case R.id.iv_right:
//                DialogUtil.showDialogConfim();
                DialogUtil.showOrderCancelDialog(this, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OkHttpUtils.getInstance().order_cancel(SPUtils.getValue("token"),order_no,OrderDetailActivity.this);
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        OkHttpUtils.getInstance().order_info(SPUtils.getValue("token"), order_no, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }
}
