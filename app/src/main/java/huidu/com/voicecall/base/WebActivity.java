package huidu.com.voicecall.base;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import huidu.com.voicecall.R;
import huidu.com.voicecall.http.API;

/**
 * 加载网页
 */
public class WebActivity extends BaseActivity {
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.tv_title)
    TextView tv_title;
    String web_url;
    int web_type = 1;

    private static final int WITHDRAWAL0 = 1;//提现规则
    private static final int WITHDRAWAL1 = 2;//注册协议
    private static final int WITHDRAWAL2 = 3;//充值服务协议
    private static final int WITHDRAWAL3 = 4;//隐私策略

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    protected void initView() {
        if (getIntent() != null) {
            web_type = getIntent().getIntExtra("web_type", 1);
        }
        switch (web_type) {
            case WITHDRAWAL0:
                tv_title.setText("提现规则");
                web_url = API.WithdrawalUrl;
                break;
            case WITHDRAWAL1:
                tv_title.setText("注册协议");
                web_url = API.REGISTER_PROTOCOL;
                break;
            case WITHDRAWAL2:
                tv_title.setText("充值服务协议");
                web_url = API.RECHARGE_PROTOCOL;
                break;
            case WITHDRAWAL3:
                tv_title.setText("隐私策略");
                web_url = API.SECRET_PROTOCOL;
                break;
        }
    }

    @Override
    protected void initData() {
        webView.getSettings().setJavaScriptEnabled(true);
        // 设置可以访问文件
        webView.getSettings().setAllowFileAccess(true);
        // 设置支持缩放
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.loadUrl(web_url);
//        initWebViewSetting(web_url);
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void initWebViewSetting(String url) {
        WebSettings settings = webView.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDomStorageEnabled(true);
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setAllowContentAccess(true); // 是否可访问Content Provider的资源，默认值 true
        settings.setAllowFileAccess(true);    // 是否可访问本地文件，默认值 true
        // 是否允许通过file url加载的Javascript读取本地文件，默认值 false
        settings.setAllowFileAccessFromFileURLs(false);
        // 是否允许通过file url加载的Javascript读取全部资源(包括文件,http,https)，默认值 false
        settings.setAllowUniversalAccessFromFileURLs(false);
        //开启JavaScript支持
        settings.setJavaScriptEnabled(true);
        // 支持缩放
        settings.setSupportZoom(true);
//        //辅助WebView设置处理关于页面跳转，页面请求等操作
//        webView.setWebViewClient(new MyWebViewClient());
//        //辅助WebView处理图片上传操作
//        webView.setWebChromeClient(new MyChromeWebClient());

        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        //加载地址
        webView.loadUrl(url);
    }

    @OnClick({R.id.iv_back})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }
}
