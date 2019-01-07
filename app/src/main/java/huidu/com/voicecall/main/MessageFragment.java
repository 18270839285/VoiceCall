package huidu.com.voicecall.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import huidu.com.voicecall.R;
import huidu.com.voicecall.base.BaseFragment;

/**
 * Description:
 * Data：2019/1/3-11:29
 * Author: lin
 */
public class MessageFragment extends BaseFragment {
    @BindView(R.id.webView)
    WebView webView;

    Unbinder unbinder;
    private String TestUrl = "http://support.51qdd.net/h5voice/index.html";
//    private String TestUrl = "https://www.zhibohome.net/ShopOfficial/index.html";
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    protected void initView(View view) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(TestUrl);
    }

    @Override
    protected void initData() {

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
