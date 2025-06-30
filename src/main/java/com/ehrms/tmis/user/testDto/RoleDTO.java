package com.ehrms.tmis.user.testDto;

import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_District;

public class RoleDTO {

    private Long roleId;
    private String roleName;

    private String empCd;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;

    }

    public String getEmpCd() {
        return empCd;
    }

    public void setEmpCd(String empCd) {
        this.empCd = empCd;
    }

    public RoleDTO(Long roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;

    }

    public RoleDTO(String empCd2, M_District districtId, Long userRoleId, Long[] roleIds) {

    }
}
