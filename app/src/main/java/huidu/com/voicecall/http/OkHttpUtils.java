package huidu.com.voicecall.http;

/**
 * Description:
 * Data：2018/12/3-11:16
 * Author: lin
 */
public class OkHttpUtils {
    private volatile static OkHttpUtils httpUtils;
    private OkManager okManager = OkManager.getInstance();

    /**
     * 采用单例获取对象
     *
     * @return
     */
    public static OkHttpUtils getInstance() {
        if (httpUtils == null) {
            synchronized (OkHttpUtils.class) {
                if (httpUtils == null) {
                    httpUtils = new OkHttpUtils();
                }
            }
        }
        return httpUtils;
    }

    /**
     * 登陆
     */
//    public void login(Object object, RequestFinish protocol, String telephone, String password) {
//        RequestConfig config = new RequestConfig();
//        config.setCls(SignBean.class);
//        config.setRequestCode(API.SIGN);
//        Map<String, String> data = new HashMap<String, String>();
//        data.put("telephone", telephone);
//        data.put("password", password);
//        okManager.postRequest(object, config, API.BASE_URL+ API.SIGN, data, protocol);
//    }
}
