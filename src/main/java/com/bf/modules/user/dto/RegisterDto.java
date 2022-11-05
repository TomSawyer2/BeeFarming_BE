package com.bf.modules.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
