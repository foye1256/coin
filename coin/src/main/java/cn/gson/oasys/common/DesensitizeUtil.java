package cn.gson.oasys.common;

import org.apache.commons.lang3.StringUtils;
 
/**
 * @Description: 脱敏工具类
 *      java数据脱敏，简单形式分为3类：
 *          一、保留前n位
 *          二、保留后m位
 *          三、保留前n位+保留后m位。
 * @Version: 1.0
 */
public class DesensitizeUtil {
    /**
     * @Description:   保留前面几位 比如 姓名 张**
     * @param:         [fullName, index]
     * @return:        java.lang.String
     * @Version:       1.0
     */
    public static String left(String str,int index) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        String name = StringUtils.left(str, index);
        return StringUtils.rightPad(name, StringUtils.length(str), "*");
    }
 
    /**
     * @Description:    前面保留 index 位明文，后面保留 end 位明文,如：[身份证号] 110****58，前面保留3位明文，后面保留2位明文
     * @param:         [name, index, end]
     * @return:        java.lang.String
     * @Version:       1.0
     */
    public static String around(String str, int index, int end) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        return StringUtils.left(str, index).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(str, end), StringUtils.length(str), "*"), "***********************************"));
//        return StringUtils.left(str, index).concat("***").concat(StringUtils.leftPad(StringUtils.right(str, end), StringUtils.length(str), ""));
    }
 
 
    public static String around1(String str, int index, int end) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        return StringUtils.left(str, index).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(str, end), StringUtils.length(str), "*"), "*********************"));
    }
    /**
     * @Description:   保留后面几位 如手机号 *******5678
     * @param:         [num, end]
     * @return:        java.lang.String
     * @Version:       1.0
     */
    public static String right(String str,int end) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        return StringUtils.leftPad(StringUtils.right(str, end), StringUtils.length(str), "*");
    }
}