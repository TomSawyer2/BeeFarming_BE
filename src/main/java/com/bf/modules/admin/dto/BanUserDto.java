package com.bf.modules.admin.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class BanUserDto {
    @NotBlank
    private Integer userId;
}
