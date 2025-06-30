package com.ehrms.tmis.user.testDto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class RoleAssignmentDTO {

    @NotBlank(message = "Employee code is required")
    private String empCd;

    @NotEmpty(message = "At least one role is required")
    private List<Long> roleIds;

    @NotNull(message = "District is required")
    private Long districtId;

    // Getters and setters
    public String getEmpCd() {
        return empCd;
    }

    public void setEmpCd(String empCd) {
        this.empCd = empCd;
    }

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }

    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }
}
