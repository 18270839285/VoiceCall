package huidu.com.voicecall.http;

import android.os.Environment;

import java.io.File;

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
//    String TOKEN_TEST = "L2uk5tVN4CUQQb0P6n650BRTVLc4q0tV7K050Bdd0u0N7TKP";
//    String TOKEN_TEST = "UfCQ19B0Y9XQX1Q0m8nDxbCyx1w0XnY000BY1cy013eCC3tq";
//    String USERID = "1";
//    String TOKEN_TEST = "Sf3JalUwgJEYIl32FSgyYe3YwYS1FeLYjiwfjMuIffls8LYy";
//    String USERID = "6";
    String TOKEN_TEST = "H5K2SBXac8Bm5JscrYA3j88kwyBy5mZ555W0fcZ5DzF5Cr26";
    String USERID = "14";

    /**
     * h5地址
     */
    String WithdrawalUrl = "http://support.51qdd.net/Withdrawal/#/";//提现规则
    String WithdrawalUrl1 = "http://support.51qdd.net/Withdrawal/#/";//主播认证
    String VOICECARD = "http://support.51qdd.net/demo.html";//身份认证
    String REGISTER_PROTOCOL = "http://voice.51qdd.net/api/pact?mark=register";//用户注册
    String RECHARGE_PROTOCOL = "http://voice.51qdd.net/api/pact?mark=recharge";//充值服务协议
    String TIXIAN_PROTOCOL = "http://voice.51qdd.net/api/pact?mark=tixian";//提现服务协议
    String SECRET_PROTOCOL = "http://voice.51qdd.net/api/pact?mark=secret";//隐私政策
//    String VOICECARD = "http://support.51qdd.net/attestation/#/voiceCard";//身份认证

    String COMMON_IMAGE_UPLOAD = "common/image/upload";//图片上传
    String COMMON_AUDIO = "common/audio";//音频上传
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

    String SIGN_IM_INFO = "sign/im/info";//用户im信息
    String ACCID_NAME = "accid/name";//accid获取信息

    /**
     * 充值
     */
    String USER_ACCOUNT = "user/account";//我的账户
    String PACKAGE_RECHARGE = "package/recharge";//充值套餐列表
    String ORDER_RECHARGE = "order/recharge";//Y豆套餐充值
    String ORDER_COIN1_LOG = "order/coin1/log";//Y豆 消费记录
    String ANCHOR_PRICE = "anchor/price";//主播价格
    String ORDER_VOICE = "order/voice";//语音下单


    String HOME = "home";//首页
    String ANCHOR_INFO = "anchor/info";//主播资料
    String USER_ATTENTION = "user/attention";//关注
    String USER_ATTENTION_CANCEL = "user/attention/cancel";//取关

    /**
     * 订单
     */
    String ORDER_LIST = "order/list";//订单列表
    String ORDER_INFO = "order/info";//订单详情
    String ORDER_CANCEL = "order/cancel";//取消订单
    String ORDER_FINISH = "order/finish";//订单完成
    String ORDER_REFUSE = "order/refuse";//拒绝订单
    String ORDER_RECEIVE = "order/receive";//接单
    String ORDER_BEGIN = "order/begin";//通话开始
    String ORDER_OVER = "order/over";//通话结束

    /**
     * 消息
     */
    String NOTICE_SYSTEM = "notice/system";//系统通知
    String NOTICE_INDEX = "notice/index";//平台消息接口

    /**
     * 认证
     */
    String AUTH_IDENTITY = "auth/identity";//身份证认证
    String AUTH_FACE = "auth/face";//身份证认证
    String AUTH_ANCHOR = "auth/anchor";//主播认证
    String AUTH_ANCHOR_TYPE = "auth/anchor/type";//主播类型
    /**
     * Returns 参数返回列表
     */
    String HTTP_OK = "0";
    String TOKEN_FAILURE = "10086";// token失效
    int DATABASEADDERROR = 600;// 数据库添加错误

    String temp_filename = "temp.jpg";
//    String FILE_DIR = "YinYuan";
    File FILE_DIR = Environment.getExternalStorageDirectory();
    String YinYuan = "/YinYuan/";

    String apiKey = "FG6igBnkGmEimwCvwkuxqeyU";
    String secretKey = "Qb2Qdb9lwKtdQaFYAGa1AGGpNr9PlE6n";
    String licenseID = "YinYuan-face-android";
    String licenseFileName = "idl-license.face-android";

}
