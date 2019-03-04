package huidu.com.voicecall.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * Data：2019/3/4-15:41
 * Author: lin
 */
public class UserBlack implements Serializable {

    List<BlackList> list;

    public List<BlackList> getList() {
        return list;
    }

    public void setList(List<BlackList> list) {
        this.list = list;
    }

    public class BlackList implements Serializable{
        String black_id;
        String user_id;
        String nickname;
        String head_image;
        String sex;
        String age;

        public String getBlack_id() {
            return black_id;
        }

        public void setBlack_id(String black_id) {
            this.black_id = black_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getHead_image() {
            return head_image;
        }

        public void setHead_image(String head_image) {
            this.head_image = head_image;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }
    }
}
