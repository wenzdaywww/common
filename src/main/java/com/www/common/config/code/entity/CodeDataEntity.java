package com.www.common.config.code.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>@Description 数据字典信息 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/11/10 22:21 </p>
 */
@Data
@Accessors(chain = true)//开启链式编程
@TableName("CODE_DATA")
public class CodeDataEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
    * code类型
    */
    @TableField(value = "CODE_TYPE")
    private String codeType;
    /**
    * code类型名称
    */
    @TableField("CODE_NAME")
    private String codeName;
    /**
     * code的key
     */
    @TableField("CODE_KEY")
    private String codeKey;
    /**
    * 码值
    */
    @TableField("CODE_VALUE")
    private String codeValue;
    /**
     * 码值名称
     */
    @TableField("VALUE_NAME")
    private String valueName;
    /**
     * 是否有效，1有效，0无效
     */
    @TableField("IS_VALID")
    private String isValid;
}