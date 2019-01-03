package huidu.com.voicecall.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zzj on 2017/9/12.
 */

public class SPUtils {


    /**
     * 向SharedPreferences中写入int类型数据
     *
     * @param context 上下文环境
     * @param name 对应的xml文件名称
     * @param key 键
     * @param value 值
     */
    public static final String SPREADER = "jiazhou";


    public SPUtils() {
        super();


    }

    public static void putValue(Context ctx, String key,
                                int value) {
        SharedPreferences.Editor sp = getEditor(ctx);
        sp.putInt(key, value);
        sp.commit();
    }


    public static void putValue(Context ctx, String key,
                                boolean value) {
        SharedPreferences.Editor sp = getEditor(ctx);
        sp.putBoolean(key, value);
        sp.commit();
    }


    public static void putValue(Context ctx, String key,
                                String value) {
        SharedPreferences.Editor sp = getEditor(ctx);
        sp.putString(key, value);
        sp.commit();
    }


    public static void putValue(Context ctx, String key,
                                float value) {
        SharedPreferences.Editor sp = getEditor(ctx);
        sp.putFloat(key, value);
        sp.commit();
    }


    public static void putValue(Context ctx, String key,
                                long value) {
        SharedPreferences.Editor sp = getEditor(ctx);
        sp.putLong(key, value);
        sp.commit();
    }


    public static int getValue(Context ctx, String key,
                               int defValue) {
        SharedPreferences sp = ctx.getSharedPreferences(SPREADER, Context.MODE_PRIVATE);
        int value = sp.getInt(key, defValue);
        return value;
    }


    public static boolean getValue(Context ctx, String key,
                                   boolean defValue) {
        SharedPreferences sp = ctx.getSharedPreferences(SPREADER, Context.MODE_PRIVATE);
        boolean value = sp.getBoolean(key, defValue);
        return value;
    }


    public static String getValue(Context ctx, String key,
                                  String defValue) {
        SharedPreferences sp = ctx.getSharedPreferences(SPREADER, Context.MODE_PRIVATE);
        String value = sp.getString(key, defValue);
        return value;
    }
    public static String getValue(Context ctx, String key) {
        SharedPreferences sp = ctx.getSharedPreferences(SPREADER, Context.MODE_PRIVATE);
        String value = sp.getString(key, "");
        return value;
    }


    public static float getValue(Context ctx, String key,
                                 float defValue) {
        SharedPreferences sp = ctx.getSharedPreferences(SPREADER, Context.MODE_PRIVATE);
        float value = sp.getFloat(key, defValue);
        return value;
    }


    public static long getValue(Context ctx, String key,
                                long defValue) {
        SharedPreferences sp = ctx.getSharedPreferences(SPREADER, Context.MODE_PRIVATE);
        long value = sp.getLong(key, defValue);
        return value;
    }

    //获取Editor实例
    private static SharedPreferences.Editor getEditor(Context ctx) {
        return ctx.getSharedPreferences(SPREADER, Context.MODE_PRIVATE).edit();
    }


}
