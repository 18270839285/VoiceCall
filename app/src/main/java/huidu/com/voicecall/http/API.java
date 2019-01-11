package huidu.com.voicecall.http;

/**
 * Created ： LiLin
 * Date ： 2017/10/11
 * Function ：接口列表
 */
public interface API {
    String HEADER = "Content-Type";
    String HEADER_VALUE = "application/x-www-form-urlencoded";
    /**
     * 正式地址
     */
//    String BASE_URL = "http://newshop.51qdd.net/api/";
    /**
     * 测试地址
     */
    String BASE_URL = "http://voice.51qdd.net/api/";
    String TOKEN_TEST = "UfCQ19B0Y9XQX1Q0m8nDxbCyx1w0XnY000BY1cy013eCC3tq";


    String USER_INFO = "user/info";//个人资料
    String USER_INFO_EDIT = "user/info/edit";//个人资料— 修改
    String USER_FANS_LIST = "user/fans/list";//粉丝列表
    String USER_ATTENTION_LIST = "user/attention/list";//关注列表
    String USER_MYACCOUNT2 = "user/myaccount2";//我的财富
    String ORDER_WITHDRAWAL = "order/withdrawal";//提现
    String ORDER_WITHDRAWAL_LOG = "order/withdrawal/log";//提现列表

    /**
     * 登录模块
     */
    String SIGN = "sign";//登录
    String SIGN_GET_VERIFY = "sign/get/verify";//获取验证码
    String SIGN_REGISTER = "sign/register";//注册
    String SIGN_FORGET = "sign/forget";//忘记密码
    String SIGN_VERIFY_CODE = "sign/verify/code";//验证验证码
    String CHANGE = "change";//修改密码

    /**
     * 充值
     */
    String USER_ACCOUNT = "user/account";//我的账户
    String PACKAGE_RECHARGE = "package/recharge";//充值套餐列表
    String ORDER_RECHARGE = "order/recharge";//虚拟币1 套餐充值
    String ORDER_COIN1_LOG = "order/coin1/log";//虚拟币1 消费记录
    String ANCHOR_PRICE = "anchor/price";//主播价格


    String HOME = "home";//首页
    String ANCHOR_INFO = "anchor/info";//主播资料
    /**
     * Returns 参数返回列表
     */
    String HTTP_OK = "0";
    int NORECORD = 550;// 没有找到记录
    int DATABASEADDERROR = 600;// 数据库添加错误
}
