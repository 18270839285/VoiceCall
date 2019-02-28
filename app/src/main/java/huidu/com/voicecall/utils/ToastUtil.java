package huidu.com.voicecall.utils;

import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import huidu.com.voicecall.R;
import huidu.com.voicecall.base.BaseActivity;

/**
 * 弹出框
 */
public class ToastUtil {
    private static Toast toast = null;
    private static Toast toast2 = null;

    public static void toastShow(String paramString) {
        if (toast == null) {
            toast = Toast.makeText(AtyContainer.getInstance().currentActivity(), paramString, Toast.LENGTH_SHORT);
            toast.setGravity(17, 0, 0);
        } else {
            toast.setText(paramString);
        }
        toast.show();
    }

    public static void toastBottom(Activity context, String paramString) {
        Display display = context.getWindowManager().getDefaultDisplay();
        int height = display.getHeight();
//        Toast  toast = Toast.makeText(AtyContainer.getInstance().currentActivity(), paramString, Toast.LENGTH_SHORT);
        if (toast2 == null) {
            toast2 = Toast.makeText(AtyContainer.getInstance().currentActivity(), paramString, Toast.LENGTH_SHORT);
//            toast2.setGravity(Gravity.BOTTOM, 0, 0);
            toast2.setGravity(Gravity.TOP, 0, height * 3 / 4);
        } else {
            toast2.setText(paramString);
        }
        toast2.show();
    }

    public static void recordToast(BaseActivity context) {
        LayoutInflater inflater = context.getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_record,
                null);
        //(ViewGroup) context.findViewById(R.id.ll_Toast));
        ImageView image = (ImageView) layout.findViewById(R.id.iv_icon);
//        image.setImageResource(R.mipmap.zzly);
        TextView tv_time_task = (TextView) layout.findViewById(R.id.tv_time_task);
        tv_time_task.setText("倒计时");
        final Toast toast1 = new Toast(context);
        toast1.setGravity(Gravity.CENTER,0,0);
        toast1.setDuration(Toast.LENGTH_LONG);
        toast1.setView(layout);
        toast1.show();
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toast1.show();
            }
        }, 0, 100);// 3000表示点击按钮之后，Toast延迟3000ms后显示
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toast1.cancel();
                timer.cancel();
            }
        }, 15000);// 5000表示Toast显示时间为5秒
    }
}