package cn.maisann.reggie.dto;

import cn.maisann.reggie.pojo.Setmeal;
import cn.maisann.reggie.pojo.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
