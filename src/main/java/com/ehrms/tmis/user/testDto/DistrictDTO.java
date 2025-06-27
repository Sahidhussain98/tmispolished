package com.tmisehrms.user.testDto;

public class DistrictDTO {
    private Long districtId;
    private String districtName;

    public DistrictDTO(Long districtId, String districtName) {
        this.districtId = districtId;
        this.districtName = districtName;
    }

    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }
}