package com.ysyy.biserver.common;

public class ReturnUtils {

    /**
     * 登录失败
     * @return
     */
    public static String get2001(){
        return "{\n" +
                "\t\"msg\": \"登录失败\",\n" +
                "\t\"error_code\": 2001\n" +
                "}";

    }

    /**
     * 非法调用
     * @return
     */
    public static String get2002(){
        return "{\n" +
                "\t\"msg\": \"非法调用\",\n" +
                "\t\"error_code\": 2002\n" +
                "}";

    }

    /**
     * 令牌过期
     * @return
     */
    public static String get2003(){
        return "{\n" +
                "\t\"msg\": \"令牌过期\",\n" +
                "\t\"error_code\": 2003\n" +
                "}";

    }

    /**
     * 参数错误
     * @return
     */
    public static String get2004(){
        return "{\n" +
                "\t\"msg\": \"参数错误\",\n" +
                "\t\"error_code\": 2004\n" +
                "}";

    }
}
