package com.www.common.config.security.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>@Description 角色访问权限信息 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/11/10 22:25 </p>
 */
@Data
@Accessors(chain = true)//开启链式编程
@TableName("AUTHORITY_ROLE")
public class AuthorityRoleEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
    * 用户角色主键
    */
    @TableId(value = "AR_ID",type = IdType.AUTO)
    private Long arId;
    /**
    * 访问路径
    */
    @TableField("URL")
    private String url;
    /**
    * 角色ID
    */
    @TableField("ROLE_ID")
    private Long roleId;
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