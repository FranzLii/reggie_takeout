package cn.maisann.reggie.dto;

import cn.maisann.reggie.pojo.OrderDetail;
import cn.maisann.reggie.pojo.Orders;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OrdersDto extends Orders implements Serializable {
    private List<OrderDetail> orderDetails;
}
