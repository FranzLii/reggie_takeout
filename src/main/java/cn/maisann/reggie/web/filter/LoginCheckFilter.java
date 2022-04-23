package cn.maisann.reggie.web.filter;

import cn.maisann.reggie.common.BaseContext;
import cn.maisann.reggie.common.R;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@WebFilter(urlPatterns = "/*", filterName = "loginCheckFilter")
@Slf4j
public class LoginCheckFilter implements Filter {

    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestURI = request.getRequestURI();

        String[] noFilterPath = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/sendMsg",
                "/user/login"
        };


        if (check(noFilterPath, requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!Objects.isNull(request.getSession().getAttribute("employee"))) {

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
        }

        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
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


}
