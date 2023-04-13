### 使用security包下的Security安全配置要求：
* 需要引入依赖包
````
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-security</artifactId>
    <version></version>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
    <version></version>
</dependency>
````
* 需要创建相关表模型，执行【secutiry表模型.sql】脚本
* application.yml的必须是mapper-locationsclasspath*:mapper/**/*.xml
* com.www.common.securuty.enable 是否开启Security安全配置,默认关闭false
* com.www.common.securuty.secret-key jwt令牌签名，默认值wenzday
* com.www.common.securuty.token-expire-hour 用户token过期时间（单位小时）,默认48小时
* com.www.common.securuty.token-prefix 使用redis保存用户的token的key前缀,不需冒号结尾，代码已添加
* com.www.common.securuty.user-prefix 使用redis保存用户的角色信息的key前缀,不需冒号结尾，代码已添加
* com.www.common.securuty.user-expire-hour 保存用户的角色信息的redis的key的过期时间（单位小时）,默认48小时
* com.www.common.securuty.auth-redis-key 角色访问请求权限信息的redis的key，不为空则缓存到redis中
* com.www.common.securuty.auth-expire-hour 角色访问请求权限信息的redis的key的过期时间（单位小时）,默认48小时
* com.www.common.securuty.login 登录的http请求地址，即表单form中action的地址，默认/login
* com.www.common.securuty.name 登录的http请求的用户名key，即表单form中action的参数key，默认id
* com.www.common.securuty.password 登录的http请求的密码key，即表单form中action的参数key，默认pwd
* com.www.common.securuty.logout 退出登录的http请求地址，默认/logout
