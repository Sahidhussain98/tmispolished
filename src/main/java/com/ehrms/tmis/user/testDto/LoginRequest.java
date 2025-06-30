package com.ehrms.tmis.user.testDto;

public class LoginRequest {

    private String empCd;
    private String pwdtemp;

    public String getEmpCd() {
        return empCd;
    }

    public void setEmpCd(String empCd) {
        this.empCd = empCd;
    }

    public String getPwdtemp() {
        return pwdtemp;
    }

    public void setPwdtemp(String pwdtemp) {
        this.pwdtemp = pwdtemp;
    }

}
