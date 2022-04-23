package cn.maisann.reggie.service.impl;

import cn.maisann.reggie.mapper.ShoppingCartMapper;
import cn.maisann.reggie.pojo.ShoppingCart;
import cn.maisann.reggie.service.ShoppingCartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
