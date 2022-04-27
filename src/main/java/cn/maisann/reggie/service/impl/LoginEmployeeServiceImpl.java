package cn.maisann.reggie.service.impl;

import cn.maisann.reggie.pojo.Employee;
import cn.maisann.reggie.pojo.LoginEmployee;
import cn.maisann.reggie.service.EmployeeService;
import cn.maisann.reggie.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class LoginEmployeeServiceImpl implements UserDetailsService {
    @Autowired
    EmployeeService employeeService;

    //实现UserDetailsService实现Authori...Manger实现调用该Service获得用户以及权限，并封装成UserDetail的实现类返回实现认证以及授权
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名从数据库查询对象
        LambdaQueryWrapper<Employee> wrapper =  new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getUsername,username);
        Employee employee = employeeService.getOne(wrapper);
        //如果用户名在数据库中不存在
        if(Objects.isNull(employee)){
            throw new RuntimeException("用户名或密码错误!");
        }
        //封装权限信息
        List<String> permissions = new ArrayList<>();
        if(employee.getName().equals("管理员")){
            permissions.add("admin");
        }

        permissions.add("employee");

        return new LoginEmployee(employee,permissions);
    }
}
