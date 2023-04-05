### 使用com.www.common.config.code下的数据字典配置说明：
#### 1、需要结合redis使用
#### 2、com.www.common.config.code.read为只从redis读取数据字典，com.www.common.config.code.write为将数据字典写入redis
#### 3、如果只从redis读取数据字典，须以下配置
##### 3.1、com.www.common.code.read-enable    是否开启数据字典读取
##### 3.2、com.www.common.code.read-scheduled 定时重新读取字典的时间格式，设置定时需在启动类添加@EnableScheduling
##### 3.3、com.www.common.code.code-redis-key  redis中数据字典的key
#### 4、如果要使用将数据字典写入redis中，须以下配置
##### 4.1、Application类需要添加扫描code的mapper，@MapperScan(basePackages = {"com.www.common.config.code"})
##### 4.2、application.yml的必须是mapper-locations: classpath*:mapper/**/*.xml
##### 4.3、com.www.common.code.write-enable    是否开启数据字典写入redis
##### 4.4、com.www.common.code.write-scheduled 定时重新写入redis数据字典的时间格式，设置定时需在启动类添加@EnableScheduling
##### 4.5、com.www.common.code.code-redis-lock 数据字典的分布式锁key，不为空则使用分布式锁将数据字典写入redis
##### 4.6、com.www.common.code.code-redis-key  redis中数据字典的key