package cn.maisann.reggie.web.controller;

import cn.maisann.reggie.common.R;
import cn.maisann.reggie.pojo.User;
import cn.maisann.reggie.service.UserService;
import cn.maisann.reggie.utils.ValidateCodeUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    UserService userService;


    @Autowired
    RedisTemplate redisTemplate;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        String phone = user.getPhone();
        String s = ValidateCodeUtils.generateValidateCode4String(4);
//        session.setAttribute(phone,s);
        redisTemplate.opsForValue().set(phone,s,300, TimeUnit.SECONDS);
        log.info(s);
        return R.success("短信发送成功");
    }

    @PostMapping("/login")
    public R<String> login(@RequestBody Map map,HttpSession session){
        String phone = (String) map.get("phone");
//        String code = (String) session.getAttribute(phone);
        String code = (String) redisTemplate.opsForValue().get(phone);
        String vcode = (String) map.get("code");
        User user = new User();
        user.setPhone(phone);
        if(code != null && code.equals(vcode)){
            if(isNewUser(phone)){
                userService.save(user);
            }else{
                LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(User::getPhone,phone);
                user.setId(userService.getOne(queryWrapper).getId());
            }
            redisTemplate.delete(phone);
            session.setAttribute("user",user.getId());
            return R.success("登录成功");
        }

       return R.success("登陆失败");
    }

   public Boolean isNewUser(String PhoneNumber){
       LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
       queryWrapper.eq(User::getPhone,PhoneNumber);
       User user = userService.getOne(queryWrapper);
       return Objects.isNull(user);
   }

}
