package huidu.com.voicecall.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
//    public static View getEmptyView(Context context, int type) {
//        View inflate = LayoutInflater.from(context).inflate(R.layout.custome_empty_view, null, false);
//        ImageView iv_empty = inflate.findViewById(R.id.iv_empty);
//        TextView tv_msg = inflate.findViewById(R.id.tv_msg);
//        switch (type) {
//            case 1://系统消息
//                Glide.with(context).load(R.drawable.pingtai_kongbaiye).into(iv_empty);
//                tv_msg.setText("平台还没有发布任何公告哦");
//                break;
//            case 2://平台公告
//                Glide.with(context).load(R.drawable.xitong_konmgbaiye).into(iv_empty);
//                tv_msg.setText("暂时还没有最新消息哦");
//                break;
//            case 3://商品搜索
//                Glide.with(context).load(R.drawable.ss_zwsp).into(iv_empty);
//                tv_msg.setText("抱歉，没有找到相关的宝贝~");
//                break;
//            case 4://账单管理
//                Glide.with(context).load(R.drawable.zdgl_zwzd).into(iv_empty);
//                tv_msg.setText("你还没有账单哦!");
//                break;
//        }
//        return inflate;
//    }
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
