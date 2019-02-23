package huidu.com.voicecall.utils;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Description:
 * Data：2019/2/23-10:05
 * Author: lin
 */
public class CustomLLManager extends LinearLayoutManager {
    private boolean isScrollEnabled = true;

    public CustomLLManager(Context context) {
        super(context);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically();
    }
}