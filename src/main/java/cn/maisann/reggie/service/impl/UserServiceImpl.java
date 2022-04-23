package cn.maisann.reggie.service.impl;

import cn.maisann.reggie.mapper.UserMapper;
import cn.maisann.reggie.pojo.User;
import cn.maisann.reggie.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
