package cn.maisann.reggie.web.filter;

import cn.maisann.reggie.common.BaseContext;
import cn.maisann.reggie.common.R;
import cn.maisann.reggie.pojo.Employee;
import cn.maisann.reggie.pojo.LoginEmployee;
import cn.maisann.reggie.utils.JwtUtil;
import cn.maisann.reggie.utils.RedisCache;
import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.lang.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Slf4j
@Component
public class LoginCheckFilter extends OncePerRequestFilter {

    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Autowired
    RedisCache redisCache;


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {



        String token = httpServletRequest.getHeader("Authorization");

        //下段代码转为在Sercuirtonfig中配置
        /*         String requestURI = httpServletRequest.getRequestURI();
         String[] noFilterPath = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/sendMsg",
                "/user/login"
        };

        //如果请求资源为不需要验证url则直接放行
        if (check(noFilterPath, requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }
            public boolean check(String[] noFilterPath, String requestURI) {
        for (String urls : noFilterPath) {
            if (PATH_MATCHER.match(urls,requestURI)){
                return true;
            }
        }
        return false;
    }
*/

        //如果请求头不存在 Authorization 字段 直接放行请求，不在SecurirtyContext域中解析并存储对象
        if (!Strings.hasText(token)) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        token = token.replace("\"","");

        String loginId;

        //解析token字段 转化为employee / user : id 格式

        try{
            Claims claims = JwtUtil.parseJWT(token);
            loginId = claims.getSubject();
        }catch (Exception e){
            e.printStackTrace();
            httpServletResponse.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
            return;
        }

        //如果是员工登录
        if(loginId.startsWith("employee")){
            //获取登录用户的id
            Long empid = Long.valueOf(loginId.split(":")[1]);
            //存储ThreadLocal中 方便后续Meta调用填充公共字段
            BaseContext.setCurrentId(empid);
            //从redis中根据token内容获取已经登录的对象
            LoginEmployee loginEmployee = redisCache.getCacheObject(loginId);
//            LoginEmployee loginEmployee = (LoginEmployee) redisTemplate.opsForValue().get(loginId);
            //如果redis中不存在用户直接抛出异常交由Security的统一异常处理器处理
            if(Objects.isNull(loginEmployee)){
                throw new RuntimeException("用户登录状态异常!");
            }
            //存储SecurityContext域中，方便框架后续认证授权
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(loginEmployee,null,loginEmployee.getAuthorities()));
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }



   /*     if(loginId.startsWith("employee")){
            //获取登录用户的id
            Long empid = Long.valueOf(loginId.split(":")[1]);
            //存储ThreadLocal中 方便后续Meta调用填充公共字段
            BaseContext.setCurrentId(empid);
            //从redis中根据token内容获取已经登录的对象
            LoginEmployee loginEmployee = redisCache.getCacheObject(loginId);
            //如果redis中不存在用户直接抛出异常交由Security的统一异常处理器处理
            if(Objects.isNull(loginEmployee)){
                throw new RuntimeException("用户登录状态异常!");
            }
            //存储SecurityContext域中，方便框架后续认证授权
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(loginEmployee,null,loginEmployee.getAuthorities()));
        }

     */
        //如果是用户登录
        if(loginId.startsWith("user")){
            //获取登录用户的id
            Long empid = Long.valueOf(loginId.split(":")[1]);
            //存储ThreadLocal中 方便后续Meta调用填充公共字段
            BaseContext.setCurrentId(empid);
            //从redis中根据token内容获取已经登录的对象
            //TODO
//            LoginEmployee loginEmployee = redisCache.getCacheObject(loginId);
//            LoginEmployee loginEmployee = (LoginEmployee) redisTemplate.opsForValue().get(loginId);
            //如果redis中不存在用户直接抛出异常交由Security的统一异常处理器处理
//            if(Objects.isNull(loginEmployee)){
//                throw new RuntimeException("用户登录状态异常!");
//            }
//            //存储SecurityContext域中，方便框架后续认证授权
//            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(loginEmployee,null,loginEmployee.getAuthorities()));
//            filterChain.doFilter(httpServletRequest, httpServletResponse);
//            return;
        }


    //原始认证方式
       /* if (!Objects.isNull(request.getSession().getAttribute("employee"))) {

            Long empid = (Long) request.getSession().getAttribute("employee");

            BaseContext.setCurrentId(empid);

            filterChain.doFilter(request, response);

            return;
        }

        if (!Objects.isNull(request.getSession().getAttribute("user"))) {

            Long usrid = (Long) request.getSession().getAttribute("user");

            BaseContext.setCurrentId(usrid);

            filterChain.doFilter(request, response);
            return;
        }*/

        httpServletResponse.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }
}
