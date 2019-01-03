package huidu.com.voicecall.main;

import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.multidex.MultiDexApplication;


/**
 * Created by Administrator on 2018/3/7.
 */

public class App extends MultiDexApplication {
    public static Context mContext;
    private static App instance;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mContext = getApplicationContext();
        initConfig();
    }


    private void initConfig() {
        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }

    public static App instance() {
        return instance;
    }

}
