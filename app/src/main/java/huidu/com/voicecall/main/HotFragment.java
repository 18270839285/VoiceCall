package huidu.com.voicecall.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import huidu.com.voicecall.R;
import huidu.com.voicecall.base.BaseFragment;
import huidu.com.voicecall.utils.Loading;

/**
 * Description:
 * Data：2019/1/3-16:12
 * Author: lin
 */
public class HotFragment extends BaseFragment {
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.textView1)
    TextView textView1;
    @BindView(R.id.textView2)
    TextView textView2;
    Unbinder unbinder;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_hot;
    }

    Loading mLoading;

    @Override
    protected void initView(View view) {
        mLoading = new Loading(getActivity());
        String string = getArguments().getString("type");
        textView.setText(string);
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLoading != null)
                    mLoading.show();
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLoading != null && mLoading.isShowing())
                    mLoading.dismiss();
            }
        });
    }

    /**
     * fragment静态传值
     */
    public static HotFragment newInstance(String str) {
        HotFragment fragment = new HotFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", str);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initData() {
        // 添加Loading

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
