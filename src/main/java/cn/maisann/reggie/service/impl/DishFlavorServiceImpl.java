package cn.maisann.reggie.service.impl;

import cn.maisann.reggie.mapper.DishFlavorMapper;
import cn.maisann.reggie.pojo.DishFlavor;
import cn.maisann.reggie.service.DishFlavorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
