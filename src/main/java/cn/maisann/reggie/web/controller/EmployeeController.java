package cn.maisann.reggie.web.controller;

import cn.maisann.reggie.common.R;
import cn.maisann.reggie.pojo.Employee;
import cn.maisann.reggie.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Objects;

@RestController
@Slf4j
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login(@RequestBody Employee employee, HttpSession session) {
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(lambdaQueryWrapper);
        if (Objects.isNull(emp)) {
            return R.error("用户名或密码错误");
        }
        if (!emp.getPassword().equals(password)) {
            return R.error("用户名或密码错误");
        }
        if (emp.getStatus() == 0) {
            return R.error("账号已禁用");
        }
        session.setAttribute("employee", emp.getId());
        return R.success(emp);

    }


    @PostMapping("/logout")
    public R logout(HttpSession session) {
        session.removeAttribute("employee");
        return R.success("退出成功");
    }


    @PostMapping
    public R addUser(@RequestBody Employee employee, HttpSession session) {
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employeeService.save(employee);
        return R.success("success");
    }

    @GetMapping("/page")
    public R<Page> page(Integer page, Integer pageSize, String name) {
        Page pageInfo = new Page(page, pageSize);
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(Strings.isNotEmpty(name), Employee::getName, name);
        lambdaQueryWrapper.orderByDesc(Employee::getUpdateTime);
        employeeService.page(pageInfo, lambdaQueryWrapper);
        return R.success(pageInfo);
    }

    @PutMapping
    public R<String> update(@RequestBody Employee employee,HttpSession session){
        employeeService.updateById(employee);
        return R.success("用户信息修改成功");
    }

    @GetMapping("/{id}")
    public R<Employee> getuser(@PathVariable Long id){
        return R.success(employeeService.getById(id));
    }


}
