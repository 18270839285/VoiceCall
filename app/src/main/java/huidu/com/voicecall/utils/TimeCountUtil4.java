package huidu.com.voicecall.utils;

import android.os.CountDownTimer;
import android.widget.TextView;

/* 定义一个倒计时的内部类 */
public class TimeCountUtil4 extends CountDownTimer {
    TextView tv;
    long time;

    public TimeCountUtil4(long millisInFuture, long countDownInterval, TextView tv) {
        super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        this.tv = tv;
        this.time = millisInFuture;
    }

    @Override
    public void onFinish() {// 计时完毕时触发
//        onTick(0);
//        tv.setText(time / 1000 + "s");
    }

    @Override
    public void onTick(long millisUntilFinished) {// 计时过程显示
        tv.setText(millisUntilFinished / 1000 + "s");
    }
}