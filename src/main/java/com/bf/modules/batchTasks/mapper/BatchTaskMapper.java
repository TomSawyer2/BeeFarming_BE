package com.bf.modules.batchTasks.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bf.modules.batchTasks.model.BatchTask;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface BatchTaskMapper extends BaseMapper<BatchTask> {

}