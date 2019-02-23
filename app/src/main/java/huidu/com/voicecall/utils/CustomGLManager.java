package huidu.com.voicecall.utils;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;

/**
 * Description:
 * Data：2019/2/23-10:05
 * Author: lin
 */
public class CustomGLManager extends GridLayoutManager {
    private boolean isScrollEnabled = true;

    public CustomGLManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public CustomGLManager(Context context, int spanCount) {
        super(context, spanCount);
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