package huidu.com.voicecall.bean;

import java.io.Serializable;

/**
 * Description:
 * Data：2019/1/14-14:49
 * Author: lin
 */
public class OrderInfo implements Serializable {
    InfoBean info;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public class InfoBean implements Serializable{
        String is_anchor;
        String user_id;
        String nickname;
        String head_image;
        String sex;
        String age;
        String status;
        String anchor_expire_time;
        String ready_expire_time;
        String anchor_type_text;
        String create_time;
        String start_time;
        String times;
        String num;
        String anchor_price;
        String anchor_bus_type;
        String total;
        String expire_time;
        String accid;

        public String getAccid() {
            return accid;
        }

        public void setAccid(String accid) {
            this.accid = accid;
        }

        public String getExpire_time() {
            return expire_time;
        }

        public void setExpire_time(String expire_time) {
            this.expire_time = expire_time;
        }

        public String getIs_anchor() {
            return is_anchor;
        }

        public void setIs_anchor(String is_anchor) {
            this.is_anchor = is_anchor;
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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getAnchor_expire_time() {
            return anchor_expire_time;
        }

        public void setAnchor_expire_time(String anchor_expire_time) {
            this.anchor_expire_time = anchor_expire_time;
        }

        public String getReady_expire_time() {
            return ready_expire_time;
        }

        public void setReady_expire_time(String ready_expire_time) {
            this.ready_expire_time = ready_expire_time;
        }

        public String getAnchor_type_text() {
            return anchor_type_text;
        }

        public void setAnchor_type_text(String anchor_type_text) {
            this.anchor_type_text = anchor_type_text;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public String getTimes() {
            return times;
        }

        public void setTimes(String times) {
            this.times = times;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getAnchor_price() {
            return anchor_price;
        }

        public void setAnchor_price(String anchor_price) {
            this.anchor_price = anchor_price;
        }

        public String getAnchor_bus_type() {
            return anchor_bus_type;
        }

        public void setAnchor_bus_type(String anchor_bus_type) {
            this.anchor_bus_type = anchor_bus_type;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }
    }
}
