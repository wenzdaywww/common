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
 * <p>@Description 角色信息 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/11/10 22:24 </p>
 */
@Data
@Accessors(chain = true)//开启链式编程
@TableName("SYS_ROLE")
public class SysRoleEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
    * 角色主键
    */
    @TableId(value = "ROLE_ID",type = IdType.AUTO)
    private Long roleId;
    /**
    * 角色编码
    */
    @TableField("ROLE_CODE")
    private String roleCode;
    /**
    * 角色名称
    */
    @TableField("ROLE_NAME")
    private String roleName;
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