package huidu.com.voicecall.bean;

import java.io.Serializable;

/**
 * Description:
 * Data：2019/3/1-15:03
 * Author: lin
 */
public class ViolationType implements Serializable{

    String violation_id;
    String name;

    public String getViolation_id() {
        return violation_id;
    }

    public void setViolation_id(String violation_id) {
        this.violation_id = violation_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
