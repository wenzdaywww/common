### 使用code包下的数据字典配置说明：
* 需要引入以下依赖
````
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
    <version></version>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version></version>
</dependency>
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version></version>
</dependency>
````
* com.www.common.config.code.read为只从redis读取数据字典，com.www.common.config.code.write为将数据字典写入redis，两者只需二选一
* 如果只从redis读取数据字典，须以下配置
  + com.www.common.code.read-enable    是否开启数据字典读取，默认关闭false
  + com.www.common.code.read-scheduled 定时重新读取字典的时间格式，默认每天0点执行
  + com.www.common.code.code-redis-key  redis中数据字典的key
* 如果要使用将数据字典写入redis中，须以下配置
  + application.yml的必须是mapper-locations: classpath*:mapper/**/*.xml
  + 需要执行com.www.common.config.code.doc下的【code相关表模型.sql】脚本创建相关表
  + com.www.common.code.write-enable    是否开启数据字典写入redis，默认关闭false
  + com.www.common.code.write-scheduled 定时重新写入redis数据字典的时间格式，默认每天0点执行
  + com.www.common.code.code-redis-lock 数据字典的分布式锁key，不为空则使用分布式锁将数据字典写入redis
  + com.www.common.code.code-redis-key  redis中数据字典的key