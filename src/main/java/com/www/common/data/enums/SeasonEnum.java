package com.www.common.data.enums;

/**
 * <p>@Description 季节枚举 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2022/3/22 10:36 </p>
 */
public enum SeasonEnum {
    /** 春天 */
    SPRING(1),
    /** 夏天 */
    SUMMER (2),
    /** 秋天 */
    AUTUMN(3),
    /** 冬天 */
    WINTER(4);

    private Integer name;

    SeasonEnum(java.lang.Integer name) {
        this.name = name;
    }

    public Integer getName() {
        return name;
    }
}
