### 使用uaa包下的单点登录认证服务方配置
* 需引入以下依赖
````
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-oauth2</artifactId>
    <version></version>
</dependency>
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-jwt</artifactId>
    <version></version>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
    <version></version>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
    <version></version>
</dependency>
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version></version>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version></version>
</dependency>
````
* com.www.common.uaa.enable 是否开启uaa单点登录认证服务方配置，默认关闭false
* com.www.common.uaa.login  登录的http请求，即登录页面loginPage中form的action请求，默认/login
* com.www.common.uaa.logout 退出登录的http请求，即form的action请求，默认/logout
* com.www.common.uaa.login-url 自定义登录页面请求url，默认/uaa-login
* com.www.common.uaa.login-page 自定义登录页面thymeleaf文件存在路径，如：resource/uaa/uaa_login.html，只需配置uaa/uaa_login，默认uaa/uaa_login
* com.www.common.uaa.error 登录失败返回的Session的Attribute的名称，默认error
* com.www.common.uaa.confirm-page 自定义授权页面thymeleaf文件存在路径，如：resource/uaa/confirm.html，只需配置uaa/confirm，默认uaa/confirm
* com.www.common.uaa.client-id 自定义授权页面配置的资源服务方客户端的key，默认clientId
* com.www.common.uaa.scopes  自定义授权页面配置的资源服务方权限范围的key，默认scopes
* com.www.common.uaa.cookies-access-token  保存到cookie的access_token的key，默认access_token
* com.www.common.uaa.cookies-refresh-token  保存到cookie的refresh_token的key，默认refresh_token
* com.www.common.uaa.cookies-user  保存到cookie的user的key，默认user
* com.www.common.uaa.cookies-user-id  保存到cookie的user的ID的key，默认userId
* com.www.common.uaa.cookies-user-roles  保存到cookie的user的角色的key，默认roles
* com.www.common.uaa.signing-key  jwt令牌签名，即生成token的令牌签名，默认wenzday 
* com.www.common.uaa.token-key-prefix  用户登录的token保存到redis中的key的前缀，需冒号结尾，默认oauth2_token:user_token:
* 授权相关请求见ConfirmController.java和OauthController.java中的方法