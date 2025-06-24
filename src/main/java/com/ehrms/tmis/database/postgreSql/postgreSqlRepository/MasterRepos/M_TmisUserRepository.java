package com.ehrms.tmis.database.postgreSql.postgreSqlRepository.MasterRepos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_TmisUser;

@Repository
public interface M_TmisUserRepository extends JpaRepository<M_TmisUser, Long> {

    @Query("SELECT u FROM M_TmisUser u WHERE u.empCd = :empCd")
    Optional<M_TmisUser> findByEmpCd(@Param("empCd") String empCd);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM M_TmisUser u WHERE u.empCd = :empCd")
    boolean existsByEmpCd(@Param("empCd") String empCd);

    M_TmisUser findByTmisUserName(String username);

}