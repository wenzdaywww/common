### 使用feign包下的自定义Feign配置说明：
* 引入hystrix依赖
````
<dependency>
   <groupId>org.springframework.cloud</groupId>
   <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
   <version></version>
</dependency>
````
* 引入hystrix依赖后，则自动配置负载均衡、hystrix策略和配置feign请求头
