package cn.maisann.reggie.service.impl;

import cn.maisann.reggie.mapper.OrderDetailMapper;
import cn.maisann.reggie.pojo.OrderDetail;
import cn.maisann.reggie.service.OrderDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
