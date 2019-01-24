package huidu.com.voicecall.utils;

import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.widget.Toast;

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

}