package com.ehrms.tmis.database.msSql.sqlEntity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class MUserMaster {
    @EmbeddedId
    private MUserMasterId id;

    @Column(name = "app_id", nullable = false)
    private Integer appId;

    @Column(name = "OfficeId")
    private Long officeId;

    @Nationalized
    @Column(name = "RoleId", length = 20)
    private String roleId;

    @Nationalized
    @Column(name = "Pwd", length = 256)
    private String pwd;

    @Column(name = "Stat", nullable = false)
    private Boolean stat = false;

    @Column(name = "Dt", nullable = false)
    private LocalDateTime dt;

    @Column(name = "TransId")
    private Long transId;

    @Column(name = "deptid", nullable = false, columnDefinition = "char(7)")
    private String deptid;

    @Column(name = "postdeptid", length = 7, columnDefinition = "char(7)")
    private String postdeptid;

    @Nationalized
    @Column(name = "pwdtemp", length = 20)
    private String pwdtemp;

    @Nationalized
    @Column(name = "DeptAssigned", length = 200)
    private String deptAssigned;

    @Column(name = "LevelCode")
    private Short levelCode;

    @Column(name = "EmpLevel")
    private Integer empLevel;

    @Column(name = "DEDate")
    private LocalDateTime dEDate;

    @Column(name = "DEUserId", length = 20)
    private String dEUserId;

    @Nationalized
    @Column(name = "CIPAdress", length = 20)
    private String cIPAdress;

    @Column(name = "NewRoleId", length = 3)
    private String newRoleId;

    @Column(name = "LoginAttempt")
    private Long loginAttempt;

}