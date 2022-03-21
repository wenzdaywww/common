package com.www.common.config.code;

import com.www.common.pojo.constant.CharConstant;
import com.www.common.pojo.dto.code.CodeDTO;
import com.www.common.pojo.enums.CodeKeyEnum;
import com.www.common.pojo.enums.CodeTypeEnum;
import org.apache.commons.collections4.MapUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>@Description 数据字典类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2022/1/1 13:58 </p>
 */
public class CodeDict {
    /** 数据字典集合Map<codeType,Map<codeKey, CodeDTO>> **/
    private static Map<String,Map<String, CodeDTO>> codeMap = new HashMap<>();


    /**
     * <p>@Description 初始化数据字典数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/1 16:29 </p>
     * @param codeMap
     * @return void
     */
    public static void initCode(Map<String,Map<String, CodeDTO>> codeMap){
        CodeDict.codeMap = codeMap;
    }
    /**
     * <p>@Description 判断value值对应的code类型中是否是非法数值 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/1 15:20 </p>
     * @param codeType code类型枚举值
     * @param value 数值
     * @return boolean true数值非法，false数值合法
     */
    public static boolean isIllegalValue(CodeTypeEnum codeType,String value){
        return isIllegalValue(codeType.getCodeType(),value);
    }
    /**
     * <p>@Description 判断value值对应的code类型中是否是非法数值 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/1 15:20 </p>
     * @param codeType code类型
     * @param value 数值
     * @return boolean true数值非法，false数值合法
     */
    public static boolean isIllegalValue(String codeType,String value){
        return !isLegalValue(codeType,value);
    }
    /**
     * <p>@Description 判断value值对应的code类型中是否是合法数值 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/1 15:20 </p>
     * @param codeType code类型枚举值
     * @param value 数值
     * @return boolean true数值合法，false数值非法
     */
    public static boolean isLegalValue(CodeTypeEnum codeType,String value){
        return isLegalValue(codeType.getCodeType(),value);
    }
    /**
     * <p>@Description 判断value值对应的code类型中是否是合法数值 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/1 15:20 </p>
     * @param codeType code类型
     * @param value 数值
     * @return boolean true数值合法，false数值非法
     */
    public static boolean isLegalValue(String codeType,String value){
        List<String> list = getCodeValues(codeType);
        return list.contains(value);
    }
    /**
     * <p>@Description 获取code类型所有value值 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/1 15:08 </p>
     * @param codeType code类型枚举值
     * @return java.util.List<java.lang.String>
     */
    public static List<String> getCodeValues(CodeTypeEnum codeType){
        if(codeType == null){
            return new ArrayList<>();
        }
        return getCodeValues(codeType.getCodeType());
    }
    /**
     * <p>@Description 获取code类型所有value值 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/1 15:08 </p>
     * @param codeType code类型
     * @return java.util.List<java.lang.String>
     */
    public static List<String> getCodeValues(String codeType){
        Map<String,CodeDTO> keyMap = getAllCodeDTO(codeType);
        if(MapUtils.isNotEmpty(keyMap)){
            List<String> list = keyMap.values().stream().map(CodeDTO::getValue).collect(Collectors.toList());
            return list;
        }
        return new ArrayList<>();
    }
    /**
     * <p>@Description 获取code类型value值的名称 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/1 14:32 </p>
     * @param codeType code类型枚举值
     * @param value 数值
     * @return java.lang.String
     */
    public static String getCodeValueName(CodeTypeEnum codeType,String value){
        if(codeType == null){
            return CharConstant.EMPTY;
        }
        return getCodeValueName(codeType.getCodeType(),value);
    }
    /**
     * <p>@Description 获取code类型value值的名称 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/1 14:32 </p>
     * @param codeType code类型
     * @param value 数值
     * @return java.lang.String
     */
    public static String getCodeValueName(String codeType,String value){
        Map<String,CodeDTO> keyMap = getAllCodeDTO(codeType);
        if(MapUtils.isNotEmpty(keyMap)){
            Map<String,String> valueMap = MapUtils.isEmpty(keyMap) ? new HashMap<>() :
                    keyMap.values().stream().collect(Collectors.toMap(CodeDTO::getValue,CodeDTO::getName,(k1,k2)->k2));
            return valueMap.get(value);
        }
        return CharConstant.EMPTY;
    }
    /**
     * <p>@Description 获取code码key值数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/1 14:01 </p>
     * @param codeType code类型枚举
     * @param codeKey code的key值
     * @return null则不存此数据
     */
    public static String getValue(CodeTypeEnum codeType, CodeKeyEnum codeKey){
        if(codeType == null){
            return null;
        }
        return getValue(codeType.getCodeType(),codeKey.getKey());
    }
    /**
     * <p>@Description 获取code码key值数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/1 14:01 </p>
     * @param codeType code类型
     * @param codeKey code的key值
     * @return java.lang.String null则不存此数据
     */
    public static String getValue(String codeType,String codeKey){
        Map<String,CodeDTO> keyMap = getAllCodeDTO(codeType);
        if(MapUtils.isNotEmpty(keyMap) && keyMap.containsKey(codeKey)){
            CodeDTO codeDTO = keyMap.get(codeKey);
            return codeDTO.getValue();
        }
        return null;
    }
    /**
     * <p>@Description 获取code码key值数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/1 14:01 </p>
     * @param codeType code类型枚举
     * @param codeKey code的key值
     * @return null则不存此数据
     */
    public static CodeDTO getCodeDTO(CodeTypeEnum codeType, CodeKeyEnum codeKey){
        if(codeType == null){
            return new CodeDTO();
        }
        return getCodeDTO(codeType.getCodeType(),codeKey.getKey());
    }
    /**
     * <p>@Description 获取code码key值数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/1 14:01 </p>
     * @param codeType code类型
     * @param codeKey code的key值
     * @return java.lang.String null则不存此数据
     */
    public static CodeDTO getCodeDTO(String codeType,String codeKey){
        Map<String,CodeDTO> keyMap = getAllCodeDTO(codeType);
        if(MapUtils.isNotEmpty(keyMap) && keyMap.containsKey(codeKey)){
            return keyMap.get(codeKey);
        }
        return new CodeDTO();
    }
    /**
     * <p>@Description 获取codeType的所有键值对 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/1 14:15 </p>
     * @param codeType code类型
     * @return java.util.List<java.lang.String>
     */
    public static Map<String,CodeDTO> getAllCodeDTO(CodeTypeEnum codeType){
        if(codeType == null){
            return new HashMap<>();
        }
        return getAllCodeDTO(codeType.getCodeType());
    }
    /**
     * <p>@Description 获取codeType的所有键值对 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/1 14:15 </p>
     * @param codeType code类型
     * @return codeType的所有键值对
     */
    public static Map<String,CodeDTO> getAllCodeDTO(String codeType){
        if(codeMap.containsKey(codeType)){
            return codeMap.get(codeType);
        }
        return new HashMap<>();
    }
}
