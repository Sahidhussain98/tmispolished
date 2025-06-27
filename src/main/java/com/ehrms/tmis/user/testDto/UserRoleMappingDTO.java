package com.tmisehrms.user.testDto;

import java.util.List;

public class UserRoleMappingDTO {
    private String empCd;
    private List<RoleDTO> roles;
    private DistrictDTO district;

    public UserRoleMappingDTO(String empCd, List<RoleDTO> roles, DistrictDTO district) {
        this.empCd = empCd;
        this.roles = roles;
        this.district = district;
    }

    public String getEmpCd() {
        return empCd;
    }

    public void setEmpCd(String empCd) {
        this.empCd = empCd;
    }

    public List<RoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleDTO> roles) {
        this.roles = roles;
    }

    public DistrictDTO getDistrict() {
        return district;
    }

    public void setDistrict(DistrictDTO district) {
        this.district = district;
    }

    @Override
    public String toString() {
        return "UserRoleMappingDTO{" +
                "empCd='" + empCd + '\'' +
                ", roles=" + roles +
                ", district=" + district +
                '}';
    }
}
