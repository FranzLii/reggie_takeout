package cn.maisann.reggie.service;

import cn.maisann.reggie.dto.DishDto;
import cn.maisann.reggie.pojo.Dish;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

public interface DishService extends IService<Dish> {
    void savaWithFlavor(DishDto dishDto);

    void updateWithFlavor(DishDto dishDto);

}
