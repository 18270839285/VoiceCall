package huidu.com.voicecall.utils;

import android.os.CountDownTimer;
import android.widget.TextView;

import java.text.SimpleDateFormat;

/* 定义一个倒计时的内部类 */
public class TimeCountUtil extends CountDownTimer {
    TextView tv;

    public TimeCountUtil(long millisInFuture, long countDownInterval, TextView tv) {
        super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        this.tv = tv;
    }

    @Override
    public void onFinish() {// 计时完毕时触发
        tv.setText("重新获取");
        tv.setEnabled(true);
    }

    @Override
    public void onTick(long millisUntilFinished) {// 计时过程显示
        tv.setEnabled(false);
        tv.setText( millisUntilFinished / 1000 + "S");
    }

    public static boolean compareTime(String nowTime, String selectTime) {
        try {

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            java.util.Date now = df.parse(nowTime);
            java.util.Date date = df.parse(selectTime);
            long l = date.getTime() - now.getTime();
            long day = l / (24 * 60 * 60 * 1000);
            long hour = (l / (60 * 60 * 1000) - day * 24);
            long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
            long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
            System.out.println("" + day + "天" + hour + "小时" + min + "分" + s
                    + "秒");

            if (hour <= 0 && min < 30) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {

        }
        return true;
    }

}