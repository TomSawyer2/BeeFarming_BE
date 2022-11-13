package com.bf.modules.code.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bf.modules.code.model.Code;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface CodeMapper extends BaseMapper<Code> {

}