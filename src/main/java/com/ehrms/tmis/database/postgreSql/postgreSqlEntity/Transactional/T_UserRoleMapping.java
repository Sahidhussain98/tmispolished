package com.ehrms.tmis.database.postgreSql.postgreSqlEntity.Transactional;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_District;
import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_NatureOfStaff;

// Removed unused or incorrect import for Role
@Getter
@Setter
@Entity
public class T_UserRoleMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userRoleId;

    @Column(name = "EmpCd")
    private String empCd;

    @Column(name = "role_ids", columnDefinition = "bigint[]")
    @JdbcTypeCode(SqlTypes.ARRAY)
    private Long[] roleIds;

    @ManyToOne
    @JoinColumn(name = "districtId")
    private M_District districtId; // Assuming M_District is a valid class in your project

    @ManyToOne
    @JoinColumn(name = "natureOfStaffId")
    private M_NatureOfStaff NatureOfstaff; // Assuming M_District is a valid class in your project

}