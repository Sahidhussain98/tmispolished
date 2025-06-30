package com.ehrms.tmis.user.testDto;

import java.util.ArrayList;
import java.util.List;

import com.ehrms.tmis.database.msSql.sqlEntity.MUserMaster;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MUserMasterDTO {

    private IdDTO id; // New nested ID class
    private int appId;
    private Long officeId;
    private List<String> roles = new ArrayList<>();

    // New field from stored procedure
    private String fullName;

    private DistrictDTO district;

    // Inner class to match the 'id' JSON structure
    public static class IdDTO {
        private int stateId;
        private String empCd;

        public IdDTO(int stateId, String empCd) {
            this.stateId = stateId;
            this.empCd = empCd;
        }

        public int getStateId() {
            return stateId;
        }

        public String getEmpCd() {
            return empCd;
        }
    }

    // Constructor to map from MUserMaster entity
    public MUserMasterDTO(MUserMaster user) {
        this.id = new IdDTO(user.getId().getStateId(), user.getId().getEmpCd());
        this.appId = user.getAppId();
        this.officeId = user.getOfficeId();

    }

    // Getters and setters for other fields (omitted for brevity)
    // ...
}
