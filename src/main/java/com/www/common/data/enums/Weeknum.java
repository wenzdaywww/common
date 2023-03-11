package com.www.common.data.enums;

/**
 * <p>@Description 一周枚举 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2022/3/22 10:36 </p>
 */
public enum Weeknum {
    /** 星期日 */
    SUNDAY("星期日"),
    /** 星期一 */
    MONDAY("星期一"),
    /** 星期二 */
    TUESDAY("星期二"),
    /** 星期三 */
    WEDNESDAY("星期三"),
    /** 星期四 */
    THURSDAY("星期四"),
    /** 星期五 */
    FRIDAY("星期五"),
    /** 星期六 */
    SATURDAY("星期六");

    private String name;

    Weeknum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
