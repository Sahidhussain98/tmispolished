package com.ehrms.tmis.database.postgreSql.postgreSqlEntity.Transactional;

import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_District;
import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_Role;
import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_User;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_district_role_mapping") // Optional, use actual DB table name if different
@Getter
@Setter
public class T_UserDistrictMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userDistrictId")
    private Long userDistrictId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // maps to users(emp_cd)
    private M_User userId;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false) // maps to roles(id)
    private M_Role roleId;

    @ManyToOne
    @JoinColumn(name = "district_id", nullable = false) // maps to districts(id)
    private M_District districtId;
}
