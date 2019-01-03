package huidu.com.voicecall.utils;

import android.widget.Toast;

/**
 * 弹出框
 */
public class ToastUtil {
    private static Toast toast = null;

    public static void toastShow(String paramString) {
        if (toast == null) {
            toast = Toast.makeText(AtyContainer.getInstance().currentActivity(), paramString, Toast.LENGTH_SHORT);
            toast.setGravity(17, 0, 0);
        } else {
            toast.setText(paramString);
        }
        toast.show();
    }

}