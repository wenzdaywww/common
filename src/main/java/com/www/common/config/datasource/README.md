### 使用com.www.common.config.datasource包下的多数据源配置要求：
#### 1、application.yml需要配置 com.www.common.datasource.enable : 是否开启多数据源配置
#### 2、application.yml需要配置 com.www.common.datasource.monitor.enable : 是否开启druid监控平台
#### 3、application.yml最少需要配置一个具有读写权限的数据源，数据源配置参数与druid的配置参数一致，
#### 默认可配置一个读写数据源com.www.common.datasource.write，两个只读数据源com.www.common.datasource.read-one和com.www.common.datasource.read-two
#### 如果需要实现更多个数据源，读写数据源需要继承DruidDataSource实现IWriteDataSoure接口，只读数据源需要继承DruidDataSource实现IReadDataSoure接口，数据源配置参数prefix可自定义