package cn.maisann.reggie.web.controller;

import cn.maisann.reggie.common.R;
import cn.maisann.reggie.dto.DishDto;
import cn.maisann.reggie.pojo.Dish;
import cn.maisann.reggie.pojo.DishFlavor;
import cn.maisann.reggie.pojo.Setmeal;
import cn.maisann.reggie.service.CategoryService;
import cn.maisann.reggie.service.DishFlavorService;
import cn.maisann.reggie.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    DishService dishService;

    @Autowired
    DishFlavorService dishFlavorService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    CacheManager cacheManager;


    @PostMapping
    @CachePut(value = "DishDtoInfo",key = "#dishDto.id")
    public R<String> save(@RequestBody DishDto dishDto){
        dishService.savaWithFlavor(dishDto);
        return R.success("添加成功");
    }

    @GetMapping("/page")
    @Cacheable(value = "DishPageInfo",key = "#root.args[0] + '_' + #root.args[1]",unless = "#result.data == null")
    public R<Page> page(int page,int pageSize,String name){

        Page<Dish> pageinfo = new Page<>(page,pageSize);
        Page<DishDto> dtoPageInfo = new Page<>(page,pageSize);

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Strings.isNotEmpty(name),Dish::getName,name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(pageinfo,queryWrapper);

        BeanUtils.copyProperties(page,dtoPageInfo,"records");
        dtoPageInfo.setTotal(pageinfo.getTotal());

        List<Dish> dishList = pageinfo.getRecords();
        List<DishDto> dishDtoList = dishList.stream().map(item -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            dishDto.setCategoryName(categoryService.getById(item.getCategoryId()).getName());
            return dishDto;
        }).collect(Collectors.toList());
        log.debug(String.valueOf(dtoPageInfo.getTotal()));
        dtoPageInfo.setRecords(dishDtoList);
        return R.success(dtoPageInfo);

    }

    @GetMapping("/{id}")
    @Cacheable(value = "DishDtoInfo",key = "#result.data.id")
    public R<DishDto> getByid(@PathVariable Long id){
        Dish dish = dishService.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishDto.setFlavors(dishFlavorService.list(queryWrapper));
        dishDto.setCategoryName(categoryService.getById(dish.getCategoryId()).getName());
        return R.success(dishDto);
    }

    @PutMapping
    @CacheEvict(value = "DishDtoInfo",key = "#dishDto.id")
    public R<String> update(@RequestBody DishDto dishDto){
        dishService.updateWithFlavor(dishDto);
        return R.success("添加成功");
    }

    @GetMapping("/list")
    @Cacheable(value = "categortInfo",key = "#categoryId")
    public R<List> list(Long categoryId){
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Dish::getCategoryId,categoryId);
        List<Dish> list = dishService.list(queryWrapper);
        List<DishDto> dtoList;
        dtoList = list.stream().map(item -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
            dishDto.setFlavors(dishFlavorService.list(lambdaQueryWrapper));
            return dishDto;}
        ).collect(Collectors.toList());
        return R.success(dtoList);
    }

    @PostMapping("status/{statusid}")
    @CacheEvict(value = "DishPageInfo",allEntries = true)
    public R<String> updateStatus(@PathVariable("statusid") Integer statusId,Long[] ids){
        List<Dish> dishList = new ArrayList<>();
        for (Long id : ids) {
            Dish dish = new Dish();
            dish.setId(id);
            log.info(id.toString());
            dish.setStatus(statusId);
            dishList.add(dish);
        }
        dishService.updateBatchById(dishList);
        return R.success("修改状态成功");
    }

    @DeleteMapping
    @CacheEvict(value = "DishPageInfo",allEntries = true)
    public R<String> deleteSetmeals(Long[] ids){
        dishService.removeByIds(Arrays.asList(ids));
        return R.success("修改状态成功");
    }


}
