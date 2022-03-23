package com.www.common.config.security.handler;

import com.alibaba.fastjson.JSON;
import com.www.common.pojo.dto.response.ResponseDTO;
import com.www.common.pojo.enums.ResponseEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>@Description 会话过期处理 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2021/11/18 21:05 </p>
 */
@Slf4j
public class SessionExpiredHandler implements SessionInformationExpiredStrategy {

    /**
     * <p>@Description 构造方法 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/1/1 18:18 </p>
     * @return
     */
    public SessionExpiredHandler(){
        log.info("启动加载：Security认证自动配置类：注册security配置会话过期处理");
    }
    /**
     * <p>@Description 单点登录会话过期处理 </p>
     * <p>@Author www </p>
     * <p>@Date 2021/11/18 21:06 </p>
     * @param sessionInformationExpiredEvent
     * @return void
     */
    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent sessionInformationExpiredEvent) throws IOException, ServletException {
        log.info("6、security会话过期");
        ResponseDTO<String> responseDTO = new ResponseDTO<>(ResponseEnum.SUCCESS,"账号被挤下线");
        HttpServletResponse httpServletResponse = sessionInformationExpiredEvent.getResponse();
        httpServletResponse.setContentType("application/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(responseDTO));
    }
}
