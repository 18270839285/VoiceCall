package huidu.com.voicecall.utils;

import java.math.BigDecimal;

/**
 * 同时点击多次判断
 */

public class PointUtils {

    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private static long lastClickTime;

    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }

    public static boolean isFastClick1() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= 500) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }
    /**
     * point保留几位就是几    1wei 1 二位 2
     *
     * @return
     */
    public static Double KeepPoint1(Double number, int point) {
        BigDecimal bg = new BigDecimal(number);
        double f1 = bg.setScale(point, BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1;
    }
}
