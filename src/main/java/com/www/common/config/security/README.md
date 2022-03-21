### 使用com.www.common.config.security包下的Security安全配置要求：
#### 1、application.yml需要配置 com.www.common.securuty.enable : 是否开启Security安全配置
#### 2、application.yml需要配置 com.www.common.securuty.user-prefix : 使用redis保存用户的token的key前缀
#### 3、application.yml需要配置 com.www.common.securuty.secret-key : jwt令牌签名
#### 4、application.yml需要配置 com.www.common.securuty.expire-time-second : 过期时间（秒）
#### 5、application.yml需要配置 com.www.common.securuty.cookie-day : cookie免登录有效天数
