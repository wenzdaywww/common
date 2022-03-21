package com.www.common.pojo.enums;

/**
 * <p>@Description code类型枚举类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/12/2 21:04 </p>
 */
public enum CodeTypeEnum {
    /** 博客状态 **/
    BLOG_STATUS("blogStatus","博客状态"),
    /** 菜单类型 **/
    MENU_TYPE("menuType","菜单类型"),
    /** 性别 **/
    SEX("sex","性别"),
    /** 是否标志 **/
    YES_NO("yesNo","是否指示器"),
    /** 用户状态 **/
    USER_STATUS("userStatus","用户状态")
    ;


    /** 名称 **/
    private String codeName;
    /** 码值 **/
    private String codeType;
    /**
     * <p>@Description 构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/12/2 21:05 </p>
     * @param codeType code类型
     * @param codeName code类型名称
     * @return
     */
    CodeTypeEnum(String codeType, String codeName) {
        this.codeType = codeType;
        this.codeName = codeName;
    }

    public String getCodeName() {
        return codeName;
    }

    public String getCodeType() {
        return codeType;
    }

}
