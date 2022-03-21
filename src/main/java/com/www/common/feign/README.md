### 使用com.www.common.feign包下的各应用服务接口要求：
#### 1、要使用哪个应用的服务，需要在application.yml配置 com.www.common.feign.<应用在feign包下的包名> = 应用名
#### 如：要使用blog应用服务，com.www.common.feign.blog = my-blog
#### 2、需要在启动类添加@EnableFeignClients(basePackages = {"com.www.common.feign"})