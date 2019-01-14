package huidu.com.voicecall.http;

import java.util.HashMap;
import java.util.Map;

import huidu.com.voicecall.bean.AnchorInfo;
import huidu.com.voicecall.bean.AnchorPrice;
import huidu.com.voicecall.bean.CoinLog;
import huidu.com.voicecall.bean.Home;
import huidu.com.voicecall.bean.OrderInfo;
import huidu.com.voicecall.bean.OrderList;
import huidu.com.voicecall.bean.PackageRecharge;
import huidu.com.voicecall.bean.SignBean;
import huidu.com.voicecall.bean.SpareBean;
import huidu.com.voicecall.bean.UserAccount;
import huidu.com.voicecall.bean.UserAttention;
import huidu.com.voicecall.bean.UserFans;
import huidu.com.voicecall.bean.UserInfo;
import huidu.com.voicecall.bean.UserMyAccount2;
import huidu.com.voicecall.bean.WithdrawalLog;

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
     * 图片上传
     */
    public void common_image_upload(String image, RequestFinish protocol) {
        RequestConfig config = new RequestConfig();
        config.setCls(SpareBean.class);
        config.setRequestCode(API.COMMON_IMAGE_UPLOAD);
        Map<String, String> data = new HashMap<String, String>();
        data.put("image", image);
        data.put("ext", "jpg");
        okManager.postRequest(config, API.BASE_URL + API.COMMON_IMAGE_UPLOAD, data, protocol);
    }
    /**
     * 登录
     */
    public void login(String telephone, String password, RequestFinish protocol) {
        RequestConfig config = new RequestConfig();
        config.setCls(SignBean.class);
        config.setRequestCode(API.SIGN);
        Map<String, String> data = new HashMap<String, String>();
        data.put("telephone", telephone);
        data.put("password", password);
        okManager.postRequest(config, API.BASE_URL + API.SIGN, data, protocol);
    }

    /**
     * 获取验证码
     */
    public void sign_get_verify(String telephone, int type, RequestFinish protocol) {
        RequestConfig config = new RequestConfig();
        config.setCls(Object.class);
        config.setRequestCode(API.SIGN_GET_VERIFY);
        Map<String, String> data = new HashMap<String, String>();
        data.put("telephone", telephone);
        if (type == 0)
            data.put("type", "register");
        else
            data.put("type", "forget");
        okManager.postRequest(config, API.BASE_URL + API.SIGN_GET_VERIFY, data, protocol);
    }

    /**
     * 注册
     */
    public void sign_register(String telephone, String verify_code, String password, RequestFinish protocol) {
        RequestConfig config = new RequestConfig();
        config.setCls(SignBean.class);
        config.setRequestCode(API.SIGN_REGISTER);
        Map<String, String> data = new HashMap<String, String>();
        data.put("telephone", telephone);
        data.put("verify_code", verify_code);
        data.put("password", password);
        data.put("create_terminal", "3");
        okManager.postRequest(config, API.BASE_URL + API.SIGN_REGISTER, data, protocol);
    }

    /**
     * 忘记密码
     */
    public void sign_forget(String telephone, String verify_code, String password, String repet_password, RequestFinish protocol) {
        RequestConfig config = new RequestConfig();
        config.setCls(SignBean.class);
        config.setRequestCode(API.SIGN_FORGET);
        Map<String, String> data = new HashMap<String, String>();
        data.put("telephone", telephone);
        data.put("verify_code", verify_code);
        data.put("password", password);
        data.put("repet_password", repet_password);
        okManager.postRequest(config, API.BASE_URL + API.SIGN_FORGET, data, protocol);
    }
    /**
     * 忘记密码
     */
    public void change(String token, String password, String new_password, String repet_password, RequestFinish protocol) {
        RequestConfig config = new RequestConfig();
        config.setCls(SignBean.class);
        config.setRequestCode(API.CHANGE);
        Map<String, String> data = new HashMap<String, String>();
        data.put("token", token);
        data.put("password", password);
        data.put("new_password", new_password);
        data.put("repet_password", repet_password);
        okManager.postRequest(config, API.BASE_URL + API.CHANGE, data, protocol);
    }

    /**
     * 验证验证码
     */
    public void sign_verify_code(String telephone, String verify_code, int type, RequestFinish protocol) {
        RequestConfig config = new RequestConfig();
        config.setCls(SignBean.class);
        config.setRequestCode(API.SIGN_VERIFY_CODE);
        Map<String, String> data = new HashMap<String, String>();
        data.put("telephone", telephone);
        data.put("verify_code", verify_code);
        if (type == 0)
            data.put("type", "register");
        else
            data.put("type", "forget");
        okManager.postRequest(config, API.BASE_URL + API.SIGN_VERIFY_CODE, data, protocol);
    }

    /**
     * 个人资料
     */
    public void user_info(String token, String user_id, RequestFinish protocol) {
        RequestConfig config = new RequestConfig();
        config.setCls(UserInfo.class);
        config.setRequestCode(API.USER_INFO);
        Map<String, String> data = new HashMap<String, String>();
        data.put("token", token);
        data.put("user_id", user_id);
        okManager.postRequest(config, API.BASE_URL + API.USER_INFO, data, protocol);
    }

    /**
     * 个人资料 — 修改
     */
    public void user_info_edit(String token, String head_image,String nickname,String sex,
                               String birthday,String zodiac,String introduce,RequestFinish protocol) {
        RequestConfig config = new RequestConfig();
        config.setCls(Object.class);
        config.setRequestCode(API.USER_INFO_EDIT);
        Map<String, String> data = new HashMap<String, String>();
        data.put("token", token);
        data.put("head_image", head_image);
        data.put("nickname", nickname);
        data.put("sex", sex);
        data.put("birthday", birthday);
        data.put("zodiac", zodiac);
        data.put("introduce", introduce);
        okManager.postRequest(config, API.BASE_URL + API.USER_INFO_EDIT, data, protocol);
    }

    /**
     * 粉丝列表
     */
    public void user_fans_list(String token, RequestFinish protocol) {
        RequestConfig config = new RequestConfig();
        config.setElement(UserFans.class);
        config.setRequestCode(API.USER_FANS_LIST);
        Map<String, String> data = new HashMap<String, String>();
        data.put("token", token);
        okManager.postRequest(config, API.BASE_URL + API.USER_FANS_LIST, data, protocol);
    }

    /**
     * 个人资料 — 修改
     */
    public void user_attention_list(String token, RequestFinish protocol) {
        RequestConfig config = new RequestConfig();
        config.setElement(UserAttention.class);
        config.setRequestCode(API.USER_ATTENTION_LIST);
        Map<String, String> data = new HashMap<String, String>();
        data.put("token", token);
        okManager.postRequest(config, API.BASE_URL + API.USER_ATTENTION_LIST, data, protocol);
    }

    /**
     * 我的财富
     */
    public void user_myaccount2(String token, RequestFinish protocol) {
        RequestConfig config = new RequestConfig();
        config.setCls(UserMyAccount2.class);
        config.setRequestCode(API.USER_MYACCOUNT2);
        Map<String, String> data = new HashMap<String, String>();
        data.put("token", token);
        okManager.postRequest(config, API.BASE_URL + API.USER_MYACCOUNT2, data, protocol);
    }

    /**
     * 提现
     */
    public void order_withdrawal(String token, String money, String coin, String receive_method, String receive_account, RequestFinish protocol) {
        RequestConfig config = new RequestConfig();
        config.setCls(Object.class);
        config.setRequestCode(API.ORDER_WITHDRAWAL);
        Map<String, String> data = new HashMap<String, String>();
        data.put("token", token);
        data.put("money", money);
        data.put("coin", coin);
        data.put("receive_method", receive_method);
        data.put("receive_account", receive_account);
        okManager.postRequest(config, API.BASE_URL + API.ORDER_WITHDRAWAL, data, protocol);
    }

    /**
     * 提现记录
     */
    public void order_withdrawal_log(String token, RequestFinish protocol) {
        RequestConfig config = new RequestConfig();
        config.setCls(WithdrawalLog.class);
        config.setRequestCode(API.ORDER_WITHDRAWAL_LOG);
        Map<String, String> data = new HashMap<String, String>();
        data.put("token", token);
        okManager.postRequest(config, API.BASE_URL + API.ORDER_WITHDRAWAL_LOG, data, protocol);
    }

    /**
     * 我的账户
     */
    public void user_account(String token, RequestFinish protocol) {
        RequestConfig config = new RequestConfig();
        config.setCls(UserAccount.class);
        config.setRequestCode(API.USER_ACCOUNT);
        Map<String, String> data = new HashMap<String, String>();
        data.put("token", token);
        okManager.postRequest(config, API.BASE_URL + API.USER_ACCOUNT, data, protocol);
    }
    /**
     * 充值套餐列表
     */
    public void package_recharge(String token, RequestFinish protocol) {
        RequestConfig config = new RequestConfig();
        config.setCls(PackageRecharge.class);
        config.setRequestCode(API.PACKAGE_RECHARGE);
        Map<String, String> data = new HashMap<String, String>();
        data.put("token", token);
        data.put("sys", "1");
        okManager.postRequest(config, API.BASE_URL + API.PACKAGE_RECHARGE, data, protocol);
    }
    /**
     * 虚拟币1 套餐充值
     */
    public void order_recharge(String token,String package_id,String pay_method,String is_sale, RequestFinish protocol) {
        RequestConfig config = new RequestConfig();
        config.setCls(SpareBean.class);
        config.setRequestCode(API.ORDER_RECHARGE);
        Map<String, String> data = new HashMap<String, String>();
        data.put("token", token);
        data.put("package_id", package_id);
        data.put("pay_method", pay_method);
        data.put("is_sale", is_sale);
        data.put("sys", "1");
        okManager.postRequest(config, API.BASE_URL + API.ORDER_RECHARGE, data, protocol);
    }
    /**
     * 虚拟币1 消费记录
     */
    public void order_coin1_log(String token, RequestFinish protocol) {
        RequestConfig config = new RequestConfig();
        config.setCls(CoinLog.class);
        config.setRequestCode(API.ORDER_COIN1_LOG);
        Map<String, String> data = new HashMap<String, String>();
        data.put("token", token);
        okManager.postRequest(config, API.BASE_URL + API.ORDER_COIN1_LOG, data, protocol);
    }

    /**
     * 首页
     */
    public void home(String token,String type,String page, RequestFinish protocol) {
        RequestConfig config = new RequestConfig();
        config.setCls(Home.class);
        config.setRequestCode(API.HOME);
        Map<String, String> data = new HashMap<String, String>();
        data.put("token", token);
        data.put("sys", "1");
        if (!type.isEmpty())
            data.put("type", type);
        if (!page.isEmpty())
            data.put("page", page);
        okManager.postRequest(config, API.BASE_URL + API.HOME, data, protocol);
    }
    /**
     * 主播资料
     */
    public void anchor_info(String token,String anchor_id,String anchor_type_id, RequestFinish protocol) {
        RequestConfig config = new RequestConfig();
        config.setCls(AnchorInfo.class);
        config.setRequestCode(API.ANCHOR_INFO);
        Map<String, String> data = new HashMap<String, String>();
        data.put("token", token);
        data.put("anchor_id", anchor_id);
        data.put("anchor_type_id", anchor_type_id);
        okManager.postRequest(config, API.BASE_URL + API.ANCHOR_INFO, data, protocol);
    }
    /**
     * 主播价格
     */
    public void anchor_price(String token,String anchor_id,String anchor_type_id, RequestFinish protocol) {
        RequestConfig config = new RequestConfig();
        config.setCls(AnchorPrice.class);
        config.setRequestCode(API.ANCHOR_PRICE);
        Map<String, String> data = new HashMap<String, String>();
        data.put("token", token);
        data.put("anchor_id", anchor_id);
        data.put("anchor_type_id", anchor_type_id);
        okManager.postRequest(config, API.BASE_URL + API.ANCHOR_PRICE, data, protocol);
    }
    /**
     * 语音下单
     */
    public void order_voice(String token,String anchor_id,String anchor_type_id,String times,String num, RequestFinish protocol) {
        RequestConfig config = new RequestConfig();
        config.setCls(SpareBean.class);
        config.setRequestCode(API.ORDER_VOICE);
        Map<String, String> data = new HashMap<String, String>();
        data.put("token", token);
        data.put("anchor_id", anchor_id);
        data.put("anchor_type_id", anchor_type_id);
        data.put("times", times);
        data.put("num", num);
        okManager.postRequest(config, API.BASE_URL + API.ORDER_VOICE, data, protocol);
    }
    /**
     * 关注
     */
    public void user_attention(String token,String anchor_id, RequestFinish protocol) {
        RequestConfig config = new RequestConfig();
        config.setCls(Object.class);
        config.setRequestCode(API.USER_ATTENTION);
        Map<String, String> data = new HashMap<String, String>();
        data.put("token", token);
        data.put("anchor_id", anchor_id);
        okManager.postRequest(config, API.BASE_URL + API.USER_ATTENTION, data, protocol);
    }
    /**
     * 取关
     */
    public void user_attention_cannel(String token,String anchor_id, RequestFinish protocol) {
        RequestConfig config = new RequestConfig();
        config.setCls(Object.class);
        config.setRequestCode(API.USER_ATTENTION_CANNEL);
        Map<String, String> data = new HashMap<String, String>();
        data.put("token", token);
        data.put("anchor_id", anchor_id);
        okManager.postRequest(config, API.BASE_URL + API.USER_ATTENTION_CANNEL, data, protocol);
    }


    /**
     * 订单列表
     */
    public void order_list(String token,String is_receive, RequestFinish protocol) {
        RequestConfig config = new RequestConfig();
        config.setCls(OrderList.class);
        config.setRequestCode(API.ORDER_LIST);
        Map<String, String> data = new HashMap<String, String>();
        data.put("token", token);
        data.put("is_receive", is_receive);
        okManager.postRequest(config, API.BASE_URL + API.ORDER_LIST, data, protocol);
    }
    /**
     * 订单详情
     */
    public void order_info(String token,String order_no, RequestFinish protocol) {
        RequestConfig config = new RequestConfig();
        config.setCls(OrderInfo.class);
        config.setRequestCode(API.ORDER_INFO);
        Map<String, String> data = new HashMap<String, String>();
        data.put("token", token);
        data.put("order_no", order_no);
        okManager.postRequest(config, API.BASE_URL + API.ORDER_INFO, data, protocol);
    }
    /**
     * 取消订单
     */
    public void order_cannel(String token,String order_no, RequestFinish protocol) {
        RequestConfig config = new RequestConfig();
        config.setCls(Object.class);
        config.setRequestCode(API.ORDER_CANNEL);
        Map<String, String> data = new HashMap<String, String>();
        data.put("token", token);
        data.put("order_no", order_no);
        okManager.postRequest(config, API.BASE_URL + API.ORDER_CANNEL, data, protocol);
    }
    /**
     * 订单完成
     */
    public void order_finish(String token,String order_no, RequestFinish protocol) {
        RequestConfig config = new RequestConfig();
        config.setCls(Object.class);
        config.setRequestCode(API.ORDER_FINISH);
        Map<String, String> data = new HashMap<String, String>();
        data.put("token", token);
        data.put("order_no", order_no);
        okManager.postRequest(config, API.BASE_URL + API.ORDER_FINISH, data, protocol);
    }
    /**
     * 拒绝订单
     */
    public void order_refuse(String token,String order_no, RequestFinish protocol) {
        RequestConfig config = new RequestConfig();
        config.setCls(Object.class);
        config.setRequestCode(API.ORDER_REFUSE);
        Map<String, String> data = new HashMap<String, String>();
        data.put("token", token);
        data.put("order_no", order_no);
        okManager.postRequest(config, API.BASE_URL + API.ORDER_REFUSE, data, protocol);
    }
    /**
     * 主播接单
     */
    public void order_receivei(String token,String order_no, RequestFinish protocol) {
        RequestConfig config = new RequestConfig();
        config.setCls(Object.class);
        config.setRequestCode(API.ORDER_RECEIVEI);
        Map<String, String> data = new HashMap<String, String>();
        data.put("token", token);
        data.put("order_no", order_no);
        okManager.postRequest(config, API.BASE_URL + API.ORDER_RECEIVEI, data, protocol);
    }
    /**
     * 通话开始
     */
    public void order_begin(String token,String order_no,String begin_man, RequestFinish protocol) {
        RequestConfig config = new RequestConfig();
        config.setCls(Object.class);
        config.setRequestCode(API.ORDER_BEGIN);
        Map<String, String> data = new HashMap<String, String>();
        data.put("token", token);
        data.put("order_no", order_no);
        data.put("begin_man", begin_man);
        okManager.postRequest(config, API.BASE_URL + API.ORDER_BEGIN, data, protocol);
    }
    /**
     * 通话开始
     */
    public void order_over(String token,String order_no,String record_id,String over_man, RequestFinish protocol) {
        RequestConfig config = new RequestConfig();
        config.setCls(Object.class);
        config.setRequestCode(API.ORDER_OVER);
        Map<String, String> data = new HashMap<String, String>();
        data.put("token", token);
        data.put("order_no", order_no);
        data.put("record_id", record_id);
        data.put("over_man", over_man);
        okManager.postRequest(config, API.BASE_URL + API.ORDER_OVER, data, protocol);
    }
}
