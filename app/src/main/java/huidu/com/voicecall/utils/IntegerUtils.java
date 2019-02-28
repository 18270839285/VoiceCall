package huidu.com.voicecall.utils;

/**
 * Description:
 * Data：2019/2/25-11:33
 * Author: lin
 */
public class IntegerUtils {
    /**
     * 转化成int
     * @param value
     * @param defaultValue
     * @return
     */
    public static int convertToInt(Object value, int defaultValue){
        if (value == null || value.toString().equals("")){
            return defaultValue;
        }
        try{
            return Integer.valueOf(value.toString());
        }catch (Exception e){
            try {
                return Double.valueOf((value.toString())).intValue();
            }catch (Exception e1){
                return defaultValue;
            }
        }
    }
}
