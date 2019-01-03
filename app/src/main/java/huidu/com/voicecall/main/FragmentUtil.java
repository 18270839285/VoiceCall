package huidu.com.voicecall.main;

import android.support.annotation.AnimRes;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.List;


public class FragmentUtil {
    public static void replaceFragment(FragmentManager manager, List<Fragment> list, @IdRes int
            tabIndex, int layoutId, @AnimRes int enter, @AnimRes int exit) {
        FragmentTransaction transaction = manager.beginTransaction();
        //设置碎片显示的自定义动画
        if (enter != 0 && exit != 0) {
            transaction.setCustomAnimations(enter, exit);
        }
        transaction.replace(layoutId, list.get(tabIndex));
        transaction.commit();
    }

    public static void switchFragment(FragmentManager manager, List<Fragment> list, int
            tabIndex, @IdRes int layoutId) {//, @AnimRes int enter, @AnimRes int exit
        FragmentTransaction transaction = manager.beginTransaction();
        //让当前显示的碎片进行隐藏
        int currentTabIndex = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isVisible()) {
                transaction.hide(list.get(i));
            }
        }
        //设置碎片显示的自定义动画
//        if (enter != 0 && exit != 0) {
//            transaction.setCustomAnimations(enter, exit);
//        }
        Fragment toFragment = list.get(tabIndex);
        if (toFragment.isAdded()) {
            transaction.show(toFragment);
        } else {
            transaction.add(layoutId, toFragment);
        }
        transaction.commit();
    }
}
