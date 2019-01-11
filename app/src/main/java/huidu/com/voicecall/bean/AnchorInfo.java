package huidu.com.voicecall.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * Data：2019/1/10-15:25
 * Author: lin
 */
public class AnchorInfo implements Serializable {
    String nickname;
    String head_image;
    String anchor_id;
    String age;
    String sex;
    List<String> cover;
    String voice;
    String price;
    String type;
    String is_attention;
    String anchor_type_text;

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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public List<String> getCover() {
        return cover;
    }

    public void setCover(List<String> cover) {
        this.cover = cover;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

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

    public String getIs_attention() {
        return is_attention;
    }

    public void setIs_attention(String is_attention) {
        this.is_attention = is_attention;
    }

    public String getAnchor_type_text() {
        return anchor_type_text;
    }

    public void setAnchor_type_text(String anchor_type_text) {
        this.anchor_type_text = anchor_type_text;
    }
}
