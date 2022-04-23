package cn.maisann.reggie.service.impl;

import cn.maisann.reggie.mapper.EmployeeMapper;
import cn.maisann.reggie.pojo.Employee;
import cn.maisann.reggie.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

}
