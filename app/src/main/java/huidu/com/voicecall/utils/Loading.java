package huidu.com.voicecall.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import huidu.com.voicecall.R;

/**
 * Description:全局加载框
 * Data：2019/1/3-16:41
 * Author: lin
 */
public  class Loading extends Dialog {
//    public abstract void cancel();

    public Loading(Context context) {
        super(context, R.style.Loading);
        // 加载布局
        setContentView(R.layout.view_loading);
        // 设置Dialog参数
        setCanceledOnTouchOutside(false);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
    }

//    // TODO 封装Dialog消失的回调
//    @Override
//    public void onBackPressed() {
//        // 回调
//        cancel();
//        // 关闭Loading
//        dismiss();
//    }

}
