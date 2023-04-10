package com.www.common.config.uaa.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>@Description 客户端信息 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/11/10 22:24 </p>
 */
@Data
@TableName("OAUTH_CLIENT_DETAIL")
public class OauthClientDetailEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
    * 客户端ID
    */
    @TableId(value = "CLIENT_ID")
    private String clientId;
    /**
    * 客户端密钥
    */
    @TableField("CLIENT_SECRET")
    private String clientSecret;
    /**
    * 客户端拥有的资源列表,逗号分割
    */
    @TableField("RESOURCE_ID")
    private String resourceId;
    /**
    * 认证范围,逗号分割
    */
    @TableField("GRANT_TYPE")
    private String grantType;
    /**
    * 允许的授权范围,逗号分割
    */
    @TableField("SCOPE")
    private String scope;
    /**
    * 是否跳转授权页面,1是0否
    */
    @TableField("AUTO_APPROVE")
    private String autoApprove;
    /**
    * 回调地址
    */
    @TableField("REDIRECT_URI")
    private String redirectUri;
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