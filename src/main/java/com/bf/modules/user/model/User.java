package com.bf.modules.user.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("users")
public class User {
    @TableId(value = "id", type = IdType.AUTO)

    private Integer id;

    private String username;

    private String password;

    private Integer permission;
}
