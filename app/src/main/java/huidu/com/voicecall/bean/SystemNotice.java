package huidu.com.voicecall.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * Data：2019/1/16-9:22
 * Author: lin
 */
public class SystemNotice implements Serializable {

    List<Notice> notice;

    public List<Notice> getNotice() {
        return notice;
    }

    public void setNotice(List<Notice> notice) {
        this.notice = notice;
    }

    public class Notice implements Serializable{
        String id;
        String sendTime;
        String title;
        String content;
        String icon;

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSendTime() {
            return sendTime;
        }

        public void setSendTime(String sendTime) {
            this.sendTime = sendTime;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
