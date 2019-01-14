package huidu.com.voicecall.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * Data：2019/1/14-14:48
 * Author: lin
 */
public class OrderList implements Serializable {

    List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public class ListBean implements Serializable {
        String order_no;
        String status;
        String total;
        String times;
        String num;
        String anchor_bus_type;
        String anchor_type_name;
        String nickname;
        String head_image;
        String sex;
        String age;

        public String getOrder_no() {
            return order_no;
        }

        public void setOrder_no(String order_no) {
            this.order_no = order_no;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
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

        public String getAnchor_bus_type() {
            return anchor_bus_type;
        }

        public void setAnchor_bus_type(String anchor_bus_type) {
            this.anchor_bus_type = anchor_bus_type;
        }

        public String getAnchor_type_name() {
            return anchor_type_name;
        }

        public void setAnchor_type_name(String anchor_type_name) {
            this.anchor_type_name = anchor_type_name;
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
