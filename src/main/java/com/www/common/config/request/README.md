### 使用request下的请求响应报文打印自动配置类说明：
* 需要引入aop依赖
````
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-aop</artifactId>
   <version></version>
</dependency>
````
* com.www.common.request.enable : 是否开启请求响应报文打印，默认关闭false
* com.www.common.request.content : 请求响应字段过长替换的字符串，默认为<longText>
* com.www.common.request.replace : 请求响应字段过长时是否开启字符串替换，默认开启true
* com.www.common.request.length : 请求响应字段过长时字符串长度限制，默认长度256
