### com.www.common.config下的配置参考对应包中的README.md说明
### 其他配置：
#### 1、application.yml配置文件数据加密
##### 1.1、在启动类main方法中加入 System.setProperty("jasypt.encryptor.password","wenzday"); 如：
##### public static void main(String[] args) {
#####   System.setProperty("jasypt.encryptor.password","加密密钥，自定义");
#####   SpringApplication.run(LedgerApplication.class, args);
##### }
##### 1.2、application.yml使用如：password: ENC(FYI+VwCCxFQJmU7EeLCgw7CewV+hznTf) ENC括号中内容为加密后的数据