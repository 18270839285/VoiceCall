package huidu.com.voicecall.utils;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import huidu.com.voicecall.R;

/**
 * Description:自定义Toast - 正确提示
 * Data：2019/2/27-14:10
 * Author: lin
 */

public class ToastRecording {
    private boolean canceled = true;
    private Handler handler;
    private Toast toast;
    private TimeCount time;
//    private TextView toast_content;

    private static ToastRecording instance;

    public static ToastRecording getInstance(Context context) {
        if (instance == null) {
            instance = new ToastRecording(context);
        }

        return instance;
    }

    public ToastRecording(Context context) {
        this(context, new Handler());
    }

    public ToastRecording(Context context, Handler handler) {
        this.handler = handler;

        View layout = LayoutInflater.from(context).inflate(R.layout.toast_record, null, false);
//        toast_content = (TextView) layout.findViewById(R.id.tv_time_task);
        if (toast == null) {
            toast = new Toast(context);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
    }

    /**
//     * @param text     要显示的内容
     * @param duration 显示的时间长
     *                 根据LENGTH_MAX进行判断
     *                 如果不匹配，进行系统显示
     *                 如果匹配，永久显示，直到调用hide()
     */
    public void show( int duration) {
        time = new TimeCount(duration, 1000);//1000是消失渐变时间
//        toast_content.setText(text);
        if (canceled) {
            time.start();
            canceled = false;
            showUntilCancel();
        }
    }

    /**
     * 隐藏Toast
     */
    public void hide() {
        if (toast != null) {
            toast.cancel();
        }
        canceled = true;
    }

    private void showUntilCancel() {
        if (canceled) {
            return;
        }
        toast.show();
        handler.postDelayed(new Runnable() {
            public void run() {
                showUntilCancel();
            }
        }, 3000);
    }

    /**
     * 计时器
     */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval); // 总时长,计时的时间间隔
        }

        @Override
        public void onFinish() { // 计时完毕时触发
            hide();
        }

        @Override
        public void onTick(long millisUntilFinished) { // 计时过程显示
        }

    }

}
 
