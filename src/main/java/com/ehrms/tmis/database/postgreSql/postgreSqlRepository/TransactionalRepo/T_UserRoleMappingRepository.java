package com.ehrms.tmis.database.postgreSql.postgreSqlRepository.TransactionalRepo;

import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ehrms.tmis.database.postgreSql.postgreSqlEntity.Transactional.T_UserRoleMapping;

import jakarta.transaction.Transactional;

@Repository
public interface T_UserRoleMappingRepository extends JpaRepository<T_UserRoleMapping, Long> {
  List<T_UserRoleMapping> findByEmpCdIgnoreCase(String empCd);

  @Modifying
  @Transactional
  @Query("DELETE FROM T_UserRoleMapping u WHERE u.empCd = :empCd")
  void deleteByEmpCd(@Param("empCd") String empCd);

  // Ensure that this method matches the entity field name
  List<T_UserRoleMapping> findByEmpCd(String empCd);

  @Query("""
      SELECT m
        FROM T_UserRoleMapping m
       WHERE TRIM(m.empCd) IN :empCds
      """)
  List<T_UserRoleMapping> findByEmpCdIn(@Param("empCds") Collection<String> empCds);

}
