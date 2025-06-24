package com.ehrms.tmis.database.postgreSql.postgreSqlEntity.Transactional;

import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_District;
import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_Role;
import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_User;

import jakarta.persistence.*;

@Entity
public class T_UserDistrictRoleMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The user this mapping belongs to
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private M_User user;

    // The district this user belongs to in this mapping
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "district_id", nullable = false)
    private M_District district;

    // The role assigned to the user in this district
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private M_Role role;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public M_User getUser() {
        return user;
    }

    public void setUser(M_User user) {
        this.user = user;
    }

    public M_District getDistrict() {
        return district;
    }

    public void setDistrict(M_District district) {
        this.district = district;
    }

    public M_Role getRole() {
        return role;
    }

    public void setRole(M_Role role) {
        this.role = role;
    }
}
