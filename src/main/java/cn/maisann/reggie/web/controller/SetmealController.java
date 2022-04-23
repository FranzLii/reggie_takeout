package cn.maisann.reggie.web.controller;


import cn.maisann.reggie.common.R;
import cn.maisann.reggie.dto.SetmealDto;
import cn.maisann.reggie.pojo.Setmeal;
import cn.maisann.reggie.pojo.SetmealDish;
import cn.maisann.reggie.service.CategoryService;
import cn.maisann.reggie.service.SetmealDishService;
import cn.maisann.reggie.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    SetmealService setmealService;

    @Autowired
    SetmealDishService setmealDishService;

    @Autowired
    CategoryService categoryService;

    @PostMapping
    @Transactional
    public R<String> save(@RequestBody SetmealDto setmealDto){
        setmealService.save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream().map(item -> {
                    item.setSetmealId(setmealDto.getId());
                    return item;
                }
        ).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
        return R.success("添加成功!");
    }

    @GetMapping("/page")
    public R<Page<SetmealDto>> page(Long page,Long pageSize,String name){
        Page<Setmeal> pageinfo = new Page(page,pageSize);
        setmealService.page(pageinfo);
        List<Setmeal> records = pageinfo.getRecords();
        Page<SetmealDto> dtoPageInfo = new Page<>();
        BeanUtils.copyProperties(pageinfo,dtoPageInfo,"records");
        List<SetmealDto> dtoList = records.stream().map(item -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item,setmealDto);
            setmealDto.setCategoryName(categoryService.getById(setmealDto.getCategoryId()).getName());
            return setmealDto;
        }).collect(Collectors.toList());
        dtoPageInfo.setRecords(dtoList);
        return R.success(dtoPageInfo);
    }

    @GetMapping("/{id}")
    public R<SetmealDto> getOneById(@PathVariable("id")Long id){
        Setmeal byId = setmealService.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(byId,setmealDto);
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,id);
        setmealDto.setSetmealDishes(setmealDishService.list(queryWrapper));
        return R.success(setmealDto);
    }

    @PutMapping
    @Transactional
    public R<String> update(@RequestBody SetmealDto setmealDto){
        setmealService.updateById(setmealDto);
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,setmealDto.getId());
        setmealDishService.remove(queryWrapper);
        for (SetmealDish setmealDish : setmealDto.getSetmealDishes()) {
            setmealDish.setSetmealId(setmealDto.getId());
        }
        setmealDishService.saveBatch(setmealDto.getSetmealDishes());
        return R.success("修改成功");
    }

    @GetMapping("/list")
    public R<List> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Objects.nonNull(setmeal.getId()),Setmeal::getId,setmeal.getId());
        queryWrapper.eq(Objects.nonNull(setmeal.getStatus()),Setmeal::getStatus,setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(queryWrapper);
        return R.success(list);
    }

    @PostMapping("status/{statusid}")
    public R<String> updateStatus(@PathVariable("statusid") Integer statusId,Long[] ids){
        List<Setmeal> setmeals = new ArrayList<>();
        for (Long id : ids) {
            Setmeal setmeal = new Setmeal();
            setmeal.setId(id);
            setmeal.setStatus(statusId);
            setmeals.add(setmeal);
        }
        setmealService.updateBatchById(setmeals);
        return R.success("修改状态成功");
    }

    @DeleteMapping
    public R<String> deleteSetmeals(Long[] ids){
        setmealService.removeByIds(Arrays.asList(ids));
        return R.success("修改状态成功");
    }

}
