package com.ehrms.tmis.database.postgreSql.postgreSqlRepository.MasterRepos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_User;

import java.util.Optional;

@Repository
public interface M_UserRepository extends JpaRepository<M_User, Long> {

    Optional<M_User> findByEmailAndPassword(String email, String password);

    Optional<M_User> findByEmail(String email);

    Optional<M_User> findById(Long userId);

}
