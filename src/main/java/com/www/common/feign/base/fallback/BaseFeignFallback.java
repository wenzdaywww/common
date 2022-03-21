package com.www.common.feign.base.fallback;

import com.www.common.feign.base.IBaseFeignService;
import com.www.common.pojo.dto.feign.UserInfoDTO;
import com.www.common.pojo.dto.response.ResponseDTO;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>@Description base应用的服务降级处理 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2022/1/21 19:47 </p>
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "com.www.common.feign",name = {"base"})
public class BaseFeignFallback implements FallbackFactory<IBaseFeignService> {
    /**
     * <p>@Description 服务降级处理 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/21 19:53 </p>
     * @param throwable
     * @return com.www.common.feign.IBlogFeignService
     */
    @Override
    public IBaseFeignService create(Throwable throwable) {
        log.error("base服务降级,失败原因:",throwable);
        return new IBaseFeignService() {
            /**
             * <p>@Description 查询多个用户信息 </p>
             * <p>@Author www </p>
             * <p>@Date 2022/1/23 15:43 </p>
             * @param userList 用户id集合
             * @return com.www.common.pojo.dto.response.ResponseDTO<com.www.common.pojo.dto.feign.UserInfoDTO>
             */
            @Override
            public ResponseDTO<List<UserInfoDTO>> findUserInfoList(List<String> userList) {
                log.error("base服务降级：查询多个用户信息");
                List<UserInfoDTO> list = new ArrayList<>();
                return new ResponseDTO<>(ResponseDTO.RespEnum.SUCCESS,list);
            }

            /**
             * <p>@Description 校验用户是否存在 </p>
             * <p>@Author www </p>
             * <p>@Date 2022/1/23 15:43 </p>
             * @param userList 用户id集合
             * @return com.www.common.pojo.dto.response.ResponseDTO<Boolean>
             */
            @Override
            public ResponseDTO<Boolean> validateUserExist(List<String> userList) {
                log.error("base服务降级：校验用户是否存在");
                return new ResponseDTO<>(ResponseDTO.RespEnum.SUCCESS,false);
            }
            /**
             * <p>@Description 查询用户信息 </p>
             * <p>@Author www </p>
             * <p>@Date 2022/1/23 15:43 </p>
             * @param userId 用户id
             * @return com.www.common.pojo.dto.response.ResponseDTO<com.www.common.pojo.dto.feign.UserInfoDTO>
             */
            @Override
            public ResponseDTO<UserInfoDTO> findUserInfo(String userId) {
                log.error("base服务降级：查询用户信息");
                UserInfoDTO userInfoDTO = new UserInfoDTO();
                userInfoDTO.setUserId(userId);
                return new ResponseDTO<>(ResponseDTO.RespEnum.SUCCESS,userInfoDTO);
            }
        };
    }
}
