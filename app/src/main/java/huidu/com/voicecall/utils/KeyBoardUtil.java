package huidu.com.voicecall.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.Timer;
import java.util.TimerTask;

//打开或关闭软键盘
public class KeyBoardUtil {

    //隐藏虚拟键盘
    public static void HideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);

        }
    }

    //显示虚拟键盘
    public static void ShowKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);

    }

    public static void hindKeyBoard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(((Activity) context).getWindow().getDecorView().getWindowToken(), 0);
    }

    public static void showKeyBoard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(((Activity) context).getWindow().getDecorView(), InputMethodManager.SHOW_FORCED);
    }

    //强制显示或者关闭系统键盘
    public static void KeyBoard(final Context context, final String status) {

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager)
                        context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (status.equals("open")) {
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    imm.showSoftInput(((Activity) context).getWindow().getDecorView(), InputMethodManager.SHOW_FORCED);
                } else {
                    // 隐藏软键盘
                    imm.hideSoftInputFromWindow(((Activity) context).getWindow().getDecorView().getWindowToken(), 0);
                }
            }
        }, 100);
    }

    //通过定时器强制隐藏虚拟键盘
    public static void TimerHideKeyboard(final View v) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                }
            }
        }, 10);
    }

    //输入法是否显示着
    public static boolean KeyBoard(EditText edittext) {
        boolean bool = false;
        InputMethodManager imm = (InputMethodManager) edittext.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            bool = true;
        }
        return bool;

    }

    /**
     * 打开软键盘
     */
    public static void openKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 关闭软键盘
     */
    public static void closeKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }
}