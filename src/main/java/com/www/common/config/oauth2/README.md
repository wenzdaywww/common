### 使用com.www.common.config.oauth2包下的客户端统一认证资源服务方配置要求：
#### 1、application.yml需要配置 spring.application.name : 应用名称
#### 2、application.yml需要配置 com.www.common.oauth2.enable : 是否开启统一认证资源服务方配置
#### 3、application.yml需要配置 com.www.common.oauth2.signing-key : JWT令牌签名
#### 4、在类或方法上使用@PreAuthorize("hasAnyAuthority('角色编码')")配置角色权限
