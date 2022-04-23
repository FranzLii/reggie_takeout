package cn.maisann.reggie.web.controller;

import cn.maisann.reggie.common.R;
import cn.maisann.reggie.dto.OrdersDto;
import cn.maisann.reggie.pojo.OrderDetail;
import cn.maisann.reggie.pojo.Orders;
import cn.maisann.reggie.service.OrderDetailService;
import cn.maisann.reggie.service.OrdersService;
import cn.maisann.reggie.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/order")
public class OrdersController {

    @Autowired
    OrdersService ordersService;

    @Autowired
    OrderDetailService orderDetailService;

    @Autowired
    UserService userService;

    @PostMapping("/submit")
    public R<String> orderSubmit(@RequestBody Orders orders) {
        ordersService.saveWithDetail(orders);
        return R.success("下单成功");
    }

    @GetMapping("/userPage")
    public R<Page> userPage(int page, int pageSize) {
        Page pageinfo = new Page(page, pageSize);
        Page<OrdersDto> dtoPageInfo = new Page<>();

        BeanUtils.copyProperties(pageinfo,dtoPageInfo,"records");

        LambdaQueryWrapper<Orders> ordersLambdaQueryWrapper = new LambdaQueryWrapper<>();
        ordersLambdaQueryWrapper.orderByDesc(Orders::getOrderTime);
        ordersService.page(pageinfo, ordersLambdaQueryWrapper);
        List<Orders> records = pageinfo.getRecords();

        List<OrdersDto> ordersDtoList = records.stream().map(item -> {
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item, ordersDto);
            LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(OrderDetail::getOrderId, ordersDto.getId());
            ordersDto.setOrderDetails(orderDetailService.list(queryWrapper));
            return ordersDto;
        }).collect(Collectors.toList());

        dtoPageInfo.setRecords(ordersDtoList);

        return R.success(dtoPageInfo);
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String number, LocalDateTime beginTime, LocalDateTime endTime) {
        Page<Orders> pageInfo = new Page(page, pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Strings.isNotEmpty(number), Orders::getId, number);
        queryWrapper.gt(Objects.nonNull(beginTime), Orders::getOrderTime, beginTime);
        queryWrapper.lt(Objects.nonNull(endTime), Orders::getOrderTime, endTime);
        ordersService.page(pageInfo, queryWrapper);

        List<Orders> records = pageInfo.getRecords();
        for (Orders record : records) {
            record.setUserName(userService.getById(record.getUserId()).getName());
        }
        pageInfo.setRecords(records);

        log.info(pageInfo.getRecords().toString());
        return R.success(pageInfo);
    }

    @PutMapping
    public R<String> updateOrder(@RequestBody Orders orders){
        ordersService.updateById(orders);
        return R.success("修改订单状态成功!");
    }



}
