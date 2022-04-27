package cn.maisann.reggie;

import cn.maisann.reggie.pojo.LoginEmployee;
import cn.maisann.reggie.utils.JwtUtil;
import cn.maisann.reggie.utils.RedisCache;
import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@SpringBootTest
@Slf4j
public class test1 {
    @Autowired
    RedisCache redisCache;

    @Autowired
    AuthenticationManager authenticationManager;

    @Test
    public void test(){
        Authentication authentication = new UsernamePasswordAuthenticationToken("admin","123456");
//        LoginEmployee employee = (LoginEmployee) authentication.getPrincipal();
//        redisCache.setCacheObject("test:1",employee);
//        LoginEmployee cacheObject = redisCache.getCacheObject("test:1");
//        log.info(cacheObject.toString());
        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI0YjhiOGVhNjY3ZmU0Njc4OWZkM2E2NGY1ZDJmZTY2MCIsInN1YiI6ImVtcGxveWVlOjEiLCJpc3MiOiJzZyIsImlhdCI6MTY1MTA2NjQ5NiwiZXhwIjoxNjUxMDcwMDk2fQ.n_wfC74TaUopem5vGRTQiYRzAZLmMHgm-eReZLFzVGE";
        try {
            Claims claims = JwtUtil.parseJWT(jwt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
