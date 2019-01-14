package huidu.com.voicecall.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import huidu.com.voicecall.utils.ToastUtil;

/**
 * 订单详情
 */
public class OrderDetailActivity extends BaseActivity implements RequestFinish{
    @BindView(R.id.tv_title)
    TextView tv_title;
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

    private String order_no;
    private String order_type;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_detail;
    }

    @Override
    protected void initView() {
        tv_title.setText("订单详情");
        order_no = getIntent().getStringExtra("order_no");
        order_type = getIntent().getStringExtra("order_type");
    }

    @Override
    protected void initData() {
        OkHttpUtils.getInstance().order_info(API.TOKEN_TEST,order_no,this);
    }

    @Override
    public void onSuccess(BaseModel result, String params) {
        switch (params){
            case API.ORDER_BEGIN:
                //订单开始
                break;
            case API.ORDER_CANNEL:
                //订单开始
                break;
            case API.ORDER_FINISH:
                //订单开始
                break;
            case API.ORDER_OVER:
                //订单开始
                break;
            case API.ORDER_REFUSE:
                //拒绝订单
                break;
            case API.ORDER_RECEIVEI:
                //主播接单
                break;
            case API.ORDER_INFO:
                //订单开始
                OrderInfo orderInfo = (OrderInfo)result.getData();
                OrderInfo.InfoBean infoBean = orderInfo.getInfo();
                RequestOptions mRequestOptions = RequestOptions.circleCropTransform()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)//不做磁盘缓存
                        .skipMemoryCache(true);//不做内存缓存
                Glide.with(this).load(infoBean.getHead_image()).apply(mRequestOptions).into(iv_head);

                tv_nickName.setText(infoBean.getNickname());
                tv_order_type.setText(infoBean.getAnchor_type_text());
                tv_order_time.setText(DateUtil.getTime2(infoBean.getCreate_time()));
                if (infoBean.getAnchor_bus_type().equals("1")){
                    tv_order_num.setText(infoBean.getTimes()+"分钟x"+infoBean.getNum());
                    tv_pay_type.setText(infoBean.getAnchor_price()+"虚拟币1/分钟");
                }else {
                    tv_order_num.setText(infoBean.getTimes()+"次x"+infoBean.getNum());
                    tv_pay_type.setText(infoBean.getAnchor_price()+"虚拟币1/次");
                }
                tv_pay.setText(infoBean.getTotal()+"虚拟币1");
                tv_message.setVisibility(View.GONE);
                ll_sure.setVisibility(View.GONE);
                tv_start.setVisibility(View.GONE);
                switch (infoBean.getStatus()){
                    case "1":
//                      "待确定";\
                        tv_status.setText("待确定");
                        tv_message.setVisibility(View.VISIBLE);
                        iv_icon.setImageResource(R.mipmap.wddd_dfw);
                        if (order_type.equals("1")){
                            tv_message.setText("若主播在15分钟内未确定订单，订单将会自动取消");
                            ll_sure.setVisibility(View.VISIBLE);
                            tv_agree.setText("拒绝");
                            tv_refuse.setText("同意");
                        }else {
                            tv_start.setVisibility(View.VISIBLE);
                            tv_start.setText("提醒主播");
                        }

                        break;
                    case "2":
//                      "待服务";
                        tv_status.setText("待服务");
                        tv_message.setVisibility(View.VISIBLE);
                        iv_icon.setImageResource(R.mipmap.wddd_dfw);
                        if (order_type.equals("1")){
                            tv_start.setVisibility(View.VISIBLE);
                            tv_start.setText("立即开始");
                            tv_message.setText("主播接单后，有5分钟的准备时间!");
                        }else {
                            tv_message.setText("主播接单后，有5分钟的准备时间!");

                        }
                        break;
                    case "3":
//                      "进行中";
                        tv_status.setText("进行中");
                        tv_message.setVisibility(View.VISIBLE);
                        iv_icon.setImageResource(R.mipmap.wddd_dfw);
                        if (order_type.equals("1")){
                            tv_message.setText("主播需要在5分钟内主动联系客户哦，否则订单会自动取消的哦!");
                            tv_start.setVisibility(View.VISIBLE);
                            tv_start.setText("联系Ta");
                        }else {
                            tv_message.setText("服务已经开始，赶紧联系主播吧!");
                            ll_sure.setVisibility(View.VISIBLE);
                            tv_refuse.setText("联系Ta");
                            tv_agree.setText("完成订单");
                        }

                        break;
                    case "5":
//                     "已完成";
                        tv_status.setText("已完成");
                        iv_icon.setImageResource(R.mipmap.wddd_ywc);
                        break;
                    case "6":
//                     "已取消";
                        tv_status.setText("已取消");
                        iv_icon.setImageResource(R.mipmap.wddd_yqx);
                        break;
                    case "7":
//                      "已拒绝";
                        tv_status.setText("已拒绝");
                        iv_icon.setImageResource(R.mipmap.wddd_yqx);
                        break;
                    case "8":
//                     "已关闭";
                        tv_status.setText("已关闭");
                        break;
                }
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
            case R.id.tv_start:
                //立即开始
                break;
            case R.id.tv_refuse:
                //拒绝
                break;
            case R.id.tv_agree:
                //同意
                break;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }
}