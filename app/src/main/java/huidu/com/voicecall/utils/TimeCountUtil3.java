package huidu.com.voicecall.utils;

import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;

import java.text.SimpleDateFormat;

/* 定义一个倒计时的内部类 */
public class TimeCountUtil3 extends CountDownTimer {
    TextView tv;
    long time;
    OnFinish onFinish;
    long playTime = 0;
    public TimeCountUtil3(long millisInFuture, long countDownInterval, TextView tv,OnFinish finish) {
        super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        this.tv = tv;
        this.time = millisInFuture;
        this.onFinish = finish;
    }

    @Override
    public void onFinish() {// 计时完毕时触发
//        onTick(0);
        tv.setText((time-playTime) / 1000 + "s");
        onFinish.finish();
    }

    @Override
    public void onTick(long millisUntilFinished) {// 计时过程显示
        Log.e("onTick: ", "millisUntilFinished = "+millisUntilFinished);
        this.playTime = millisUntilFinished;
        tv.setText((time-900) / 1000-millisUntilFinished / 1000 + "s");
    }

    public interface OnFinish{
        void finish();
    }
}