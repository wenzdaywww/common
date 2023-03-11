package com.www.common.config.code.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>@Description 码值数据 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2022/1/1 14:24 </p>
 */
@Data
@Accessors(chain = true)//开启链式编程
public class CodeDTO {
    /** code类型 **/
    private String type;
    /** code类型名称 **/
    private String typeName;
    /** code的key **/
    private String codeKey;
    /** code的数值 **/
    private String value;
    /** code的数值名称 **/
    private String name;
}
