package huidu.com.voicecall.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * Data：2019/1/10-10:37
 * Author: lin
 */
public class PackageRecharge implements Serializable {
    List<PackageListBean> package_list;
    List<String> pay_list;
    UserList user_list;

    public List<PackageListBean> getPackage_list() {
        return package_list;
    }

    public void setPackage_list(List<PackageListBean> package_list) {
        this.package_list = package_list;
    }

    public List<String> getPay_list() {
        return pay_list;
    }

    public void setPay_list(List<String> pay_list) {
        this.pay_list = pay_list;
    }

    public UserList getUser_list() {
        return user_list;
    }

    public void setUser_list(UserList user_list) {
        this.user_list = user_list;
    }

    public class UserList implements Serializable{
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

    public class PackageListBean implements Serializable{

        String package_id;
        String package_name;
        String coin;
        String price;
        String is_sale;
        String sale_price;
        boolean isCheck = false;

        public String getPackage_id() {
            return package_id;
        }

        public void setPackage_id(String package_id) {
            this.package_id = package_id;
        }

        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
        }

        public String getPackage_name() {
            return package_name;
        }

        public void setPackage_name(String package_name) {
            this.package_name = package_name;
        }

        public String getCoin() {
            return coin;
        }

        public void setCoin(String coin) {
            this.coin = coin;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getIs_sale() {
            return is_sale;
        }

        public void setIs_sale(String is_sale) {
            this.is_sale = is_sale;
        }

        public String getSale_price() {
            return sale_price;
        }

        public void setSale_price(String sale_price) {
            this.sale_price = sale_price;
        }
    }
}
