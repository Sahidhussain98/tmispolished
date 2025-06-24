package com.ehrms.tmis.database.postgreSql.postgreSqlRepository.MasterRepos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_Session;

@Repository
public interface M_SessionRepository extends JpaRepository<M_Session, Long> {

}
