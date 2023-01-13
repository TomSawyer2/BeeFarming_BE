package com.bf.modules.admin.vo;

import com.bf.modules.code.model.Code;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class GetCodeForAdminVo {
    @NotBlank
    private Integer page;

    @NotBlank
    private Integer pageSize;

    @NotBlank
    private Integer total;

    @NotBlank
    private List<Code> codeList;
}
