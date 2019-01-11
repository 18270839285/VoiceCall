package huidu.com.voicecall.bean;

import java.io.Serializable;

/**
 * Description:
 * Data：2019/1/10-10:35
 * Author: lin
 */
public class UserAccount implements Serializable {
    Info info;

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public class Info implements Serializable{
        String id;
        String head_image;
        String nickname;
        String telephone;
        String consume_coin;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getHead_image() {
            return head_image;
        }

        public void setHead_image(String head_image) {
            this.head_image = head_image;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getConsume_coin() {
            return consume_coin;
        }

        public void setConsume_coin(String consume_coin) {
            this.consume_coin = consume_coin;
        }
    }
}
