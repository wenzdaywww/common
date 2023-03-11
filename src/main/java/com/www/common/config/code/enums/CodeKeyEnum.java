package com.www.common.config.code.enums;

/**
 * <p>@Description 通用key值枚举类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/12/2 21:04 </p>
 */
public enum CodeKeyEnum {
    /** 通用key：K0 **/
    K0("K0"),
    /** 通用key：K1 **/
    K1("K1"),
    /** 通用key：K2 **/
    K2("K2"),
    /** 通用key：K3 **/
    K3("K3"),
    /** 通用key：K4 **/
    K4("K4"),
    /** 通用key：K5 **/
    K5("K5"),
    /** 通用key：K6 **/
    K6("K6"),
    /** 通用key：K7 **/
    K7("K7"),
    /** 通用key：K8 **/
    K8("K8"),
    /** 通用key：K9 **/
    K9("K9"),
    /** 通用key：K10 **/
    K10("K10")
    ;

    /** key值 **/
    private String key;
    /**
     * <p>@Description 构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/2 21:05 </p>
     * @param key key值
     * @return
     */
    CodeKeyEnum(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

}
