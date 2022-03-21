package com.www.common.pojo.dto.feign;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>@Description 用户信息 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/11/10 22:24 </p>
 */
@Data
@Accessors(chain = true)//开启链式编程
public class UserInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
    * 用户主键
    */
    private Long suId;
    /**
    * 用户ID
    */
    private String userId;
    /**
    * 用户昵称
    */
    private String userName;
    /**
    * 手机号
    */
    private String phoneNum;
    /**
    * 生日
    */
    private Date birthday;
    /**
    * 性别：1男0女
    */
    private String sex;
    /**
    * 粉丝数
    */
    private Integer fans;
    /**
    * 关注数
    */
    private Integer friends;
    /**
    * 头像
    */
    private String photo;
    /**
    * 邮箱
    */
    private String email;
    /**
     * 个人简介
     */
    private String brief;
    /**
    * 更新时间
    */
    private Date updateTime;
    /**
    * 创建时间
    */
    private Date createTime;

}