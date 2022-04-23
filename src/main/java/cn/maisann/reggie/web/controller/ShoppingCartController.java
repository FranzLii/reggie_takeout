package cn.maisann.reggie.web.controller;

import cn.maisann.reggie.common.BaseContext;
import cn.maisann.reggie.common.R;
import cn.maisann.reggie.pojo.ShoppingCart;
import cn.maisann.reggie.service.ShoppingCartService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@Slf4j
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        shoppingCart.setUserId(BaseContext.getCurrentId());
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        shoppingCart.setNumber(1);
        queryWrapper.eq(ShoppingCart::getUserId,shoppingCart.getUserId());
        queryWrapper.eq(Objects.nonNull(shoppingCart.getDishId()),ShoppingCart::getDishId,shoppingCart.getDishId());
        queryWrapper.eq(Objects.nonNull(shoppingCart.getSetmealId()),ShoppingCart::getSetmealId,shoppingCart.getSetmealId());

        ShoppingCart one = shoppingCartService.getOne(queryWrapper);

        if(Objects.nonNull(one)){

            one.setNumber(one.getNumber() + 1);
            shoppingCartService.updateById(one);
            return R.success(one);

        }else{

            shoppingCartService.save(shoppingCart);
            return R.success(shoppingCart);

        }

    }

    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        Long id = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,id);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);
    }

    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody Map map){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Objects.nonNull(map.get("dishId")),ShoppingCart::getDishId,map.get("dishId"));
        queryWrapper.eq(Objects.nonNull(map.get("setmealId")),ShoppingCart::getSetmealId,map.get("setmealId"));
        ShoppingCart one = shoppingCartService.getOne(queryWrapper);
        if(one.getNumber() - 1 <= 0){
            shoppingCartService.removeById(one.getId());
            one.setNumber(0);
        }else{
            one.setNumber(one.getNumber() - 1);
            shoppingCartService.updateById(one);
        }
        return R.success(one);
    }

    @DeleteMapping("/clean")
    public R<String> clean(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        shoppingCartService.remove(queryWrapper);
        return R.success("清空购物车成功");
    }

}
