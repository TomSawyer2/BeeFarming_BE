package com.bf.modules.code.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("code")
public class Code {
    @TableId(value = "id", type = IdType.AUTO)

    private Integer id;

    private String content;

    private Integer userId;
}