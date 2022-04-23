package cn.maisann.reggie.service;

import cn.maisann.reggie.pojo.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

public interface OrdersService extends IService<Orders> {
    void saveWithDetail(Orders orders);
}
