package com.www.common.pojo.dto.redis;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>@Description 博客文章 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/11/10 23:05 </p>
 */
@Data
@Accessors(chain = true)//开启链式编程
public class BlogArticleDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 当前页数 **/
    private Integer pageNum;
    /** 页面条数 **/
    private Long pageSize;
    /**
    * 博客主键
    */
    private Long blogId;
    /**
     * 博客分组主键
     */
    private Long groupId;
    /**
     * 博客分组名称
     */
    private String groupName;
    /**
     * 博客收藏分组名称
     */
    private String collectName;
    /**
     * 标签ID集合
     */
    private List<Long> tagIds;
    /**
     * 博客分类集合
     */
    private List<BlogTagDTO> blogTag;
    /**
     * 标签ID
     */
    private Long tagId;
    /**
     * 博客标签名称
     */
    private String tagName;
    /**
    * 用户ID
    */
    private String userId;
    /**
    * 博客主题
    */
    private String title;
    /**
     * 博客摘要
     */
    private String summary;
    /**
     * 博客封面图片
     */
    private String coverImg;
    /**
    * 博客内容
    */
    private String content;
    /**
    * 博客浏览量
    */
    private Long browse;
    /**
    * 博客点赞数
    */
    private Long praise;
    /**
    * 当前用户是否点赞该博客
    */
    private Boolean praised;
    /**
    * 博客创建日期
    */
    private String createDate;
    /**
    * 博客评论数
    */
    private Long comment;
    /**
    * 博客收藏数
    */
    private Integer collect;
    /**
    * 当前用户是否收藏该博客
    */
    private Boolean collection;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
}