package com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master;

import jakarta.persistence.*;

@Entity
public class M_User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long userId;

    private String userName;
    private String password;
    private String email;
    private String address;

    @ManyToOne
    @JoinColumn(name = "district_id")
    private M_District district;

    // Getters and setters
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public M_District getDistrict() {
        return district;
    }

    public void setDistrict(M_District district) {
        this.district = district;
    }
}
