package com.ehrms.tmis.database.postgreSql.postgreSqlRepository.MasterRepos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_Role;

@Repository
public interface M_RoleRepository extends JpaRepository<M_Role, Long> {
}
