package com.ehrms.tmis.database.postgreSql.postgreSqlRepository.MasterRepos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_Menu;
import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.master.M_Process;

@Repository
public interface M_ProcessRepository extends JpaRepository<M_Process, Long> {
      List<M_Process> findByMenu(M_Menu menu);

      List<M_Process> findAllByRoles_RoleId(Long roleId);

}
