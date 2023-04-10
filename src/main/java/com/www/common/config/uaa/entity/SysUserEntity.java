package com.www.common.config.uaa.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>@Description 用户信息 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/11/10 22:24 </p>
 */
@Data
@TableName("SYS_USER")
public class SysUserEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
    * 用户主键
    */
    @TableId(value = "SU_ID",type = IdType.AUTO)
    private Long suId;
    /**
    * 用户ID
    */
    @TableField("USER_ID")
    private String userId;
    /**
    * 用户昵称
    */
    @TableField("USER_NAME")
    private String userName;
    /**
    * 密码
    */
    @TableField("PASSWORD")
    private String password;
    /**
    * 手机号
    */
    @TableField("PHONE_NUM")
    private String phoneNum;
    /**
    * 生日
    */
    @TableField("BIRTHDAY")
    private Date birthday;
    /**
    * 性别：1男0女
    */
    @TableField("SEX")
    private String sex;
    /**
    * 头像
    */
    @TableField("PHOTO")
    private String photo;
    /**
    * 邮箱
    */
    @TableField("EMAIL")
    private String email;
    /**
    * 个人简介
    */
    @TableField("BRIEF")
    private String brief;
    /**
    * 用户状态：1有效，2注销，3封号
    */
    @TableField("STATE_CD")
    private String stateCd;
    /**
    * 是否未过期。默认为1未过期，0过期
    */
    @TableField("NOT_EXPIRED")
    private String notExpired;
    /**
    * 账号是否未锁定。默认为1未锁定，0锁定
    */
    @TableField("NOT_LOCKED")
    private String notLocked;
    /**
    * 证书（密码）是否未过期。默认为1未过期，0过期
    */
    @TableField("CREDENTIALS_NOT_EXPIRED")
    private String credentialsNotExpired;
    /**
    * 更新时间
    */
    @TableField("UPDATE_TIME")
    private Date updateTime;
    /**
    * 创建时间
    */
    @TableField("CREATE_TIME")
    private Date createTime;
}