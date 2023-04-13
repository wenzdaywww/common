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
* 在类或方法上使用@PreAuthorize("hasAnyAuthority('角色编码')")配置角色权限
