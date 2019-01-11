package huidu.com.voicecall.bean;

import java.io.Serializable;

/**
 * Description:
 * Data：2019/1/8-9:28
 * Author: lin
 */
public class SignBean implements Serializable {

    String token;
    String user_id;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
