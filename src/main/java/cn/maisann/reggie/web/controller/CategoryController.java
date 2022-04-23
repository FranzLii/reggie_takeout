package cn.maisann.reggie.web.controller;

import cn.maisann.reggie.common.R;
import cn.maisann.reggie.pojo.Category;
import cn.maisann.reggie.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody Category category) {
        categoryService.save(category);
        return R.success("添加分类成功");
    }

    @GetMapping("/page")
    public R<Page> page(Integer page, Integer pageSize) {
        Page<Category> pageinfo = new Page(page, pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Category::getSort);
        categoryService.page(pageinfo, queryWrapper);
        return R.success(pageinfo);
    }

    @DeleteMapping
    public R<String> delete(Long ids) {
        categoryService.removeById(ids);
        log.info(ids + "");
        return R.success("删除成功");
    }

    @PutMapping
    public R<String> update(@RequestBody Category category) {
        categoryService.updateById(category);
        return R.success("修改成功");
    }

    @GetMapping("/list")
    public R<List> list(Long type) {
        List<Category> list = null;
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Objects.nonNull(type),Category::getType, type);
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        list = categoryService.list(queryWrapper);

        return R.success(list);
    }
}
