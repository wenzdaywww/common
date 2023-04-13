## 使用mybatis包下的自定义Mybatis配置引入mybatis依赖即开启
*  需引入以下依赖
````
<dependency>
  <groupId>com.baomidou</groupId>
  <artifactId>mybatis-plus-boot-starter</artifactId>
  <version></version>
</dependency>
````
* com.www.common.mybatis.limit 是否开启全局结果集数量限制，默认开启
* com.www.common.mybatis.database 数据库类型，不区分大小写，默认为mysql
* com.www.common.mybatis.limit-num 结果集限制数量，默认10000条，使用@RowLimitInterceptor注解则限制数量失效
