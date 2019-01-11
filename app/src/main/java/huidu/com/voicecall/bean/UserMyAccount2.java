package huidu.com.voicecall.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * Data：2019/1/9-14:21
 * Author: lin
 */
public class UserMyAccount2 implements Serializable {

    Info info;

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public class Info implements Serializable{
        String id;
        String nickname;
        String earnings_coin;
        String exchange_pro;
        String formalities_pro;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getEarnings_coin() {
            return earnings_coin;
        }

        public void setEarnings_coin(String earnings_coin) {
            this.earnings_coin = earnings_coin;
        }

        public String getExchange_pro() {
            return exchange_pro;
        }

        public void setExchange_pro(String exchange_pro) {
            this.exchange_pro = exchange_pro;
        }

        public String getFormalities_pro() {
            return formalities_pro;
        }

        public void setFormalities_pro(String formalities_pro) {
            this.formalities_pro = formalities_pro;
        }
    }
}
