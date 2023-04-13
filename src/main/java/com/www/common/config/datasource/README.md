### 使用datasource包的多数据源配置说明：
* 需要引入的相关依赖：
````
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version></version>
</dependency>
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid-spring-boot-starter</artifactId>
    <version></version>
</dependency>
````
* 数据库驱动自行选择
* com.www.common.datasource.enable : 是否开启多数据源配置，默认关闭false
* com.www.common.datasource.monitor.enable : 是否开启druid监控平台，默认关闭false
* com.www.common.datasource.monitor.monitor-name : druid监控平台用户，默认admin
* com.www.common.datasource.monitor.monitor-pwd : druid监控平台密码，默认www362412
* application.yml最少需要配置一个具有读写权限的数据源，数据源配置参数与druid的配置参数一致，
* 默认可配置一个读写数据源com.www.common.datasource.write，两个只读数据源com.www.common.datasource.read-one和com.www.common.datasource.read-two
  + 如果需要实现更多个数据源，读写数据源需要继承DruidDataSource实现IWriteDataSoure接口，只读数据源需要继承DruidDataSource实现IReadDataSoure接口，数据源配置参数prefix可自定义