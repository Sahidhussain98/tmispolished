package com.ehrms.tmis.database.postgreSql.postgreSqlRepository.MasterRepos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_Program;

@Repository
public interface M_ProgramRepository extends JpaRepository<M_Program, Long> {

}
