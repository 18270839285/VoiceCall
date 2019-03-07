package huidu.com.voicecall.bean;

import java.io.Serializable;

/**
 * Description:
 * Data：2019/3/6-11:44
 * Author: lin
 */
public class Comment  implements Serializable{
    String comment_id;
    String user_id;
    String content;
    String type;
    String dynamic_id;
    String create_id;
    String comment_at;
    FromUser from_user;
    ToUser to_user;

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDynamic_id() {
        return dynamic_id;
    }

    public void setDynamic_id(String dynamic_id) {
        this.dynamic_id = dynamic_id;
    }

    public String getCreate_id() {
        return create_id;
    }

    public void setCreate_id(String create_id) {
        this.create_id = create_id;
    }

    public String getComment_at() {
        return comment_at;
    }

    public void setComment_at(String comment_at) {
        this.comment_at = comment_at;
    }

    public FromUser getFrom_user() {
        return from_user;
    }

    public void setFrom_user(FromUser from_user) {
        this.from_user = from_user;
    }

    public ToUser getTo_user() {
        return to_user;
    }

    public void setTo_user(ToUser to_user) {
        this.to_user = to_user;
    }

    public class FromUser implements Serializable {
        String id;
        String nickname;

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
    }

    public class ToUser implements Serializable {
        String id;
        String nickname;

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
    }
}
