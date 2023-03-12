### 使用com.www.common.config.security包下的Security安全配置要求：
#### 1、需要引入依赖包
##### <dependency>
#####   <groupId>org.springframework.cloud</groupId>
#####   <artifactId>spring-cloud-starter-security</artifactId>
#####   <version>${spring-cloud-starter-security-version}</version>
##### </dependency>
#### 2、需要创建相关表模型，执行【secutiry表模型.sql】脚本
#### 3、Application类需要添加扫描security的mapper，@MapperScan(basePackages = {"com.www.common.config.security"})
#### 4、application.yml的必须是mapper-locations: classpath*:mapper/**/*.xml
#### 5、application.yml需要配置 com.www.common.securuty.enable : 是否开启Security安全配置,默认关闭
#### 6、application.yml需要配置 com.www.common.securuty.secret-key : jwt令牌签名，默认值wenzday
#### 7、application.yml需要配置 com.www.common.securuty.expire-time-second : 过期时间（秒），默认值259200
#### 8、application.yml需要配置 com.www.common.securuty.cookie-day : cookie免登录有效天数，默认3天
#### 9、application.yml需要配置 com.www.common.securuty.user-prefix : 使用redis保存用户的token的key前缀
#### 10、application.yml需要配置 com.www.common.securuty.role-redis-key : 角色访问权限信息的redis的key，不为空则缓存到redis中
#### 11、application.yml需要配置 com.www.common.securuty.key-expire-hour : 角色访问权限信息的redis的key的过期时间（单位小时）,默认1小时
