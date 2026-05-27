package com.stbu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.stbu.entities.UserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {

}
