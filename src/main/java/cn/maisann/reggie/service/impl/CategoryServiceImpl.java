package cn.maisann.reggie.service.impl;

import cn.maisann.reggie.mapper.CategoryMapper;
import cn.maisann.reggie.pojo.Category;
import cn.maisann.reggie.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
}
