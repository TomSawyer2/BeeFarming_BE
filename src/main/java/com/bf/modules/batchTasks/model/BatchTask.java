package com.bf.modules.batchTasks.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("batch_tasks")
public class BatchTask {
    @TableId(value = "id", type = IdType.AUTO)

    private Integer id;

    private String name;

    private Integer userId;

    private Integer status;
}
