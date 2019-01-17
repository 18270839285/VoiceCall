package huidu.com.voicecall.bean;

import java.io.Serializable;

/**
 * Description:
 * Data：2019/1/8-9:29
 * Author: lin
 */
public class UserInfo implements Serializable {

    String id;
    String nickname;
    String head_image;
    String consume_coin;
    String earnings_coin;
    String age;
    String birthday;
    String addr;
    String zodiac;
    String introduce;
    String attention_count;
    String fans_count;
    String sex;
    String is_idcard_auth;
    String is_anchor_auth;
    String order_sum;
    String custom_tel;

    public String getCustom_tel() {
        return custom_tel;
    }

    public void setCustom_tel(String custom_tel) {
        this.custom_tel = custom_tel;
    }

    public String getOrder_sum() {
        return order_sum;
    }

    public void setOrder_sum(String order_sum) {
        this.order_sum = order_sum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getConsume_coin() {
        return consume_coin;
    }

    public void setConsume_coin(String consume_coin) {
        this.consume_coin = consume_coin;
    }

    public String getEarnings_coin() {
        return earnings_coin;
    }

    public void setEarnings_coin(String earnings_coin) {
        this.earnings_coin = earnings_coin;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getZodiac() {
        return zodiac;
    }

    public void setZodiac(String zodiac) {
        this.zodiac = zodiac;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getAttention_count() {
        return attention_count;
    }

    public void setAttention_count(String attention_count) {
        this.attention_count = attention_count;
    }

    public String getFans_count() {
        return fans_count;
    }

    public void setFans_count(String fans_count) {
        this.fans_count = fans_count;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIs_idcard_auth() {
        return is_idcard_auth;
    }

    public void setIs_idcard_auth(String is_idcard_auth) {
        this.is_idcard_auth = is_idcard_auth;
    }

    public String getIs_anchor_auth() {
        return is_anchor_auth;
    }

    public void setIs_anchor_auth(String is_anchor_auth) {
        this.is_anchor_auth = is_anchor_auth;
    }
}
