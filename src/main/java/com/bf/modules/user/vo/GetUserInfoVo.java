package com.bf.modules.user.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class GetUserInfoVo {
    @NotBlank
    private Integer id;

    @NotBlank
    private String username;

    @NotBlank
    private Integer permission;

    @NotBlank
    private Integer status;

    private Integer batchTaskId;

}
