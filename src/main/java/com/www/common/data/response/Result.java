package com.www.common.data.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>@Description 响应报文类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/8/1 21:21 </p>
 */
@Data
@Accessors(chain = true)//开启链式编程
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 请求全局跟踪号 **/
    private String traceId;
    /** 当前页数 **/
    private Integer pageNum;
    /** 页面条数 **/
    private Long pageSize;
    /** 列表查询总条数 **/
    private Long totalNum;
    /** 响应结果数据 **/
    private T data;

    /**
     * <p>@Description 响应报文构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/1 21:22 </p>
     */
    public Result() {}
    /**
     * <p>@Description 响应报文构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/1 21:22 </p>
     * @param data 数据
     */
    public Result(T data) {
        this.data = data;
    }
    /**
     * <p>@Description 响应报文构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/25 13:48 </p>
     * @param pageNum 当前页数
     * @param pageSize 页面条数
     * @param totalNum 列表查询总条数
     * @param data 响应结果数据
     */
    public Result(int pageNum,long pageSize,long totalNum,T data) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.totalNum = totalNum;
        this.data = data;
    }
    /**
     * <p>@Description 响应报文构造方法：复制另一个Result除data以外的数据并设置响应数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/1 21:22 </p>
     * @param result 响应对象
     * @param data 响应数据
     */
    public Result(Result result, T data) {
        this.traceId = result.getTraceId();
        this.pageNum = result.getPageNum();
        this.pageSize = result.getPageSize();
        this.totalNum = result.getTotalNum();
        this.data = data;
    }
    /**
     * <p>@Description 响应报文构造方法：复制另一个Response除data以外的数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/8/1 21:22 </p>
     * @param result 响应对象
     */
    public Result(Result result) {
        this.traceId = result.getTraceId();
        this.pageNum = result.getPageNum();
        this.pageSize = result.getPageSize();
        this.totalNum = result.getTotalNum();
    }
    /**
     * <p>@Description 获取成功返回的数据,只要code=200才有值 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/23 16:09 </p>
     * @param result 返回对象
     * @return T 返回数据
     */
    public static <T> T getBackData(Result<T> result){
        if(result == null){
            return null;
        }
        return result.data;
    }
}
