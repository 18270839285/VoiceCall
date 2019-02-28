package huidu.com.voicecall.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * Data：2019/2/21-10:45
 * Author: lin
 */
public class DynamicData implements Serializable {

    List<DynamicList> list;

    public List<DynamicList> getList() {
        return list;
    }

    public void setList(List<DynamicList> list) {
        this.list = list;
    }

    public class DynamicList implements Serializable {

        String dynamic_id;
        String user_id;
        String content;
        String created_at;
        String nickname;
        String head_image;
        List<String> image;
        String like_count;
        String is_my;
        String sex;
        String audio;
        String audio_time;

        public String getAudio_time() {
            return audio_time;
        }

        public void setAudio_time(String audio_time) {
            this.audio_time = audio_time;
        }

        public String getAudio() {
            return audio;
        }

        public void setAudio(String audio) {
            this.audio = audio;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getLike_count() {
            return like_count;
        }

        public void setLike_count(String like_count) {
            this.like_count = like_count;
        }

        public String getIs_my() {
            return is_my;
        }

        public void setIs_my(String is_my) {
            this.is_my = is_my;
        }

        public String getDynamic_id() {
            return dynamic_id;
        }

        public void setDynamic_id(String dynamic_id) {
            this.dynamic_id = dynamic_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
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

        public List<String> getImage() {
            return image;
        }

        public void setImage(List<String> image) {
            this.image = image;
        }
    }
}

