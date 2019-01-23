package huidu.com.voicecall.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * Data：2019/1/10-14:30
 * Author: lin
 */
public class Home implements Serializable {

    List<Banner> banner;
    List<Anchor> anchor;
    List<Type> type;
    TypeImage type_image;

    public TypeImage getType_image() {
        return type_image;
    }

    public void setType_image(TypeImage type_image) {
        this.type_image = type_image;
    }

    public List<Banner> getBanner() {
        return banner;
    }

    public void setBanner(List<Banner> banner) {
        this.banner = banner;
    }

    public List<Anchor> getAnchor() {
        return anchor;
    }

    public void setAnchor(List<Anchor> anchor) {
        this.anchor = anchor;
    }

    public List<Type> getType() {
        return type;
    }

    public void setType(List<Type> type) {
        this.type = type;
    }


    public class TypeImage implements Serializable{
        String type_img;
        String rec_img;

        public String getType_img() {
            return type_img;
        }

        public void setType_img(String type_img) {
            this.type_img = type_img;
        }

        public String getRec_img() {
            return rec_img;
        }

        public void setRec_img(String rec_img) {
            this.rec_img = rec_img;
        }
    }

    public class Banner implements Serializable {
        String title;
        String url;
        String image_url;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getImage_url() {
            return image_url;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }
    }

    public class Anchor implements Serializable {
        String nickname;
        String head_image;
        String type;
        String price;
        String sex;
        String anchor_type_id;
        String anchor_id;
        String age;
        String is_robot;

        public String getIs_robot() {
            return is_robot;
        }

        public void setIs_robot(String is_robot) {
            this.is_robot = is_robot;
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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getAnchor_type_id() {
            return anchor_type_id;
        }

        public void setAnchor_type_id(String anchor_type_id) {
            this.anchor_type_id = anchor_type_id;
        }

        public String getAnchor_id() {
            return anchor_id;
        }

        public void setAnchor_id(String anchor_id) {
            this.anchor_id = anchor_id;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }
    }

    public class Type implements Serializable {
        String id;
        String type_name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType_name() {
            return type_name;
        }

        public void setType_name(String type_name) {
            this.type_name = type_name;
        }
    }
}
