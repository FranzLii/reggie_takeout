package cn.maisann.reggie.service;

import cn.maisann.reggie.common.R;
import cn.maisann.reggie.pojo.Employee;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.Map;


public interface EmployeeService extends IService<Employee> {

    public Map<String,String> login(Employee employee);

}
