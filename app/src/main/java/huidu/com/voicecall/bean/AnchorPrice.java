package huidu.com.voicecall.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * Data：2019/1/10-17:04
 * Author: lin
 */
public class AnchorPrice implements Serializable {

    Info info;

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public class Info implements Serializable{
       String price;
       String type;
       String balance;
       List<String> times;

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public List<String> getTimes() {
            return times;
        }

        public void setTimes(List<String> times) {
            this.times = times;
        }
    }
}
