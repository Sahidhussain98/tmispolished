package com.ehrms.tmis.user.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_User;
import com.ehrms.tmis.database.postgreSql.postgreSqlRepository.MasterRepos.M_UserRepository;

public class M_UserService {

    @Autowired
    private M_UserRepository userRepository;

    public M_User saveUser(M_User users) {
        return userRepository.save(users);
    } // Corrected

    public M_User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}
