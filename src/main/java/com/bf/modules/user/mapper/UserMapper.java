package com.bf.modules.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bf.modules.user.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {

}
