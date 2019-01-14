package huidu.com.voicecall.bean;

import java.io.Serializable;

/**
 * Description:
 * Data：2019/1/10-10:41
 * Author: lin
 */
public class SpareBean implements Serializable {
    String order_no;
    String image_url;

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }
}
