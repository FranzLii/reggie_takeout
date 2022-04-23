package cn.maisann.reggie.service.impl;

import cn.maisann.reggie.mapper.AddressBookMapper;
import cn.maisann.reggie.pojo.AddressBook;
import cn.maisann.reggie.service.AddressBookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
