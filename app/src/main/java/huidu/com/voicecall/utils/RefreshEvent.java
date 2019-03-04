package huidu.com.voicecall.utils;

/**
 * Description:
 * Data：2019/3/4-14:55
 * Author: lin
 */
public class RefreshEvent {
    private String message;
    public  RefreshEvent(String message){
        this.message=message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
