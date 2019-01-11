package huidu.com.voicecall.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * Data：2019/1/10-10:37
 * Author: lin
 */
public class PackageRecharge implements Serializable {
    List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public class ListBean implements Serializable{

        String package_name;
        String coin;
        String price;
        String is_sale;
        String sale_price;
        boolean isCheck = false;

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
