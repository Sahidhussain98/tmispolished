package com.tmisehrms.user.testDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeNameDTO {
    private String empcd;

    private String fullName;

    public EmployeeNameDTO(String empcd, String fullName) {
        this.empcd = empcd;
        this.fullName = fullName;
    }

    // Getters and setters (or use Lombok @Data)

}
