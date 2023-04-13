### 使用oauth2包下的客户端统一认证资源服务方配置要求：
* 需引入以下依赖
````
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-security</artifactId>
    <version></version>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-oauth2</artifactId>
    <version></version>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
    <version></version>
</dependency>
````
* 使用该客户端统一认证资源服务方配置必须有对应的认证服务方应用
* spring.application.name : 应用名称，必须配置
* com.www.common.oauth2.enable : 是否开启统一认证资源服务方配置，默认关闭false
* com.www.common.oauth2.signing-key : JWT令牌签名，即认证服务方应用用户token的令牌签名，默认wenzday
* com.www.common.oauth2.token-key-prefix  用户登录的token保存到redis中的key的前缀，结尾不含冒号，需与uaa包的认证服务方应用保持一致，默认oauth2_token:user_token:
* com.www.common.oauth2.url-scope-prefix  资源服务ID的url的scope的redis的key前缀,结尾不含冒号，格式如：oauth2:resource_id:url_scope
* 资源服务ID的url的scope的redis数据需要由任一应用写入
* 在类或方法上使用@PreAuthorize("hasAnyAuthority('角色编码')")配置角色权限
