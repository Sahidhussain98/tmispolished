package com.tmisehrms.user.testDto;

import java.util.List;

public class UserWithRolesDTO {
    private String empCd;
    private String fullName;
    private List<RoleDTO> roles;

    public UserWithRolesDTO(String empCd, String fullName, List<RoleDTO> roles) {
        this.empCd = empCd;
        this.fullName = fullName;
        this.roles = roles;
    }

    // Getters and setters
    public String getEmpCd() {
        return empCd;
    }

    public void setEmpCd(String empCd) {
        this.empCd = empCd;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public List<RoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleDTO> roles) {
        this.roles = roles;
    }
}
