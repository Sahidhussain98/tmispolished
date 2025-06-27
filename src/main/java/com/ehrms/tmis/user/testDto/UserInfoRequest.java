package com.tmisehrms.user.testDto;

import java.util.List;

public class UserInfoRequest {

    private String empCd;
   
    private List<String> roles;

    public String getEmpCd() {
        return empCd;
    }

    public void setEmpCd(String empCd) {
        this.empCd = empCd;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }


    
}
