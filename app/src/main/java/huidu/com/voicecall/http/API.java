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
    String BASE_URL = "http://test.51qdd.net/api/";

    String AUTH_REALNAME = "auth/realname";//实名认证
    String AUTH_BASE_INFO = "auth/base/info";//基础信息
    String AUTH_BANK = "auth/bank";//绑定银行卡
    String MYCENTER_BANK = "mycenter/bank";//银行卡列表
    String USER_APPROVE = "user/approve";//认证所有状态
    String AUTH_PERSON = "auth/person";//身份证认证(前三张)
    String AUTH_FACE = "auth/face";//人脸认证



    /**
     * Returns 参数返回列表
     */
    String HTTP_OK = "0";
    int NORECORD = 550;// 没有找到记录
    int DATABASEADDERROR = 600;// 数据库添加错误
}
