package huidu.com.voicecall.base;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import huidu.com.voicecall.utils.Loading;

/**
 * Created by jason on 2017/3/15.
 */

public abstract class BaseFragment extends Fragment {

    public final String TAG = getClass().getName() + "";
    Unbinder bind;
    View view;
    public Context mContext;
    //进度条
    private Dialog mDialog;
    public Loading mLoading;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: " + TAG);
        mContext = getActivity();
        mLoading = new Loading(mContext);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(getLayoutId(), null);
        bind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView(view);
        initData();
    }

    protected abstract int getLayoutId();

    protected abstract void initView(View view);

    protected abstract void initData();

    /**
     * jump to target activity without bundle data
     *
     * @param cls
     */
    public void jump(Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), cls);
        startActivity(intent);
    }

    /**
     * with data to trans
     *
     * @param targetClz
     * @param data
     */
    public void jump(Class<?> targetClz, Bundle data) {
        startActivity(new Intent(getActivity(), targetClz).putExtras(data));
    }

    public void jump(Class<?> cls, int type) {
        Intent intent = new Intent();
        intent.putExtra("jump", type);
        intent.setClass(getActivity(), cls);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        Log.e(TAG, "onDestroyView: " + TAG);
        bind.unbind();
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void finishLoad(){
        if (mLoading!=null&&mLoading.isShowing()){
            mLoading.dismiss();
        }
    }

}
