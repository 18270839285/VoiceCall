package huidu.com.voicecall.utils;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import huidu.com.voicecall.R;


/**
 * 作者： chengwenchi
 * 创建于： 2017/7/27
 */

public class WLToast {
    /**
     * 获取当前Android系统版本
     */
    static int currentapiVersion = android.os.Build.VERSION.SDK_INT;
    /**
     * 离场动画持续时间
     */
    private static final int TIME_END_ANIM = 15000;
    /**
     * UI线程句柄
     */
    Handler mHandler;
    /**
     * 内容对象
     */
    Context mContext;

    /**
     * 顶层布局
     */
    LinearLayout mTopView;
    /**
     * 布局属性
     */
    LayoutParams lp_MM;
    /**
     * 反射过程中是否出现异常的标志
     */
    boolean hasReflectException = false;

    /**
     * 单例
     */
    private static WLToast instance;

//    private TextView tvContext;

    /**
     * 获得单例
     *
     * @param context
     * @return
     */
    public static WLToast getInstance(Context context) {
        if (instance == null) {
            instance = new WLToast(context);
        }
        return instance;
    }

    private WLToast(Context context) {
        if (context == null|| context.getApplicationContext() == null) {
            throw new NullPointerException("context can't be null");
        }
        mContext = context.getApplicationContext();
        initView();
        initTN();

    }


    /**
     * 初始化视图控件
     */
    public void initView() {
        mHandler = new Handler(mContext.getMainLooper());
        View view = LayoutInflater.from(mContext).inflate(R.layout.toast_record, null);
//        tvContext = (TextView) view.findViewById(R.id.wl_toast_context);
        lp_MM = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mTopView = new LinearLayout(mContext);
        mTopView.setLayoutParams(lp_MM);
        mTopView.setOrientation(LinearLayout.VERTICAL);
        mTopView.setGravity(Gravity.TOP);
        mTopView.addView(view);

    }

    public final void show(String msg) {
//        tvContext.setText(msg);
        showToast();
        hide();
    }


    public final void hide() {
        //动画结束后移除控件
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideToast();
            }
        }, TIME_END_ANIM);
    }

    /* 以下为反射相关内容 */
    Toast mToast;
    Field mTN;
    Object mObj;
    Method showMethod, hideMethod;

    /**
     * 通过反射获得mTN下的show和hide方法
     */
    private void initTN() {
        mToast = new Toast(mContext);
        mToast.setView(mTopView);
        Class<Toast> clazz = Toast.class;
        try {
            mTN = clazz.getDeclaredField("mTN");
            mTN.setAccessible(true);
            mObj = mTN.get(mToast);
            showMethod = mObj.getClass().getDeclaredMethod("show", new Class<?>[0]);
            hideMethod = mObj.getClass().getDeclaredMethod("hide", new Class<?>[0]);
            hasReflectException = false;
        } catch (NoSuchFieldException e) {
            hasReflectException = true;
            System.out.println(e.getMessage());
        } catch (IllegalAccessException e) {
            hasReflectException = true;
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            hasReflectException = true;
            System.out.println(e.getMessage());
        } catch (NoSuchMethodException e) {
            hasReflectException = true;
            System.out.println(e.getMessage());
        }
    }

    /**
     * 通过反射获得的show方法显示指定View
     */
    private void showToast() {
        try {
            //高版本需要再次手动设置mNextView属性，2系列版本不需要
            if (currentapiVersion > 10) {
                Field mNextView = mObj.getClass().getDeclaredField("mNextView");
                mNextView.setAccessible(true);
                mNextView.set(mObj, mTopView);
            }
            if (showMethod != null) {
                showMethod.invoke(mObj, new Object[0]);
            } else {
                mToast.setDuration(Toast.LENGTH_LONG);
                mToast.show();
            }

            hasReflectException = false;
        } catch (Exception e) {
            hasReflectException = true;
            System.out.println(e.getMessage());
        }
    }

    /**
     * 通过反射获得的hide方法隐藏指定View
     */
    public void hideToast() {
        try {
            if (mToast != null) {
                mToast.cancel();
            }
            hasReflectException = false;
        } catch (Exception e) {
            hasReflectException = true;
            System.out.println(e.getMessage());
        }
    }

    public void removeAll() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }


    /**
     * 是否是三星手机
     *
     * @return
     */
    private boolean isSamSung() {
        String brand = android.os.Build.BRAND;
        if (!TextUtils.isEmpty(brand)) {
            if (brand.equalsIgnoreCase("samsung")) {//适配三星手机且版本为7.0以上的系统
                return true;
            }
        }
        return false;
    }
}
