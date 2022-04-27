package cn.maisann.reggie.common;


import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        authException.printStackTrace();
        String json = JSON.toJSONString("认证失败请重新登录 authentication error");
        response.getWriter().write(JSONUtils.toJSONString(json));
    }
}