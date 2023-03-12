### 使用com.www.common.config.feign下的自定义Feign配置说明：
#### 1、引入hystrix依赖
#### <dependency>
####    <groupId>org.springframework.cloud</groupId>
####    <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
####    <version>${spring-cloud-version}</version>
#### </dependency>
#### 2、引入hystrix依赖后，则自动配置负载均衡、hystrix策略和配置feign请求头
