package cn.maisann.reggie.service.impl;

import cn.maisann.reggie.mapper.EmployeeMapper;
import cn.maisann.reggie.pojo.Employee;
import cn.maisann.reggie.pojo.LoginEmployee;
import cn.maisann.reggie.service.EmployeeService;
import cn.maisann.reggie.utils.JwtUtil;
import cn.maisann.reggie.utils.RedisCache;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    RedisCache redisCache;

    @Override
    public Map<String, String> login(Employee employee) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(employee.getUsername(),employee.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authentication);
        LoginEmployee loginEmployee = (LoginEmployee) authenticate.getPrincipal();

        redisCache.setCacheObject("employee:"+loginEmployee.getEmployee().getId(),loginEmployee);

        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", JwtUtil.createJWT("employee:"+loginEmployee.getEmployee().getId()));
        return tokenMap;
    }

}
