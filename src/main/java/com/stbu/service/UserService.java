package com.stbu.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stbu.entities.UserEntity;
import com.stbu.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserService extends ServiceImpl<UserMapper, UserEntity> {


}
