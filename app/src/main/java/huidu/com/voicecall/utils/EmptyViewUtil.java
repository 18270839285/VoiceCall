package huidu.com.voicecall.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import huidu.com.voicecall.R;


/**
 * Created by admin on 2018/1/8.
 */

public class EmptyViewUtil {

    //    public static View getEmptyView(Context context, String tip) {
//        TextView view = new TextView(context);
//        view.setText(TextUtils.isEmpty(tip) ? "暂无数据" : tip);
//        view.setTextColor(context.getResources().getColor(R.color.color_666666));
//        view.setTextSize(16);
//        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        view.setGravity(Gravity.CENTER);
//        return view;
//
//    }
//    public static View getEmptyView(Context context) {
//        View inflate = LayoutInflater.from(context).inflate(R.layout.empty_view, null, false);
//        return inflate;
//    }
//
    public static View getEmptyView(Context context, int type) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.empty_view, null, false);
        ImageView iv_empty = inflate.findViewById(R.id.iv_empty);
        TextView tv_msg = inflate.findViewById(R.id.tv_message);
        switch (type) {
            case 1://充值记录、提现记录
                Glide.with(context).load(R.mipmap.czjl).into(iv_empty);
                tv_msg.setText("这里什么也没有呢?");
                break;
            case 2://我的订单
                Glide.with(context).load(R.mipmap.wddd).into(iv_empty);
                tv_msg.setText("还没有相关订单呢~");
                break;
            case 3://我的粉丝
                Glide.with(context).load(R.mipmap.wdgz).into(iv_empty);
                tv_msg.setText("还没有粉丝呢~");
                break;
            case 4://我的关注
                Glide.with(context).load(R.mipmap.wdgz).into(iv_empty);
                tv_msg.setText("还没有关注别人哦~");
                break;
            case 5://动态
                Glide.with(context).load(R.mipmap.zwdt).into(iv_empty);
                tv_msg.setText("这里什么也没有呢~");
                break;
            case 6://消息
                Glide.with(context).load(R.mipmap.zwxx).into(iv_empty);
                tv_msg.setText("还没有消息~");
                break;
        }
        return inflate;
    }
//
//
//    public static View getErrorView(Context context, final View.OnClickListener listener) {
//        View view = View.inflate(context, R.layout.error_view, null);
//        TextView tv = (TextView) view.findViewById(R.id.tv_reload);
//        tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (listener != null) {
//                    listener.onClick(view);
//                }
//            }
//        });
//
//        return view;
//
//    }
}
