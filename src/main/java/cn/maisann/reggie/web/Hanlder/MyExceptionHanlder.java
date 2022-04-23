package cn.maisann.reggie.web.Hanlder;

import cn.maisann.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
@ResponseBody
@Slf4j
public class MyExceptionHanlder {



    @ExceptionHandler
    public R<String> SqlExceptionHanlder(Exception e){
        e.printStackTrace();
        if(e.getMessage().contains("duplicate")){
            return R.error("账号重复了捏");
        }
        if(e.getMessage().contains("foreign")){
            return R.error("当前分类下的套餐或商品不为空");
        }
        return R.error("未知错误");
    }


}
