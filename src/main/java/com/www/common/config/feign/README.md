### 使用com.www.common.config.feign包下的feign接口配置要求：
#### 1、application.yml需要配置 feign.hystrix.enable = true : 是否开启多数据源配置
#### 2、application.yml需要配置 hystrix.shareSecurityContext = true : 是否开启共享SecurityContext
#### 3、application.yml需要配置 hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds = 超时时间（毫秒） : 配置超时时间
#### 4、启动类添加@RibbonClient(name = "${application.xml的com.www.common.feign.<应用在feign包下的包名>}",configuration = RestConfig.class)