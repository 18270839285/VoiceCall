package huidu.com.voicecall.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Description:
 * Data：2019/3/5-9:37
 * Author: lin
 */
public class RefreshReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (listener != null)
            listener.receive(context, intent);
    }

    private ActionListener listener;

    public void setListener(ActionListener l) {
        listener = l;
    }

    public interface ActionListener {
        void receive(Context context, Intent intent);
    }
}
